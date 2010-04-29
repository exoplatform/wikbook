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

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class JavaCodeLink
{

   /** . */
   private final String member;

   /** . */
   private final String fqn;

   private JavaCodeLink(String member, String fqn)
   {
      this.member = member;
      this.fqn = fqn;
   }

   public String getMember()
   {
      return member;
   }

   public String getFQN()
   {
      return fqn;
   }

   public static JavaCodeLink parse(String s)
   {
      String member;
      String fqn;
      int poundPos = s.indexOf('#');
      if (poundPos == -1)
      {
         member = null;
         fqn = s;
      }
      else
      {
         member = s.substring(poundPos + 1);
         fqn = s.substring(0, poundPos);
      }

      //
      return new JavaCodeLink(member, fqn);
   }
}
