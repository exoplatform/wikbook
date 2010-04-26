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

package org.wikbook.model.content;

import org.wikbook.model.DocbookElement;
import org.wikbook.model.ElementContainer;
import org.wikbook.model.content.block.BlockElement;
import org.wikbook.xml.ElementEmitter;
import org.wikbook.xml.XMLEmitter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ContentContainer
{

   /** . */
   private final ElementContainer<ContentElement> container = new ElementContainer<ContentElement>(ContentElement.class);

   public ContentContainer()
   {
   }

   public boolean isNotEmpty()
   {
      return container.isNotEmpty();
   }

   public boolean isEmpty()
   {
      return container.isEmpty();
   }

   public boolean append(DocbookElement elt)
   {
      return container.append(elt);
   }

   public void writeTo(XMLEmitter emitter)
   {
      ElementEmitter paraXML = null;
      for (ContentElement e : container)
      {
         if (e instanceof BlockElement)
         {
            if (paraXML != null)
            {
               paraXML = null;
            }
            e.writeTo(emitter);
         }
         else
         {
            if (paraXML == null)
            {
               paraXML = emitter.element("para");
            }
            e.writeTo(paraXML);
         }
      }
   }
}