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

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.JavadocComment;
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
import org.wikbook.text.Position;
import org.wikbook.text.TextArea;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class CompilationUnitVisitor extends GenericVisitorAdapter<Void, CompilationUnitVisitor.Visit>
{

   static class Visit
   {

      /** The current compilation unit. */
      private final CompilationUnit compilationUnit;

      /** . */
      final LinkedList<LinkedList<CodeSource>> stack = new LinkedList<LinkedList<CodeSource>>();

      /** . */
      String pkg;

      /** . */
      Iterator<Signature> constructorSignatures;

      /** . */
      Iterator<Signature> methodSignatures;

      /** . */
      final List<TypeSource> types = new ArrayList<TypeSource>();

      /** . */
      private String source;

      /** . */
      private final TextArea sb;

      private Visit(String source) throws ParseException
      {
         this.compilationUnit = JavaParser.parse(new ByteArrayInputStream(source.getBytes()));
         this.source = source;
         this.sb = new TextArea(source);
      }

      private String clip(Node node)
      {
         // Get offset of the fragment
         int from = sb.offset(Position.get(node.getBeginLine() - 1, 0));
         int to = sb.offset(Position.get(node.getEndLine() - 1, node.getEndColumn()));

         // Get relevant chars
         return source.substring(from, to);
      }

      private String javaDoc(BodyDeclaration node)
      {
         JavadocComment doc = node.getJavaDoc();
         return doc != null ? clip(doc) : null;
      }

      private void accept(CompilationUnitVisitor visitor)
      {
         compilationUnit.accept(visitor, this);
      }
   }


   /** . */
   private final CodeSourceBuilder builder;

   CompilationUnitVisitor(CodeSourceBuilder builder)
   {
      this.builder = builder;
   }

   Visit visit(String compilationUnitPath) throws IOException, ParseException, CodeSourceException
   {
      InputStream cuis = builder.context.getResource(compilationUnitPath);

      //
      if (cuis == null)
      {
         throw new CodeSourceException("Compilation path cannot be located " + compilationUnitPath);
      }

      //
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[20];
      for (int l = cuis.read(buffer);l != -1;l = cuis.read(buffer))
      {
         baos.write(buffer, 0, l);
      }
      String s = baos.toString();

      //
      Visit visit = new Visit(s);

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
      arg.pkg = toString(n.getName());
      return super.visit(n, arg);
   }

   private static interface SuperVisit<T extends TypeDeclaration> {
      Void visit(T td, Visit v);
   }

   private <T extends TypeDeclaration> Void visit(T n, Visit v, SuperVisit<T> superVisit)
   {
      String fqn = ((v.pkg == null || v.pkg.length() == 0) ? "" : (v.pkg + ".")) + n.getName();

      //
      InputStream in = builder.context.getResource(fqn.replace(".", "/") + ".class");
      if (in == null)
      {
         throw new CodeSourceException("Cannot locate class file for fqn " + fqn);   
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
         v.constructorSignatures = constructorSignatures.iterator();
         v.methodSignatures = methodSignatures.iterator();
      }
      catch (IOException e)
      {
         throw new CodeSourceException("Could not load class " + fqn, e);
      }

      //
      v.stack.addLast(new LinkedList<CodeSource>());

      //
      Void ret = superVisit.visit(n, v);

      //
      List<CodeSource> blah = v.stack.removeLast();

      //
      Clip typeClip = Clip.get(n.getBeginLine() - 1, 0, n.getEndLine() - 1, n.getEndColumn());

      //
      TypeSource typeSource = new TypeSource(
         new TextArea(v.source),
         fqn,
         typeClip,
         v.javaDoc(n));

      //
      for (CodeSource source : blah)
      {
         MemberSource methodSource = (MemberSource)source;
         typeSource.addMember(methodSource);
      }

      //
      v.types.add(typeSource);

      //
      return ret;
   }

   @Override
   public Void visit(ClassOrInterfaceDeclaration n, Visit v)
   {
      return visit(n, v, new SuperVisit<ClassOrInterfaceDeclaration>()
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
      return visit(n, v, new SuperVisit<EnumDeclaration>()
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
      return visit(n, v, new SuperVisit<AnnotationDeclaration>()
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
         v.stack.getLast().addLast(fieldSource);
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
      v.stack.getLast().addLast(memberSource);
      return super.visit(n, v);
   }

   @Override
   public Void visit(MethodDeclaration n, Visit v)
   {
      Signature signature = v.methodSignatures.next();

      //
      SignedMemberSource methodSource = new SignedMemberSource(
         new MemberKey(n.getName(), signature),
         clip(n),
         v.javaDoc(n));

      //
      v.stack.getLast().addLast(methodSource);
      return super.visit(n, v);
   }

   @Override
   public Void visit(ConstructorDeclaration n, Visit v)
   {
      Signature signature = v.constructorSignatures.next();

      //
      SignedMemberSource methodSource = new SignedMemberSource(
         new MemberKey(n.getName(), signature),
         clip(n),
         v.javaDoc(n));

      //
      v.stack.getLast().addLast(methodSource);
      return super.visit(n, v);
   }
}
