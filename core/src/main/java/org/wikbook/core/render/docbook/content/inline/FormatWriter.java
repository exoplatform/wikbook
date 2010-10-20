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

package org.wikbook.core.render.docbook.content.inline;

import org.wikbook.core.model.content.inline.FormatElement;
import org.wikbook.core.render.docbook.ElementWriter;
import org.wikbook.core.xml.XMLEmitter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class FormatWriter extends ElementWriter<FormatElement>
{

   @Override
   public void write(FormatElement element, XMLEmitter emitter)
   {
      switch (element.getFormat())
      {
         case BOLD:
            write(element.getContainer(), emitter.element("emphasis").withAttribute("role", "bold"));
            break;
         case ITALIC:
            write(element.getContainer(), emitter.element("emphasis").withAttribute("role", "italic"));
            break;
         case CODE:
         case MONOSPACE:
            write(element.getContainer(), emitter.element("code"));
            break;
         case SUPERSCRIPT:
            write(element.getContainer(), emitter.element("superscript"));
            break;
         case SUBSCRIPT:
            write(element.getContainer(), emitter.element("subscript"));
            break;
         case UNDERLINE:
            write(element.getContainer(), emitter.element("emphasis").withAttribute("role", "underline"));
            break;
         case STRIKE:
            write(element.getContainer(), emitter.element("emphasis").withAttribute("role", "strikethrough"));
            break;
         default:
            throw new AssertionError();
      }
   }
}
