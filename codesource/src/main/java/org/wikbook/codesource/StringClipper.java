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
public class StringClipper
{

   /** . */
   final String s;

   public StringClipper(String s)
   {
      if (s == null)
      {
         throw new NullPointerException("No null string accepted");
      }
      this.s = s;
   }

   public String clip(Clip clip)
   {
      return clip(clip.getFrom(), clip.getTo());
   }

   public Coordinate getLength()
   {
      return getCoordinates(s.length());
   }

   public String clip(Coordinate from)
   {
      int _from = getOffset(from);
      int _to = s.length();
      if (_from > _to)
      {
         throw new IllegalArgumentException("Wrong clipping coordinates");
      }
      return s.substring(_from, _to);
   }

   public String clip(Coordinate from, Coordinate to)
   {
      int _from = getOffset(from);
      int _to = getOffset(to);
      if (_from > _to)
      {
         throw new IllegalArgumentException("Wrong clipping coordinates");
      }
      return s.substring(_from, _to);
   }

   public Coordinate getCoordinates(int offset)
   {
      if (offset < 0)
      {
         throw new IllegalArgumentException();
      }
      if (offset > s.length())
      {
         throw new IllegalArgumentException();
      }
      int line = 0;
      int column = 0;
      for (int i = 0;i < offset;i++)
      {
         char c = s.charAt(i);
         if (c == '\n')
         {
            column = 0;
            line++;
         }
         else
         {
            column++;
         }
      }
      return Coordinate.get(line, column);
   }

   /**
    * Returns the offset of the line / column coordinates. 
    *
    * @param coord the coordinate
    * @return the string offset
    */
   public int getOffset(Coordinate coord)
   {
      if (coord == null)
      {
         throw new NullPointerException();
      }
      int offset = 0;
      int lineOffset = coord.line;
      int columnOffset = coord.column;
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