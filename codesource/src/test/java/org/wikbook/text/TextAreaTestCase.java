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

import junit.framework.TestCase;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TextAreaTestCase extends TestCase
{

   public void testIAE()
   {
      TextArea sb = new TextArea("");
      try
      {
         sb.offset(Position.get(-1, 0));
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
      try
      {
         sb.offset(Position.get(0, -1));
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testInsert()
   {
      assertEquals("ca\nb", new TextArea("a\nb").insert(Position.get(0, 0), "c").getText());
      assertEquals("ac\nb", new TextArea("a\nb").insert(Position.get(0, 1), "c").getText());
      assertEquals("a c\nb", new TextArea("a\nb").insert(Position.get(0, 2), "c").getText());
      assertEquals("a  c\nb", new TextArea("a\nb").insert(Position.get(0, 3), "c").getText());
      assertEquals("a\ncb", new TextArea("a\nb").insert(Position.get(1, 0), "c").getText());
      assertEquals("a\nbc", new TextArea("a\nb").insert(Position.get(1, 1), "c").getText());
      assertEquals("a\nb c", new TextArea("a\nb").insert(Position.get(1, 2), "c").getText());
   }

   public void testLength()
   {
      TextArea area = new TextArea("a\nb");
      assertEquals(Position.get(1, 1), area.length());
   }

   public void testPosition()
   {
      TextArea b = new TextArea("a\nb");
      try
      {
         b.position(-1);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
      assertEquals(Position.get(0, 0), b.position(0));
      assertEquals(Position.get(0, 1), b.position(1));
      assertEquals(Position.get(1, 0), b.position(2));
      assertEquals(Position.get(1, 1), b.position(3));
      try
      {
         b.position(4);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testOffset()
   {
      TextArea b = new TextArea("a\nb");
      try
      {
         b.offset(Position.get(0, -1));
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
      try
      {
         b.offset(Position.get(-1, 0));
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
      assertEquals(0, b.offset(Position.get(0, 0)));
      assertEquals(1, b.offset(Position.get(0, 1)));
      assertEquals(1, b.offset(Position.get(0, 2)));
      assertEquals(2, b.offset(Position.get(1, 0)));
      assertEquals(3, b.offset(Position.get(1, 1)));
      try
      {
         b.offset(Position.get(1, 2));
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testClip()
   {
      TextArea b = new TextArea("a\nb");
      assertEquals("", b.clip(Position.get(0, 0), Position.get(0 ,0)));
      assertEquals("a", b.clip(Position.get(0, 0), Position.get(0 ,1)));
      assertEquals("a", b.clip(Position.get(0, 0), Position.get(0 ,2)));
      assertEquals("a\n", b.clip(Position.get(0, 0), Position.get(1 ,0)));
      assertEquals("a\nb", b.clip(Position.get(0, 0), Position.get(1 ,1)));
      assertEquals("\nb", b.clip(Position.get(0, 1), Position.get(1 ,1)));
      assertEquals("\nb", b.clip(Position.get(0, 2), Position.get(1 ,1)));
      assertEquals("b", b.clip(Position.get(1, 0), Position.get(1 ,1)));
      assertEquals("", b.clip(Position.get(1, 1), Position.get(1 ,1)));

      //
      try
      {
         b.clip(Position.get(-1, 0), Position.get(1 ,1));
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
      try
      {
         b.clip(Position.get(0, 0), Position.get(1 ,2));
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }
}