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
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.LineComment;
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
import java.util.TreeMap;
import java.util.regex.Matcher;

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
      private String source;

      /** . */
      private final List<Anchor> anchors = new LinkedList<Anchor>();

      /** . */
      private final TextArea sb;

      /** . */
      private final String finalSource;

      private static class InternalAnchor
      {

         /** . */
         private final String id;

         /** . */
         private final int from;

         /** . */
         private final int to;

         private InternalAnchor(String id, int from, int to)
         {
            this.id = id;
            this.from = from;
            this.to = to;
         }
      }

      private Visit(String source) throws ParseException
      {
         // Build the initial unit
         CompilationUnit unit = JavaParser.parse(new ByteArrayInputStream(source.getBytes()));

         // Compute the number of lines
         int height = 0;
         for (int pos = 0;pos != -1; pos = source.indexOf(pos, '\n'))
         {
            height++;
         }

         // Handle all single line comment matching an anchor
         // Build a sorted map
         TreeMap<Integer, InternalAnchor> anchors = new TreeMap<Integer, InternalAnchor>();
         TextArea clipper = new TextArea(source);
         for (Comment comment : unit.getComments())
         {
            if (comment instanceof LineComment)
            {
               String c = comment.getContent();
               Matcher matcher = Anchor.LINE_COMMENT.matcher(c);
               if (matcher.matches())
               {
                  Position anchorStart = Position.get(comment.getBeginLine() - 1, comment.getBeginColumn() - 1);
                  Position anchorEnd = Position.get(comment.getEndLine() - 1, comment.getEndColumn() - 1);
                  int from = clipper.offset(anchorStart);
                  int to = clipper.offset(anchorEnd);
                  anchors.put(from, new InternalAnchor(matcher.group(1), from, to));
               }
            }
         }

         // Rebuild the source code
         LinkedList<Clip> clips = new LinkedList<Clip>();
         Position previous = Position.get(0, 0);
         for (InternalAnchor anchor : anchors.values())
         {
            //
            Position foo = clipper.position(anchor.from);

            // Build the clipping coordinate
            String aaaa = clipper.clip(Position.get(foo.getLine(), 0), foo);
            int column = foo.getColumn();
            for (int i = aaaa.length() - 1;i >= 0;i--)
            {
               if (aaaa.charAt(i) != ' ')
               {
                  break;
               }
               column = i;
            }

            //
            Position a = Position.get(foo.getLine(), column);

            //
            clips.addLast(Clip.get(previous, a));

            // THIS IS NOT GREAT BUT IT IS OK FOR NOW
            previous = Position.get(foo.getLine(), 100000);
         }
         clips.addLast(Clip.get(previous, clipper.length()));

         // Build the final string
         StringBuilder builder = new StringBuilder();
         for (Clip clip : clips)
         {
            builder.append(clipper.clip(clip));
         }

         //
         for (InternalAnchor ia : anchors.values())
         {
            this.anchors.add(new Anchor(ia.id, clipper.position(ia.from)));
         }

         //
         this.compilationUnit = JavaParser.parse(new ByteArrayInputStream(source.getBytes()));
         this.source = source;
         this.sb = new TextArea(source);
         this.finalSource = builder.toString();
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
      Clip typeClip = Clip.get(n.getBeginLine() - 1, 0, n.getEndLine() - 1, n.getEndColumn());

      //
      LinkedList<Anchor> anchors = new LinkedList<Anchor>();
      for (Anchor anchor : v.anchors)
      {
         if (typeClip.contains(anchor.getPosition()))
         {
            anchors.add(anchor);
         }
      }

      //
      TypeSource typeSource = new TypeSource(
         new TextArea(v.finalSource),
         fqn,
         typeClip,
         v.javaDoc(n),
         anchors);

      //
      for (BodySource bodySource : blah)
      {
         if (bodySource instanceof MethodSource)
         {
            MethodSource methodSource = (MethodSource)bodySource;
            typeSource.addMethod(methodSource);
         }
         else if (bodySource instanceof FieldSource)
         {
            FieldSource fieldSource = (FieldSource)bodySource;
            typeSource.addField(fieldSource);
         }
      }

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
      MethodSource methodSource = new MethodSource(
         new MemberKey(n.getName(), signature),
         Clip.get(n.getBeginLine() - 1, 0, n.getEndLine() - 1, n.getEndColumn()),
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
            Clip.get(n.getBeginLine() - 1, 0, n.getEndLine() - 1, n.getEndColumn()),
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
      MethodSource methodSource = new MethodSource(
         new MemberKey(n.getName(), signature),
         Clip.get(n.getBeginLine() - 1, 0, n.getEndLine() - 1, n.getEndColumn()),
         v.javaDoc(n));
      v.stack.getLast().addLast(methodSource);
      return super.visit(n, v);
   }
}
