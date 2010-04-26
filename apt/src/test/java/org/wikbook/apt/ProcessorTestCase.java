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

package org.wikbook.apt;

import junit.framework.TestCase;
import org.wikbook.apt.model.Catalog;

import java.io.ObjectInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ProcessorTestCase extends TestCase
{

   public void testToWrite() throws Exception {

      Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("catalog.ser");
      assertTrue(urls.hasMoreElements());
      URL url = urls.nextElement();
      assertFalse(urls.hasMoreElements());
      ObjectInputStream ois = new ObjectInputStream(url.openStream());
      Catalog catalog = (Catalog)ois.readObject();
      assertEquals(new HashSet<String>(Arrays.asList("foo", "ctor", "juu", "bar", "org.wikbook.apt.Foo")), catalog.getIds());
      
   }
}
