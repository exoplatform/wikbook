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
import org.wikbook.text.Position;

import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class AnchorTestCase extends TestCase
{

   public void testAnchor1()
   {
      CodeSourceBuilder builder = new CodeSourceBuilder();
      TypeSource ts = builder.buildClass(Bar.class.getName());
      assertEquals("public class Bar\n" +
         "{\n" +
         "\n" +
         "\n" +
         "   public void foo()\n" +
         "   {\n" +
         "\n" +
         "      int a = 0;\n" +
         "      int b = 4;\n" +
         "   }\n" +
         "}", ts.getClip());
   }

   public void testAnchorFromType()
   {
      CodeSourceBuilder builder = new CodeSourceBuilder();
      TypeSource ts = builder.buildClass(Bar.class.getName());
      List<Anchor> anchors = ts.getAnchors();
      assertEquals(3, anchors.size());
      assertEquals("0", anchors.get(0).getId());
      assertEquals(Position.get(2, 3), anchors.get(0).getPosition());
      assertEquals("1", anchors.get(1).getId());
      assertEquals(Position.get(6, 6), anchors.get(1).getPosition());
      assertEquals("2", anchors.get(2).getId());
      assertEquals(Position.get(8, 17), anchors.get(2).getPosition());
   }

   public void testAnchorFromMethod()
   {
      CodeSourceBuilder builder = new CodeSourceBuilder();
      TypeSource ts = builder.buildClass(Bar.class.getName());
      MethodSource ms = (MethodSource)ts.findMember("foo()");
      List<Anchor> anchors = ms.getAnchors();
      assertEquals(2, anchors.size());
      assertEquals("1", anchors.get(0).getId());
      assertEquals(Position.get(2, 6), anchors.get(0).getPosition());
      assertEquals("2", anchors.get(1).getId());
      assertEquals(Position.get(4, 17), anchors.get(1).getPosition());
   }
}
