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

package org.wikbook.xwiki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class PatternFilter
{


   public static class Escape extends PatternFilter
   {

      /** . */
      private static final Pattern PATTERN = Pattern.compile("\\\\(.)");

      /** . */
      private final String before;

      /** . */
      private final String after;

      protected Escape(String before, String after)
      {
         super(PATTERN);

         //
         this.before = before;
         this.after = after;
      }

      @Override
      protected void appendMatch(StringBuilder builder, String match)
      {
         if ("\\".equals(match))
         {
            builder.append("\\");
         }
         else
         {
            if (before != null)
            {
               builder.append(before);
            }
            builder.append(match);
            if (before != null)
            {
               builder.append(after);
            }
         }
      }
   }

   public abstract static class Properties extends PatternFilter
   {

      /** . */
      private static final Pattern PATTERN = Pattern.compile("\\$\\[(\\w[\\w\\.]*)\\]");

      protected Properties()
      {
         super(PATTERN);
      }

      protected void appendMatch(StringBuilder builder, String match)
      {
         String propertyValue = resolveProperty(match);
         if (propertyValue != null)
         {
            builder.append(propertyValue);
         }
         else
         {
            builder.append("$[").append(match).append("]");
         }
      }

      protected abstract String resolveProperty(String propertyname);

   }

   /** . */
   private final Pattern pattern;

   private PatternFilter(Pattern pattern)
   {
      this.pattern = pattern;
   }

   protected abstract void appendMatch(StringBuilder builder, String match);

   public String filter(String s)
   {
      Matcher matcher = pattern.matcher(s);
      StringBuilder builder = new StringBuilder();
      int pre = 0;
      while (matcher.find())
      {
         builder.append(s, pre, matcher.start());
         String propertyName = matcher.group(1);
         appendMatch(builder, propertyName);
         pre = matcher.end();
      }
      builder.append(s, pre, s.length());
      return builder.toString();
   }
}
