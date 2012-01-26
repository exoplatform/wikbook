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

package org.wikbook.core.render.docbook.structural;

import org.wikbook.core.model.structural.BookElement;
import org.wikbook.core.render.docbook.ElementTransformer;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class BookTransformer extends ElementTransformer<BookElement>
{

   @Override
   public void write(BookElement element, XMLEmitter emitter)
   {
      if (!element.getOmitRootNode())
      {
         ElementEmitter bookEmitter = emitter.element("book");
         String id = element.getId();
         if (id != null)
         {
            bookEmitter.withAttribute("id", id);
         }
         emitter = bookEmitter;
      }

      //
      if (element.getBeforeBodyXML() != null) {
         emitter.append(element.getBeforeBodyXML());
      }

      //
      if (element.getPreface() != null)
      {
         getWriter(element.getPreface()).write(element.getPreface(), emitter.element("preface"));
      }

      //
      write(element.getChapters(), false, emitter);

      //
      if (element.getAfterBodyXML() != null) {
         emitter.append(element.getAfterBodyXML());
      }
   }
}
