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

package org.wikbook.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
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
   private final Map<String, String> properties;

   /** . */
   private boolean highlightCode;

   /** . */
   private boolean emitDoctype;

   public SimpleWikletContext(File base)
   {
      if (base == null)
      {
         throw new NullPointerException("No null base directory accepted");
      }
      this.base = base;
      this.properties = new HashMap<String, String>();
      this.emitDoctype = true;
      this.highlightCode = true;
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

   public List<URL> resolveResources(ResourceType type, String id) throws IOException
   {
      if (id.length() > 0)
      {
         File resolved;
         switch (type)
         {
            case WIKI:
               resolved = new File(base, id);
               if (resolved != null && resolved.isFile())
               {
                  return Arrays.asList(resolved.toURI().toURL());
               }
               break;
            case XML:
               if (id.startsWith("/"))
               {
                  id = id.substring(1);
               }
            case JAVA:
               Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(id);
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
      }
      return Collections.emptyList();
   }

   public URL resolveResource(ResourceType type, String id) throws IOException
   {
      List<URL> urls = resolveResources(type, id);
      return urls.isEmpty() ? null : urls.get(0);
   }

   public boolean getHighlightCode()
   {
      return highlightCode;
   }

   public void setHighlightCode(boolean highlightCode)
   {
      this.highlightCode = highlightCode;
   }
}
