/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wikbook.codesource;

import japa.parser.ParseException;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.visitor.GenericVisitorAdapter;
import org.cojen.classfile.ClassFile;
import org.cojen.classfile.MethodDesc;
import org.cojen.classfile.MethodInfo;
import org.cojen.classfile.TypeDesc;
import org.wikbook.text.Clip;
import org.wikbook.text.TextArea;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class CompilationUnitVisitor extends GenericVisitorAdapter<Void, Visit>
{


   /** . */
   private final CodeSourceBuilder builder;

   CompilationUnitVisitor(CodeSourceBuilder builder)
   {
      this.builder = builder;
   }

   Visit.CU visit(String compilationUnitPath) throws IOException, ParseException, CodeSourceException
   {
      InputStream cuis = builder.context.getResource(compilationUnitPath);

      //
      if (cuis == null)
      {
         throw new NoSuchSourceException("Compilation path cannot be located " + compilationUnitPath);
      }

      //
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[20];
      for (int l = cuis.read(buffer); l != -1; l = cuis.read(buffer))
      {
         baos.write(buffer, 0, l);
      }
      String s = baos.toString();

      //
      Visit.CU visit = new Visit.CU(s);

      //
      visit.accept(this);

      //
      return visit;
   }

   private String toString(NameExpr n)
   {
      if (n instanceof QualifiedNameExpr)
      {
         QualifiedNameExpr qn = (QualifiedNameExpr)n;
         return qn.getQualifier() + "." + qn.getName();
      }
      else
      {
         return n.getName();
      }
   }

   @Override
   public Void visit(PackageDeclaration n, Visit arg)
   {
      ((Visit.CU)arg).pkg = toString(n.getName());
      return super.visit(n, arg);
   }

   private static interface SuperVisit<T extends TypeDeclaration>
   {
      Void visit(T td, Visit v);
   }

   /**
    * @param declaration the declaration
    * @param visit the current visit
    * @param superVisit the callback for continuing the visit
    * @param <D> the declaration generic type
    * @return the void
    */
   private <D extends TypeDeclaration> Void visit(D declaration, Visit.TD visit, SuperVisit<D> superVisit)
   {
      String pkg = visit.getPkg();
      String fqn = visit.getFQN();

      // Get path of the class
      String path = visit.getPath();

      //
      InputStream in = builder.context.getResource(path);
      if (in == null)
      {
         throw new CodeSourceException("Cannot locate class file for fqn " + fqn + " with path " + path);
      }

      //
      try
      {

         ClassFile cf = ClassFile.readFrom(in);

         //
         List<Signature> constructorSignatures = new ArrayList<Signature>();
         for (MethodInfo mi : cf.getConstructors())
         {
            MethodDesc desc = mi.getMethodDescriptor();
            List<String> parameterTypes = new ArrayList<String>();
            for (TypeDesc typeDesc : desc.getParameterTypes())
            {
               parameterTypes.add(typeDesc.getFullName());
            }
            constructorSignatures.add(new Signature(parameterTypes));
         }

         //
         List<Signature> methodSignatures = new ArrayList<Signature>();
         for (MethodInfo mi : cf.getMethods())
         {
            MethodDesc desc = mi.getMethodDescriptor();
            List<String> parameterTypes = new ArrayList<String>();
            for (TypeDesc typeDesc : desc.getParameterTypes())
            {
               parameterTypes.add(typeDesc.getFullName());
            }
            methodSignatures.add(new Signature(parameterTypes));
         }

         //
         visit.constructorSignatures = constructorSignatures.iterator();
         visit.methodSignatures = methodSignatures.iterator();
      }
      catch (IOException e)
      {
         throw new CodeSourceException("Could not load class " + fqn, e);
      }

      //
      visit.stack.addLast(new LinkedList<CodeSource>());

      //
      Void ret = superVisit.visit(declaration, visit);

      //
      List<CodeSource> blah = visit.stack.removeLast();

      //
      Clip typeClip = Clip.get(declaration.getBeginLine() - 1, 0, declaration.getEndLine() - 1, declaration.getEndColumn());

      //
      TypeSource typeSource = new TypeSource(
         new TextArea(visit.getCU().source),
         fqn,
         typeClip,
         visit.javaDoc(declaration));

      //
      for (CodeSource source : blah)
      {
         MemberSource methodSource = (MemberSource)source;
         typeSource.addMember(methodSource);
      }

      //
      visit.getCU().types.add(typeSource);

      //
      return ret;
   }

   @Override
   public Void visit(ClassOrInterfaceDeclaration n, Visit v)
   {
      return visit(n, new Visit.TD(v, n), new SuperVisit<ClassOrInterfaceDeclaration>()
      {
         public Void visit(ClassOrInterfaceDeclaration td, Visit v)
         {
            return CompilationUnitVisitor.super.visit(td, v);
         }
      });
   }

   @Override
   public Void visit(EnumDeclaration n, Visit v)
   {
      return visit(n, new Visit.TD(v, n), new SuperVisit<EnumDeclaration>()
      {
         public Void visit(EnumDeclaration td, Visit v)
         {
            return CompilationUnitVisitor.super.visit(td, v);
         }
      });
   }

   @Override
   public Void visit(AnnotationDeclaration n, Visit v)
   {
      return visit(n, new Visit.TD(v, n), new SuperVisit<AnnotationDeclaration>()
      {
         public Void visit(AnnotationDeclaration td, Visit v)
         {
            return CompilationUnitVisitor.super.visit(td, v);
         }
      });
   }

   private static Clip clip(BodyDeclaration body)
   {
      return Clip.get(body.getBeginLine() - 1, 0, body.getEndLine() - 1, body.getEndColumn());
   }

   @Override
   public Void visit(FieldDeclaration n, Visit v)
   {
      if (n.getVariables().size() == 1)
      {
         VariableDeclarator declarator = n.getVariables().get(0);
         VariableDeclaratorId id = declarator.getId();
         NamedMemberSource fieldSource = new NamedMemberSource(
            id.getName(),
            clip(n),
            v.javaDoc(n));
         ((Visit.TD)v).stack.getLast().addLast(fieldSource);
      }
      else
      {
         throw new UnsupportedOperationException("todo");
      }
      return super.visit(n, v);
   }

   @Override
   public Void visit(AnnotationMemberDeclaration n, Visit v)
   {
      SignedMemberSource memberSource = new SignedMemberSource(
         MemberKey.createSignedKey(n.getName()),
         clip(n),
         v.javaDoc(n));

      //
      ((Visit.TD)v).stack.getLast().addLast(memberSource);
      return super.visit(n, v);
   }

   @Override
   public Void visit(MethodDeclaration n, Visit v)
   {
      Signature signature = ((Visit.TD)v).methodSignatures.next();

      //
      SignedMemberSource methodSource = new SignedMemberSource(
         new MemberKey(n.getName(), signature),
         clip(n),
         v.javaDoc(n));

      //
      ((Visit.TD)v).stack.getLast().addLast(methodSource);
      return super.visit(n, v);
   }

   @Override
   public Void visit(ConstructorDeclaration n, Visit v)
   {
      Signature signature = ((Visit.TD)v).constructorSignatures.next();

      //
      SignedMemberSource methodSource = new SignedMemberSource(
         new MemberKey(n.getName(), signature),
         clip(n),
         v.javaDoc(n));

      //
      ((Visit.TD)v).stack.getLast().addLast(methodSource);
      return super.visit(n, v);
   }
}
