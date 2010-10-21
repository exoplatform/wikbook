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

import org.wikbook.core.model.content.block.ListElement;
import org.wikbook.core.model.content.block.ListKind;
import org.wikbook.core.render.docbook.ElementTransformer;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;

import java.util.EnumMap;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ListTransformer extends ElementTransformer<ListElement>
{

   /** . */
   private static final EnumMap<ListKind, String> ListKindMap = new EnumMap<ListKind, String>(ListKind.class);

   /** . */
   private static final EnumMap<ListKind, String> styleMap = new EnumMap<ListKind, String>(ListKind.class);

   static
   {
      ListKindMap.put(ListKind.BULLETED, "itemizedlist");
      ListKindMap.put(ListKind.NUMBERED, "orderedlist");
      styleMap.put(ListKind.BULLETED, "mark");
      styleMap.put(ListKind.NUMBERED, "numeration");
   }


   @Override
   public void write(ListElement element, XMLEmitter emitter)
   {
      ElementEmitter listXML = emitter.element(ListKindMap.get(element.getKind()));

      //
      if (element.getStyle() != null)
      {
         listXML.withAttribute(styleMap.get(element.getKind()), element.getStyle());
      }

      //
      write(element.getItems(), false, listXML);
   }
}
