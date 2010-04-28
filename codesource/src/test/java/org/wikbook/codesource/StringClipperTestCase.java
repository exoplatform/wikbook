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
         sb.getOffset(-1, 0);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
      try
      {
         sb.getOffset(0, -1);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testOffset()
   {
      StringClipper b = new StringClipper("a\nb");
      assertEquals(0, b.getOffset(0, 0));
      assertEquals(1, b.getOffset(0, 1));
      assertEquals(1, b.getOffset(0, 2));
      assertEquals(2, b.getOffset(1, 0));
      assertEquals(3, b.getOffset(1, 1));
      assertEquals(3, b.getOffset(1, 2));
      assertEquals(3, b.getOffset(2, 0));
   }

   public void testClip()
   {
      StringClipper b = new StringClipper("a\nb");
      assertEquals("", b.clip(0, 0, 0 ,0));
      assertEquals("a", b.clip(0, 0, 0 ,1));
      assertEquals("a", b.clip(0, 0, 0 ,2));
      assertEquals("a\n", b.clip(0, 0, 1 ,0));
      assertEquals("a\nb", b.clip(0, 0, 1 ,1));
      assertEquals("a\nb", b.clip(0, 0, 1 ,2));
      assertEquals("\nb", b.clip(0, 1, 1 ,2));
      assertEquals("\nb", b.clip(0, 2, 1 ,2));
      assertEquals("b", b.clip(1, 0, 1 ,2));
      assertEquals("", b.clip(1, 1, 1 ,2));
   }
}
