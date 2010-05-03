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
public final class Coordinate
{

   public static Coordinate get(int line, int column)
   {
      return new Coordinate(line, column);
   }

   /** . */
   final int line;

   /** . */
   final int column;

   private Coordinate(int line, int column)
   {
      if (line < 0)
      {
         throw new IllegalArgumentException();
      }
      if (column < 0)
      {
         throw new IllegalArgumentException();
      }
      this.line = line;
      this.column = column;
   }

   public int getLine()
   {
      return line;
   }

   public int getColumn()
   {
      return column;
   }

   @Override
   public int hashCode()
   {
      return (line + 1) * (column + 1);
   }

   @Override
   public boolean equals(Object o)
   {
      return (o == this) || (o instanceof Coordinate && line == ((Coordinate)o).line && column == ((Coordinate)o).column);
   }

   @Override
   public String toString()
   {
      return "Coordinate[line=" + line + ",column=" + column + "]";
   }
}
