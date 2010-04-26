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

package org.wikbook.apt;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.visitor.GenericVisitorAdapter;
import org.wikbook.apt.annotations.Documented;
import org.wikbook.apt.model.Catalog;
import org.wikbook.apt.model.Fragment;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class CatalogBuilder extends GenericVisitorAdapter<Void, Data>
{

   /** . */
   private final Catalog catalog;

   CatalogBuilder()
   {
      this.catalog = new Catalog();
   }

   public void build(String annoationId, String fragmentId, JavaFileObject jfo) throws IOException, ParseException
   {
      Data data = new Data(fragmentId, annoationId, new StringBuilder().append(jfo.getCharContent(true)).toString());
      CompilationUnit cu = JavaParser.parse(jfo.openInputStream());
      cu.accept(this, data);
   }

   @Override
   public Void visit(ClassOrInterfaceDeclaration n, Data arg)
   {
      is(n, arg);
      return super.visit(n, arg);
   }

   @Override
   public Void visit(EnumDeclaration n, Data arg)
   {
      is(n, arg);
      return super.visit(n, arg);
   }

   @Override
   public Void visit(ConstructorDeclaration n, Data arg)
   {
      is(n, arg);
      return super.visit(n, arg);
   }

   @Override
   public Void visit(FieldDeclaration n, Data arg)
   {
      is(n, arg);
      return super.visit(n, arg);
   }

   @Override
   public Void visit(MethodDeclaration n, Data arg)
   {
      is(n, arg);
      return super.visit(n, arg);
   }

   public Catalog getCatalog()
   {
      return catalog;
   }

   private void is(BodyDeclaration body, Data arg)
   {
      List<AnnotationExpr> annotations = body.getAnnotations();
      if (annotations != null)
      {
         for (AnnotationExpr ae : annotations)
         {
            if (ae.getName().getName().equals(Documented.class.getSimpleName()))
            {
               boolean found = false;
               if (ae instanceof MarkerAnnotationExpr)
               {
                  found = arg.annotationId.length() == 0;
               }
               else
               {
                  NormalAnnotationExpr nae = (NormalAnnotationExpr)ae;
                  String id;
                  if (nae.getPairs() == null || nae.getPairs().size() == 0)
                  {
                     id = "";
                  }
                  else
                  {
                     MemberValuePair mvp = nae.getPairs().get(0);
                     id = ((StringLiteralExpr)mvp.getValue()).getValue();
                  }
                  found = id.equals(arg.fragmentId);
               }
               if (found)
               {
                  StringBrowser sb = new StringBrowser(arg.fileContent);

                  // Get offset of the fragment
                  int from = sb.getOffset(body.getBeginLine() - 1, 0);
                  int to = sb.getOffset(body.getEndLine(), 0);

                  // Get relevant chars
                  char[] chars = arg.fileContent.substring(from, to).toCharArray();

                  // Get coordinate of annotation to erase
                  int o1 = sb.getOffset(ae.getBeginLine() - 1, ae.getBeginColumn()) - from - 1;
                  int o2 = sb.getOffset(ae.getEndLine() - 1, ae.getEndColumn()) - from;

                  // Erase chars
                  for (int i = o1;i < o2;i++)
                  {
                     chars[i] = ' ';
                  }

                  // Create final fragment
                  String fragment = new String(chars);

                  //
                  catalog.addFragment(arg.fragmentId, new Fragment("", fragment));
               }
            }
         }
      }
   }
}
