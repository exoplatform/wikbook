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

package org.wikbook;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class SimpleWikletContext implements WikletContext
{

   /** . */
   private final File base;

   /** . */
   private final Map<String, Person> persons;

   /** . */
   private final Map<String, String> properties;

   public SimpleWikletContext(File base)
   {
      if (base == null)
      {
         throw new NullPointerException("No null base directory accepted");
      }
      this.base = base;
      this.persons = new LinkedHashMap<String, Person>();
      this.properties = new HashMap<String, String>();
   }

   public String getProperty(String propertyName)
   {
      return properties.get(propertyName);
   }

   public void setProperty(String propertyName, String propertyValue)
   {
      if (properties != null)
      {
         properties.put(propertyName, propertyValue);
      }
      else
      {
         properties.remove(propertyName);
      }
   }

   public void removeProperty(String propertyName)
   {
      setProperty(propertyName, null);
   }

   public void addPerson(String authorId, Person author)
   {
      persons.put(authorId, author);
   }

   public List<Person> findPersonsByRole(String roleName)
   {
      ArrayList<Person> ps = new ArrayList<Person>();
      for (Person p : persons.values())
      {
         if (p.hasRole(roleName))
         {
            ps.add(p);
         }
      }
      return ps;
   }

   public List<URL> resolveResources(ResourceType type, String id) throws IOException
   {
      File resolved;
      switch (type)
      {
         case WIKI_SOURCE:
            resolved = new File(base, id);
            if (resolved != null && resolved.isFile())
            {
               return Arrays.asList(resolved.toURI().toURL());
            }
            break;
         case JAVA_SOURCE:
            throw new UnsupportedOperationException();
         case CATALOG:
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("catalog.ser");
            List<URL> streams = new ArrayList<URL>();
            while (urls.hasMoreElements())
            {
               URL url = urls.nextElement();
               streams.add(url);
            }
            return streams;
         default:
            throw new AssertionError();
      }
      return null;
   }

   public URL resolveResource(ResourceType type, String id) throws IOException
   {
      List<URL> urls = resolveResources(type, id);
      return urls.isEmpty() ? null : urls.get(0);
   }
}
