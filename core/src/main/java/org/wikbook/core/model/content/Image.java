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
package org.wikbook.core.model.content;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class Image
{

   /** . */
   private static final Map<String, String> formats = new HashMap<String, String>();

   /** . */
   private static final List<String> outputs = Collections.unmodifiableList(Arrays.asList("fo", "html"));

   /** . */
   private static final Set<String> imageDataAttributes = new HashSet<String>();

   static
   {
      formats.put("png", "PNG");
      formats.put("gif", "GIF");
      formats.put("jpg", "JPG");
      formats.put("jpeg", "JPG");
      formats.put("tiff", "TIFF");

      //
      imageDataAttributes.add("align");
      imageDataAttributes.add("valign");
      imageDataAttributes.add("width");
      imageDataAttributes.add("depth");
      imageDataAttributes.add("scale");
      imageDataAttributes.add("scalefit");
      imageDataAttributes.add("contentwidth");
      imageDataAttributes.add("contentdepth");
   }

   /** . */
   private final String name;

   /** . */
   private final Map<String, String> parameters;

   public Image(String name, Map<String, String> parameters)
   {
      this.name = name;
      this.parameters = parameters;
   }

   public String getName()
   {
      return name;
   }

   public Map<String, String> getParameters()
   {
      return parameters;
   }

   @Override
   public String toString()
   {
      return "Image[name=" + name + ",parameters=" + parameters + "]";
   }
}
