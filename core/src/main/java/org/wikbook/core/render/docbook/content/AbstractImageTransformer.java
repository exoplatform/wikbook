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

package org.wikbook.core.render.docbook.content;

import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.content.Image;
import org.wikbook.core.render.docbook.ElementTransformer;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class AbstractImageTransformer<E extends DocbookElement> extends ElementTransformer<E>
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

   public final void write(E element, XMLEmitter emitter)
   {
      Image img = getImage(element);

      //
      String extension = "";
      int lastDot = img.getName().lastIndexOf('.');
      if (lastDot > -1)
      {
         extension = img.getName().substring(lastDot + 1).toLowerCase();
      }

      //
      String format = formats.get(extension);

      //
      if (format != null)
      {
         ElementEmitter mediaObjectXML = getWrapper(emitter, element, img);

         //
         for (String output : outputs)
         {
            ElementEmitter imageDataXML = mediaObjectXML.element("imageobject").withAttribute("role", output).
               element("imagedata").
               withAttribute("fileref", img.getName()).
               withAttribute("format", format);

            //
            for (Map.Entry<String, String> entry : img.getParameters().entrySet())
            {
               String key = entry.getKey();
               int colonIndex = key.indexOf(":");
               String prefix = colonIndex != -1 ? key.substring(0, colonIndex + 1) : "";
               if (prefix.length() == 0 || prefix.equals(output + ":"))
               {
                  String attrelementName = key.substring(prefix.length());
                  if (imageDataAttributes.contains(attrelementName))
                  {
                     imageDataXML.withAttribute(attrelementName, entry.getValue());
                  }
               }
            }
         }
      }
   }

   protected abstract Image getImage(E element);

   protected abstract ElementEmitter getWrapper(XMLEmitter emitter, E element, Image img);

}
