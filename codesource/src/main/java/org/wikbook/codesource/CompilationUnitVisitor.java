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
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.visitor.GenericVisitorAdapter;
import org.cojen.classfile.ClassFile;
import org.cojen.classfile.MethodDesc;
import org.cojen.classfile.MethodInfo;
import org.cojen.classfile.TypeDesc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class CompilationUnitVisitor extends GenericVisitorAdapter<Void, CompilationUnitVisitor.Visit>
{

   /** . */
   private CodeSourceBuilder builder;

   static class Visit
   {
      /** . */
      final LinkedList<LinkedList<BodySource>> stack = new LinkedList<LinkedList<BodySource>>();

      /** . */
      String pkg;

      /** . */
      Iterator<Signature> constructorSignatures;

      /** . */
      Iterator<Signature> methodSignatures;

      /** . */
      final List<TypeSource> types = new ArrayList<TypeSource>();

      /** . */
      private final String source;

      private Visit(String source)
      {
         this.source = source;
      }

      private String clip(Node node)
      {
         StringClipper sb = new StringClipper(source);

         // Get offset of the fragment
         int from = sb.getOffset(node.getBeginLine() - 1, 0);
         int to = sb.getOffset(node.getEndLine(), 0);

         // Get relevant chars
         return source.substring(from, to);
      }

      private String javaDoc(BodyDeclaration node)
      {
         JavadocComment doc = node.getJavaDoc();
         return doc != null ? clip(doc) : null;
      }
   }

   Visit visit(String compilationUnitPath) throws IOException, ParseException, CodeSourceException
   {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      InputStream cuis = cl.getResourceAsStream(compilationUnitPath);

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
      CompilationUnit cu = JavaParser.parse(new ByteArrayInputStream(s.getBytes()));

      //
      cu.accept(this, visit);

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

   @Override
   public Void visit(ClassOrInterfaceDeclaration n, Visit v)
   {
      String fqn = (v.pkg.length() == 0 ? "" : (v.pkg + ".")) + n.getName();

      //
      InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fqn.replace(".", "/") + ".class");
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
      v.stack.addLast(new LinkedList<BodySource>());
      Void ret = super.visit(n, v);
      List<BodySource> blah = v.stack.removeLast();

      //
      LinkedHashMap<MemberKey, MethodSource> methods = new LinkedHashMap<MemberKey, MethodSource>();
      LinkedHashMap<String, FieldSource> fields = new LinkedHashMap<String, FieldSource>();
      for (BodySource bodySource : blah)
      {
         if (bodySource instanceof MethodSource)
         {
            MethodSource methodSource = (MethodSource)bodySource;
            methods.put(methodSource.key, methodSource);
         }
         else if (bodySource instanceof FieldSource)
         {
            FieldSource fieldSource = (FieldSource)bodySource;
            fields.put(fieldSource.getName(), fieldSource);
         }
      }

      //
      TypeSource typeSource = new TypeSource(
         fqn,
         methods,
         fields,
         v.clip(n),
         v.javaDoc(n));

      //
      v.types.add(typeSource);

      //
      return ret;
   }

   @Override
   public Void visit(EnumDeclaration n, Visit v)
   {
      return super.visit(n, v);
   }

   @Override
   public Void visit(ConstructorDeclaration n, Visit v)
   {
      Signature signature = v.constructorSignatures.next();
      String constructorClip = v.clip(n);
      MethodSource methodSource = new MethodSource(
         new MemberKey(n.getName(), signature),
         constructorClip,
         v.javaDoc(n));
      v.stack.getLast().addLast(methodSource);
      return super.visit(n, v);
   }

   @Override
   public Void visit(FieldDeclaration n, Visit v)
   {
      if (n.getVariables().size() == 1)
      {
         VariableDeclarator declarator = n.getVariables().get(0);
         VariableDeclaratorId id = declarator.getId();
         FieldSource fieldSource = new FieldSource(
            id.getName(),
            v.clip(n),
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
   public Void visit(MethodDeclaration n, Visit v)
   {
      Signature signature = v.methodSignatures.next();
      String methodClip = v.clip(n);
      MethodSource methodSource = new MethodSource(
         new MemberKey(n.getName(), signature),
         methodClip,
         v.javaDoc(n));
      v.stack.getLast().addLast(methodSource);
      return super.visit(n, v);
   }
}
