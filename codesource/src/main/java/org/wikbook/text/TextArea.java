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

package org.wikbook.text;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TextArea
{

   /** . */
   private String s;

   public TextArea(String s)
   {
      if (s == null)
      {
         throw new NullPointerException("No null string accepted");
      }
      this.s = s;
   }

   public TextArea insert(Position position, CharSequence seq)
   {
      int offset = offset(Position.get(position.line, 0));

      //
      StringBuffer sb = new StringBuffer();

      //
      sb.append(s.substring(0, offset));

      //
      int nextCR = s.indexOf('\n', offset);
      if (nextCR == -1)
      {
         nextCR = s.length();
      }
      for (int i = 0;i < position.column;i++)
      {
         if (offset < nextCR)
         {
            sb.append(s.charAt(offset++));
         }
         else
         {
            sb.append(' ');
         }
      }

      //
      sb.append(seq);

      //
      sb.append(s.substring(offset));

      //
      s = sb.toString();
      return this;
   }

   public String getText()
   {
      return s;
   }

   public String clip(Clip clip)
   {
      return clip(clip.getFrom(), clip.getTo());
   }

   public Position length()
   {
      return position(s.length());
   }

   public String clip(Position from)
   {
      int _from = offset(from);
      int _to = s.length();
      if (_from > _to)
      {
         throw new IllegalArgumentException("Wrong clipping Positions");
      }
      return s.substring(_from, _to);
   }

   public String clip(Position from, Position to)
   {
      int _from = offset(from);
      int _to = offset(to);
      if (_from > _to)
      {
         throw new IllegalArgumentException("Wrong clipping Positions");
      }
      return s.substring(_from, _to);
   }

   /**
    * Compute the position of the specified offset.
    *
    * @param offset the offset
    * @return the position
    */
   public Position position(int offset)
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
      return Position.get(line, column);
   }

   /**
    * Compute the offset of the line / column Position.
    *
    * @param position the Position
    * @return the offset in the string
    * @throws NullPointerException if the position is null
    */
   public int offset(Position position) throws NullPointerException
   {
      if (position == null)
      {
         throw new NullPointerException();
      }
      int offset = 0;
      int lineOffset = position.line;
      int columnOffset = position.column;
      while (true)
      {
         if (lineOffset == 0 && columnOffset == 0)
         {
            break;
         }

         //
         if (offset >= s.length())
         {
            throw new IllegalArgumentException();
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