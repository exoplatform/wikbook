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
public class PropertyFilterTestCase extends TestCase
{

   /** . */
   private final Map<String, String> properties = new HashMap<String, String>();

   private String filter(String s)
   {
      return new PropertyFilter()
      {
         @Override
         protected String resolveProperty(String propertyname)
         {
            return properties.get(propertyname);
         }
      }.filter(s);
   }

   @Override
   protected void tearDown() throws Exception
   {
      properties.clear();
   }

   public void testNoProperties()
   {
      assertEquals("", filter(""));
      assertEquals("1", filter("1"));
      assertEquals("$[]", filter("$[]"));
      assertEquals("$[a]", filter("$[a]"));
      assertEquals("1$[a]", filter("1$[a]"));
      assertEquals("$[a]1", filter("$[a]1"));
      assertEquals("1$[a]2", filter("1$[a]2"));
      assertEquals("1$[a]2$[b]", filter("1$[a]2$[b]"));
      assertEquals("1$[a]2$[b]3", filter("1$[a]2$[b]3"));
   }

   public void testResolve1()
   {
      properties.put("a", "_");
      assertEquals("", filter(""));
      assertEquals("1", filter("1"));
      assertEquals("$[]", filter("$[]"));
      assertEquals("_", filter("$[a]"));
      assertEquals("1_", filter("1$[a]"));
      assertEquals("_1", filter("$[a]1"));
      assertEquals("1_2", filter("1$[a]2"));
      assertEquals("1_2$[b]", filter("1$[a]2$[b]"));
      assertEquals("1_2$[b]3", filter("1$[a]2$[b]3"));
   }

   public void testResolve2()
   {
      properties.put("b", "_");
      assertEquals("", filter(""));
      assertEquals("1", filter("1"));
      assertEquals("$[]", filter("$[]"));
      assertEquals("$[a]", filter("$[a]"));
      assertEquals("1$[a]", filter("1$[a]"));
      assertEquals("$[a]1", filter("$[a]1"));
      assertEquals("1$[a]2", filter("1$[a]2"));
      assertEquals("1$[a]2_", filter("1$[a]2$[b]"));
      assertEquals("1$[a]2_3", filter("1$[a]2$[b]3"));
   }

   public void testResolve3()
   {
      properties.put("a", "_");
      properties.put("b", "-");
      assertEquals("", filter(""));
      assertEquals("1", filter("1"));
      assertEquals("$[]", filter("$[]"));
      assertEquals("_", filter("$[a]"));
      assertEquals("1_", filter("1$[a]"));
      assertEquals("_1", filter("$[a]1"));
      assertEquals("1_2", filter("1$[a]2"));
      assertEquals("1_2-", filter("1$[a]2$[b]"));
      assertEquals("1_2-3", filter("1$[a]2$[b]3"));
   }
}
