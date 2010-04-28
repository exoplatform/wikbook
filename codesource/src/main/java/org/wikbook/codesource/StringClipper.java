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

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class StringClipper
{

   /** . */
   private final String s;

   public StringClipper(String s)
   {
      if (s == null)
      {
         throw new NullPointerException("No null string accepted");
      }
      this.s = s;
   }

   public String clip(int fromLine, int fromColumn, int toLine, int toColumn)
   {
      int from = getOffset(fromLine, fromColumn);
      int to = getOffset(toLine, toColumn);
      if (from > to)
      {
         throw new IllegalArgumentException("Wrong clipping coordinates");
      }
      return s.substring(from, to);
   }

   /**
    * Returns the offset of the line / column coordinates. 
    *
    * @param lineOffset the line offset
    * @param columnOffset the column offset
    * @return the string offset
    */
   public int getOffset(int lineOffset, int columnOffset)
   {
      if (lineOffset < 0)
      {
         throw new IllegalArgumentException("Line offset cannot be negative: " + lineOffset);
      }
      if (columnOffset < 0)
      {
         throw new IllegalArgumentException("Column offset cannot be negative: " + columnOffset);
      }
      int offset = 0;
      while (true)
      {
         if (lineOffset == 0 && columnOffset == 0)
         {
            break;
         }

         //
         if (offset >= s.length())
         {
            break;
         }
         char next = s.charAt(offset);

         //
         if (lineOffset > 0)
         {
            if (next == '\n')
            {
               lineOffset--;
            }
         }
         else
         {
            if (next == '\n' && columnOffset > 0)
            {
               break;
            }
            else
            {
               columnOffset--;
            }
         }

         //
         offset++;
      }
      return offset;
   }
}