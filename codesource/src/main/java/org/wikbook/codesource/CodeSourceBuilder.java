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

import java.io.InputStream;
import java.util.Collection;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class CodeSourceBuilder
{

   /** . */
   final CodeSourceBuilderContext context;

   public CodeSourceBuilder(CodeSourceBuilderContext context)
   {
      if (context == null)
      {
         throw new NullPointerException();
      }
      this.context = context;
   }

   public CodeSourceBuilder()
   {
      this(new CodeSourceBuilderContext()
      {
         public InputStream getResource(String path)
         {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
         }
      });
   }

   public TypeSource buildClass(Class<?> clazz) throws CodeSourceException
   {
      Class<?> top = clazz;
      while (true)
      {
         Class<?> enclosing = top.getEnclosingClass();
         if (enclosing != null)
         {
            top = enclosing;
         }
         else
         {
            break;
         }
      }
      Collection<TypeSource> types = buildCompilationUnit(top.getName().replace(".", "/") + ".java");
      for (TypeSource type : types)
      {
         if (type.getName().equals(type.getName()))
         {
            return type;
         }
      }
      return null;
   }

   public TypeSource buildClass(final String fqn) throws CodeSourceException
   {
      String current = fqn;
      while (true)
      {
         try
         {
            String path = current.replace('.', '/') + ".java";
            Collection<TypeSource> types = buildCompilationUnit(path);
            for (TypeSource type : types)
            {
               if (type.getName().equals(fqn))
               {
                  return type;
               }
            }
         }
         catch (NoSuchSourceException e)
         {
            // Not found try prefixes
            // in order to address inner classes resolution
            int index = current.lastIndexOf('.');
            if (index == -1)
            {
               break;
            }
            else
            {
               current = current.substring(0, index);
            }
         }
      }
      return null;
   }

   public Collection<TypeSource> buildCompilationUnit(String compilationUnitPath) throws CodeSourceException
   {
      if (compilationUnitPath == null)
      {
         throw new NullPointerException();
      }
      try
      {
         CompilationUnitVisitor visitor = new CompilationUnitVisitor(this);
         Visit.CU visit = visitor.visit(compilationUnitPath);
         return visit.types;
      }
      catch (Exception e)
      {
         if (e instanceof CodeSourceException)
         {
            throw (CodeSourceException)e;
         }
         else
         {
            throw new CodeSourceException(e);
         }
      }
   }
}
