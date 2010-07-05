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

package org.wikbook.core.model.content.inline;

import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.ElementList;
import org.wikbook.core.xml.XMLEmitter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class FormatElement extends InlineElement
{

   /** . */
   private ElementList<InlineElement> container = new ElementList<InlineElement>(InlineElement.class);

   /** . */
   private final TextFormat format;

   public FormatElement(TextFormat format)
   {
      this.format = format;
   }

   @Override
   public boolean append(DocbookElement elt)
   {
      if (elt instanceof InlineElement)
      {
         return container.append(elt);
      }
      else
      {
         return false;
      }
   }

   @Override
   public void writeTo(XMLEmitter xml)
   {
      switch (format)
      {
         case BOLD:
            container.writeTo(xml.element("emphasis").withAttribute("role", "bold"));
            break;
         case ITALIC:
            container.writeTo(xml.element("emphasis").withAttribute("role", "italic"));
            break;
         case CODE:
         case MONOSPACE:
            container.writeTo(xml.element("code"));
            break;
         case SUPERSCRIPT:
            container.writeTo(xml.element("superscript"));
            break;
         case SUBSCRIPT:
            container.writeTo(xml.element("subscript"));
            break;
         case UNDERLINE:
            container.writeTo(xml.element("emphasis").withAttribute("role", "underline"));
            break;
         case STRIKE:
            container.writeTo(xml.element("emphasis").withAttribute("role", "strikethrough"));
            break;
         default:
            throw new AssertionError();
      }
   }
}
