package org.wikbook.codesource;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.TypeDeclaration;
import org.wikbook.text.Position;
import org.wikbook.text.TextArea;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
abstract class Visit
{
   
   static class CU extends Visit
   {

      /** The current compilation unit. */
      private final CompilationUnit compilationUnit;

      /** . */
      final String source;

      /** . */
      private final TextArea sb;

      /** . */
      String pkg;

      /** . */
      final List<TypeSource> types;

      CU(String source) throws ParseException
      {
         this.compilationUnit = JavaParser.parse(new ByteArrayInputStream(source.getBytes()));
         this.source = source;
         this.sb = new TextArea(source);
         this.types = new ArrayList<TypeSource>();
      }

      @Override
      void appendPathPrefix(StringBuilder path)
      {
         if (pkg.length() > 0)
         {
            path.append(pkg.replace('.', '/')).append('/');
         }
      }

      @Override
      void appendFQNPrefix(StringBuilder path)
      {
         if (pkg.length() > 0)
         {
            path.append(pkg).append('.');
         }
      }

      @Override
      CU getCU()
      {
         return this;
      }
   }
   
   static class TD extends Visit
   {

      /** . */
      final Visit parent;

      /** . */
      final LinkedList<LinkedList<CodeSource>> stack = new LinkedList<LinkedList<CodeSource>>();

      /** . */
      Iterator<Signature> constructorSignatures;

      /** . */
      Iterator<Signature> methodSignatures;

      /** . */
      final TypeDeclaration decl;

      TD(Visit parent, TypeDeclaration decl)
      {
         this.parent = parent;
         this.decl = decl;
      }

      String getPath()
      {
         StringBuilder path = new StringBuilder();
         parent.appendPathPrefix(path);
         path.append(decl.getName());
         path.append(".class");
         return path.toString();
      }
      
      String getFQN()
      {
         StringBuilder fqn = new StringBuilder();
         parent.appendFQNPrefix(fqn);
         fqn.append(decl.getName());
         return fqn.toString();
      }

      @Override
      void appendPathPrefix(StringBuilder path)
      {
         parent.appendPathPrefix(path);
         path.append(decl.getName());
         path.append('$');
      }

      @Override
      void appendFQNPrefix(StringBuilder path)
      {
         parent.appendFQNPrefix(path);
         path.append(decl.getName());
         path.append('.');
      }

      @Override
      CU getCU()
      {
         return parent.getCU();
      }
   }

   abstract CU getCU();

   abstract void appendPathPrefix(StringBuilder path);
   
   abstract void appendFQNPrefix(StringBuilder path);
   
   final String clip(Node node)
   {
      CU cu = getCU();
      
      // Get offset of the fragment
      int from = cu.sb.offset(Position.get(node.getBeginLine() - 1, 0));
      int to = cu.sb.offset(Position.get(node.getEndLine() - 1, node.getEndColumn()));

      // Get relevant chars
      return cu.source.substring(from, to);
   }

   final String javaDoc(BodyDeclaration node)
   {
      JavadocComment doc = node.getJavaDoc();
      return doc != null ? clip(doc) : null;
   }
   
   final String getPkg()
   {
      return getCU().pkg;
   }

   final void accept(CompilationUnitVisitor visitor)
   {
      getCU().compilationUnit.accept(visitor, this);
   }
}
