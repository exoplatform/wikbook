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

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class PatternFilterTestCase extends TestCase
{

   /** . */
   private final Map<String, String> properties = new HashMap<String, String>();

   private String filterProperties(String s)
   {
      return new PatternFilter.Properties()
      {
         @Override
         protected String resolveProperty(String propertyname)
         {
            return properties.get(propertyname);
         }
      }.filter(s);
   }

   private String filterEscape(String s)
   {
      return new PatternFilter.Escape("<", ">").filter(s);
   }

   @Override
   protected void tearDown() throws Exception
   {
      properties.clear();
   }

   public void testEscape()
   {
      assertEquals("", filterEscape(""));
      assertEquals("a", filterEscape("a"));
      assertEquals("\\", filterEscape("\\"));
      assertEquals("<a>", filterEscape("\\a"));
      assertEquals("\\", filterEscape("\\\\"));
      assertEquals("\\\\", filterEscape("\\\\\\\\"));
   }

   public void testNoProperties()
   {
      assertEquals("", filterProperties(""));
      assertEquals("1", filterProperties("1"));
      assertEquals("$[]", filterProperties("$[]"));
      assertEquals("$[a]", filterProperties("$[a]"));
      assertEquals("1$[a]", filterProperties("1$[a]"));
      assertEquals("$[a]1", filterProperties("$[a]1"));
      assertEquals("1$[a]2", filterProperties("1$[a]2"));
      assertEquals("1$[a]2$[b]", filterProperties("1$[a]2$[b]"));
      assertEquals("1$[a]2$[b]3", filterProperties("1$[a]2$[b]3"));
   }

   public void testResolveProperties1()
   {
      properties.put("a", "_");
      assertEquals("", filterProperties(""));
      assertEquals("1", filterProperties("1"));
      assertEquals("$[]", filterProperties("$[]"));
      assertEquals("_", filterProperties("$[a]"));
      assertEquals("1_", filterProperties("1$[a]"));
      assertEquals("_1", filterProperties("$[a]1"));
      assertEquals("1_2", filterProperties("1$[a]2"));
      assertEquals("1_2$[b]", filterProperties("1$[a]2$[b]"));
      assertEquals("1_2$[b]3", filterProperties("1$[a]2$[b]3"));
   }

   public void testResolveProperties2()
   {
      properties.put("b", "_");
      assertEquals("", filterProperties(""));
      assertEquals("1", filterProperties("1"));
      assertEquals("$[]", filterProperties("$[]"));
      assertEquals("$[a]", filterProperties("$[a]"));
      assertEquals("1$[a]", filterProperties("1$[a]"));
      assertEquals("$[a]1", filterProperties("$[a]1"));
      assertEquals("1$[a]2", filterProperties("1$[a]2"));
      assertEquals("1$[a]2_", filterProperties("1$[a]2$[b]"));
      assertEquals("1$[a]2_3", filterProperties("1$[a]2$[b]3"));
   }

   public void testResolveProperties3()
   {
      properties.put("a", "_");
      properties.put("b", "-");
      assertEquals("", filterProperties(""));
      assertEquals("1", filterProperties("1"));
      assertEquals("$[]", filterProperties("$[]"));
      assertEquals("_", filterProperties("$[a]"));
      assertEquals("1_", filterProperties("1$[a]"));
      assertEquals("_1", filterProperties("$[a]1"));
      assertEquals("1_2", filterProperties("1$[a]2"));
      assertEquals("1_2-", filterProperties("1$[a]2$[b]"));
      assertEquals("1_2-3", filterProperties("1$[a]2$[b]3"));
   }
}
