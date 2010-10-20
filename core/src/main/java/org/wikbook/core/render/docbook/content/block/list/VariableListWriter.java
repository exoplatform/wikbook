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

package org.wikbook.core.render.docbook.content.block.list;

import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.content.block.list.TermElement;
import org.wikbook.core.model.content.block.list.VariableListElement;
import org.wikbook.core.render.docbook.ElementWriter;
import org.wikbook.core.xml.XMLEmitter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class VariableListWriter extends ElementWriter<VariableListElement>
{
   @Override
   public void write(VariableListElement element, XMLEmitter emitter)
   {
      XMLEmitter listXML = emitter.element("variablelist");
      if (element.getTitle() != null)
      {
         listXML.element("title").content(element.getTitle());
      }
      XMLEmitter entryXML = null;
      for (DocbookElement elt : element.getContainer())
      {
         if (elt instanceof TermElement)
         {
            entryXML = listXML.element("varlistentry");
         }
         // Investigate that cast
         ElementWriter<DocbookElement> writer = getWriter(elt.getType());
         writer.write(elt, entryXML);
      }
   }
}
