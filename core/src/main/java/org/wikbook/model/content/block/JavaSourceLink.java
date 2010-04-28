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

package org.wikbook.model.content.block;

import org.wikbook.codesource.MethodKey;

import java.util.regex.Pattern;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class JavaSourceLink
{

   /** . */
   private final MethodKey methodKey;

   /** . */
   private final String fqn;

   private JavaSourceLink(MethodKey methodKey, String fqn)
   {
      this.methodKey = methodKey;
      this.fqn = fqn;
   }

   public MethodKey getMethodKey()
   {
      return methodKey;
   }

   public String getFQN()
   {
      return fqn;
   }

   public static JavaSourceLink parse(String s)
   {

      MethodKey methodKey;
      String fqn;
      int poundPos = s.indexOf('#');
      if (poundPos == -1)
      {
         methodKey = null;
         fqn = s;
      }
      else
      {
         String member = s.substring(poundPos + 1);
         methodKey = MethodKey.parse(member);
         fqn = s.substring(0, poundPos);
      }

/*
      String[] pkg;
      String clazz;
      int lastDotPos = fqn.lastIndexOf('.');
      if (lastDotPos == -1)
      {
         pkg = new String[0];
         clazz = fqn;
      }
      else
      {
         pkg = fqn.substring(0, lastDotPos).split("\\.");
         clazz = fqn.substring(lastDotPos + 1);
      }

      //
*/




      return new JavaSourceLink(methodKey, fqn);
   }
}
