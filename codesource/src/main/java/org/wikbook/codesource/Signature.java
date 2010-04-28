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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class Signature
{

   /** . */
   private final List<String> parameterTypes;

   public Signature(String... parameterTypes)
   {
      this(Arrays.asList(parameterTypes));
   }

   public Signature(List<String> parameterTypes)
   {
      if (parameterTypes == null)
      {
         throw new NullPointerException();
      }

      //
      List<String> parameterTypesClone = new ArrayList<String>(parameterTypes);
      for (String parameterType : parameterTypesClone)
      {
         if (parameterType == null)
         {
            throw new IllegalArgumentException();
         }
      }

      //
      this.parameterTypes = parameterTypesClone;
   }

   @Override
   public int hashCode()
   {
      return parameterTypes.hashCode();
   }

   @Override
   public boolean equals(Object o)
   {
      if (o instanceof Signature)
      {
         Signature that = (Signature)o;
         return parameterTypes.equals(that.parameterTypes);
      }
      return false;
   }

   @Override
   public String toString()
   {
      StringBuffer sb = new StringBuffer("Signature[");
      for (int i = 0;i < parameterTypes.size();i++)
      {
         sb.append(i == 0 ? "" : ",");
         sb.append(parameterTypes.get(i));
      }
      sb.append("]");
      return sb.toString();
   }
}
