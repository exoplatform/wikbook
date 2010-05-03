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

import junit.framework.TestCase;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class StringClipperTestCase extends TestCase
{

   public void testCtorIAE()
   {
      StringClipper sb = new StringClipper("");
      try
      {
         sb.getOffset(Coordinate.get(-1, 0));
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
      try
      {
         sb.getOffset(Coordinate.get(0, -1));
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testGetCoordinates()
   {
      StringClipper b = new StringClipper("a\nb");
      assertEquals(Coordinate.get(0, 0), b.getPosition(0));
      assertEquals(Coordinate.get(0, 1), b.getPosition(1));
      assertEquals(Coordinate.get(1, 0), b.getPosition(2));
      assertEquals(Coordinate.get(1, 1), b.getPosition(3));
   }

   public void testOffset()
   {
      StringClipper b = new StringClipper("a\nb");
      assertEquals(0, b.getOffset(Coordinate.get(0, 0)));
      assertEquals(1, b.getOffset(Coordinate.get(0, 1)));
      assertEquals(1, b.getOffset(Coordinate.get(0, 2)));
      assertEquals(2, b.getOffset(Coordinate.get(1, 0)));
      assertEquals(3, b.getOffset(Coordinate.get(1, 1)));
      assertEquals(3, b.getOffset(Coordinate.get(1, 2)));
      assertEquals(3, b.getOffset(Coordinate.get(2, 0)));
   }

   public void testClip()
   {
      StringClipper b = new StringClipper("a\nb");
      assertEquals("", b.clip(Coordinate.get(0, 0), Coordinate.get(0 ,0)));
      assertEquals("a", b.clip(Coordinate.get(0, 0), Coordinate.get(0 ,1)));
      assertEquals("a", b.clip(Coordinate.get(0, 0), Coordinate.get(0 ,2)));
      assertEquals("a\n", b.clip(Coordinate.get(0, 0), Coordinate.get(1 ,0)));
      assertEquals("a\nb", b.clip(Coordinate.get(0, 0), Coordinate.get(1 ,1)));
      assertEquals("a\nb", b.clip(Coordinate.get(0, 0), Coordinate.get(1 ,2)));
      assertEquals("\nb", b.clip(Coordinate.get(0, 1), Coordinate.get(1 ,2)));
      assertEquals("\nb", b.clip(Coordinate.get(0, 2), Coordinate.get(1 ,2)));
      assertEquals("b", b.clip(Coordinate.get(1, 0), Coordinate.get(1 ,2)));
      assertEquals("", b.clip(Coordinate.get(1, 1), Coordinate.get(1 ,2)));
   }
}
