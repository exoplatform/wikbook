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
import org.wikbook.core.render.docbook.ElementWriter;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TableWriter extends ElementWriter<TableElement>
{
   @Override
   public void write(TableElement element, XMLEmitter emitter)
   {
      XMLEmitter tableXML;

      //
      if (element.getTitle() != null)
      {
         tableXML = emitter.element("table");
         tableXML.element("title").content(element.getTitle());
      }
      else
      {
         tableXML = emitter.element("informaltable");
      }

      // Get column count
      int columnCount = 0;
      for (TableRowElement row : element.getStructure())
      {
         columnCount = Math.max(columnCount, row.getCells().getSize());
      }

      // Determine potential header
      LinkedList<TableRowElement> head = new LinkedList<TableRowElement>();
      for (Iterator<TableRowElement> i = element.getStructure().iterator(); i.hasNext();)
      {
         TableRowElement row = i.next();
         if (row.isHead())
         {
            head.addLast(row);
            i.remove();
         }
         else
         {
            break;
         }
      }

      // Determine potential footer
      LinkedList<TableRowElement> foot = new LinkedList<TableRowElement>();
      for (Iterator<TableRowElement> i = element.getStructure().reverseIterator(); i.hasNext();)
      {
         TableRowElement row = i.next();
         if (row.isHead())
         {
            foot.addFirst(row);
            i.remove();
         }
         else
         {
            break;
         }
      }

      //
      LinkedList<TableRowElement> body = new LinkedList<TableRowElement>();
      for (TableRowElement row : element.getStructure())
      {
         body.add(row);
      }

      //
      ElementEmitter tgroup = tableXML.element("tgroup").withAttribute("cols", "" + columnCount);
      for (LinkedList<TableRowElement> a : Arrays.asList(head, body, foot))
      {
         if (!a.isEmpty())
         {
            ElementEmitter elementXML;
            if (a == head)
            {
               elementXML = tgroup.element("thead");
            }
            else if (a == body)
            {
               elementXML = tgroup.element("tbody");
            }
            else
            {
               elementXML = tgroup.element("tfoot");
            }
            for (TableRowElement row : a)
            {
               ElementEmitter rowXML = elementXML.element("row");
               if (row.getVAlign() != null)
               {
                  rowXML.withAttribute("valign", row.getVAlign().toString().toLowerCase());
               }
               for (TableCellElement cell : row.getCells())
               {
                  ElementEmitter entryXML = rowXML.element("entry");
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
