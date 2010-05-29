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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public final class MemberKey
{

   public static MemberKey createNamedKey(String name)
   {
      return new MemberKey(name, null);
   }

   public static MemberKey createSignedKey(String name, String... parameterTypes)
   {
      return new MemberKey(name, new Signature(Arrays.asList(parameterTypes)));
   }

   public static MemberKey parse(String member)
   {
      Pattern pattern = Pattern.compile(
         "^\\s*" +
            "([^\\(\\)]+)" +
            "(\\(" +
            "([^\\(\\)]*)?" +
            "\\))?" +
            "\\s*$");
      Matcher matcher = pattern.matcher(member);
      if (!matcher.matches())
      {
         throw new CodeSourceException(member + " is not a member");
      }
      Signature signature = null;
      String name = matcher.group(1);

      String signatureString = matcher.group(3);
      if (signatureString != null)
      {
         List<String> parameterTypes = Collections.emptyList();
         signatureString = signatureString.trim();
         if (signatureString.length() > 0)
         {
            parameterTypes = Arrays.asList(signatureString.split("\\s*,\\s*"));
         }
         signature = new Signature(parameterTypes);
      }

      return new MemberKey(name, signature);
   }

   /** . */
   final String name;

   /** . */
   final Signature signature;

   MemberKey(String name, Signature signature)
   {
      if (name == null)
      {
         throw new NullPointerException();
      }

      //
      this.name = name;
      this.signature = signature;
   }

   @Override
   public int hashCode()
   {
      return name.hashCode() ^ signature.hashCode();
   }

   @Override
   public boolean equals(Object o)
   {
      if (o == this)
      {
         return true;
      }
      if (o instanceof MemberKey)
      {
         MemberKey that = (MemberKey)o;
         return name.equals(that.name) && (signature == null ? that.signature == null : signature.equals(that.signature));
      }
      return false;
   }

   @Override
   public String toString()
   {
      return "MethodKey[name=" + name + ",signature=" + signature + "]";
   }
}