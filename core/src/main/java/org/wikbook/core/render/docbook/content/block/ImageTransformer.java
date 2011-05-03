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

package org.wikbook.core.render.docbook.content.block;

import org.wikbook.core.model.content.Image;
import org.wikbook.core.model.content.block.ImageElement;
import org.wikbook.core.render.docbook.content.AbstractImageTransformer;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ImageTransformer extends AbstractImageTransformer<ImageElement>
{

   @Override
   protected Image getImage(ImageElement element)
   {
      return element.getImage();
   }

   @Override
   protected ElementEmitter getWrapper(XMLEmitter emitter, ImageElement element, Image img)
   {
      String title = img.getParameters().get("title");
      if (title != null)
      {
         ElementEmitter figureXML = emitter.element("figure");
         figureXML.element("title").content(title);
         return figureXML.element("mediaobject");
      }
      else
      {
         return emitter.element("mediaobject");
      }
   }
}
