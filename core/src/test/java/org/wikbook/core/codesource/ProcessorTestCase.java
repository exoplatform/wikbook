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

package org.wikbook.core.codesource;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ProcessorTestCase extends TestCase
{

   public void testSection()
   {
      TestCodeContext ctx = new TestCodeContext();
      CodeProcessor processor = new CodeProcessor();
      processor.parse("abc", ctx);
      assertEquals("abc", ctx.getText());
   }

   public void testEraseAnchor1()
   {
      TestCodeContext ctx = new TestCodeContext();
      CodeProcessor processor = new CodeProcessor();
      processor.parse("a// <1>\nb", ctx);
      assertEquals("a// <1>\nb", ctx.getText());
   }

   public void testEraseAnchor2()
   {
      TestCodeContext ctx = new TestCodeContext();
      CodeProcessor processor = new CodeProcessor();
      processor.parse("a// <1> foo\nb", ctx);
      assertEquals("a// <1>\nb", ctx.getText());
   }

   public void testInclude()
   {
      TestCodeContext ctx = new TestCodeContext();
      CodeProcessor processor = new CodeProcessor();
      processor.parse("{@include org.wikbook.core.codesource.Bar#m() {1} }", ctx);
      assertEquals("      int a = 0;\n      int c = 0;\n", ctx.getText());
   }

   public void testInclude2()
   {
      TestCodeContext ctx = new TestCodeContext();
      CodeProcessor processor = new CodeProcessor();
      processor.parse("//\n{@include org.wikbook.core.codesource.Bar#m() {1} }", ctx);
      assertEquals("//\n      int a = 0;\n      int c = 0;\n", ctx.getText());
   }

   public void testIncludeMultiple()
   {
      TestCodeContext ctx = new TestCodeContext();
      CodeProcessor processor = new CodeProcessor();
      processor.parse("{@include org.wikbook.core.codesource.Bar#m() {1,2} }", ctx);
      assertEquals("      int a = 0;\n      int b = 0;\n      int c = 0;\n", ctx.getText());
   }
}
