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

package org.wikbook.core.render.docbook.content.block.table;

import org.wikbook.core.model.content.block.table.TableCellElement;
import org.wikbook.core.model.content.block.table.TableElement;
import org.wikbook.core.model.content.block.table.TableRowElement;
import org.wikbook.core.render.docbook.ElementTransformer;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;

import java.util.Arrays;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TableTransformer extends ElementTransformer<TableElement>
{
   @Override
   public void write(TableElement element, XMLEmitter emitter)
   {
      XMLEmitter tableXML;

      //
      if (element.getTitle() != null)
      {
         tableXML = emitter.element("table");
         tableXML.element("caption").content(element.getTitle());
      }
      else
      {
         tableXML = emitter.element("informaltable");
      }

      // Get column count
      int maxColumn = element.getColumnMaxSize();

      // Determine potential header
      Iterable<TableRowElement> header = element.getHeaders();

      // Determine potential footer
      Iterable<TableRowElement> footer = element.getFooters();

      //
      Iterable<TableRowElement> body = element.getBody();

      // for now we dont use cols anymore
      // ElementEmitter tgroup = tableXML.element("tgroup").withAttribute("cols", "" + maxColumn);

     //
      ElementEmitter tgroup = (ElementEmitter)tableXML;
      for (Iterable<TableRowElement> a : Arrays.asList(header, body, footer))
      {
         if (a.iterator().hasNext())
         {
            ElementEmitter elementXML;
            boolean head;
            if (a == header)
            {
               elementXML = tgroup.element("thead");
               head = true;
            }
            else if (a == body)
            {
               elementXML = tgroup.element("tbody");
               head = false;
            }
            else
            {
               elementXML = tgroup.element("tfoot");
               head = false;
            }
            for (TableRowElement row : a)
            {
               ElementEmitter rowXML = elementXML.element("tr");
               if (row.getVAlign() != null)
               {
                  rowXML.withAttribute("valign", row.getVAlign().toString().toLowerCase());
               }
               for (TableCellElement cell : row.getCells())
               {
                  ElementEmitter entryXML = rowXML.element(head ? "th" : "td");
                  if (cell.getAlign() != null)
                  {
                     entryXML.withAttribute("align", cell.getAlign().toString().toLowerCase());
                  }
                  if (cell.getVAlign() != null)
                  {
                     entryXML.withAttribute("valign", cell.getVAlign().toString().toLowerCase());
                  }
                  getWriter(cell).write(cell, entryXML);
               }
            }
         }
      }
   }
}
