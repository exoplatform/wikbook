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

package org.wikbook.core.model.content.block;

import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.ElementContainer;
import org.wikbook.core.model.content.ContentContainer;
import org.wikbook.core.model.content.inline.InlineElement;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TableElement extends BlockElement
{

   private static class Cell
   {

      /** . */
      private final boolean head;

      /** . */
      private ContentContainer content;

      /** . */
      private ElementContainer<InlineElement> inline;

      private Cell(boolean head)
      {
         this.head = head;
      }

      boolean append(DocbookElement elt)
      {
         if (elt instanceof InlineElement)
         {
            if (content != null)
            {
               return content.append(elt);
            }
            else
            {
               if (inline == null)
               {
                  inline = new ElementContainer<InlineElement>(InlineElement.class);
               }
               return inline.append(elt);
            }
         }
         else
         {
            if (inline == null)
            {
               if (content == null)
               {
                  content = new ContentContainer();
               }
               return content.append(elt);
            }
            else
            {
               content = new ContentContainer();
               for (InlineElement i : inline)
               {
                  content.append(i);
               }
               inline = null;
               return content.append(elt);
            }
         }
      }
      void writeTo(XMLEmitter emitter)
      {
         if (content != null)
         {
            content.writeTo(emitter);
         }
         else if (inline != null)
         {
            inline.writeTo(emitter);
         }
      }

   }

   /** . */
   private LinkedList<LinkedList<Cell>> structure;

   /** . */
   private final String title;

   public TableElement(String title)
   {
      this.structure = new LinkedList<LinkedList<Cell>>();
      this.title = title;
   }

   @Override
   public boolean append(DocbookElement elt)
   {
      return structure.getLast().getLast().append(elt);
   }

   public void doBeginTableRow(Map<String, String> parameters)
   {
      structure.addLast(new LinkedList<Cell>());
   }

   public void doBeginTableHeadCell(Map<String, String> parameters)
   {
      structure.getLast().addLast(new Cell(true));
   }

   public void doEndTableHeadCell(Map<String, String> parameters)
   {
   }

   public void doBeginTableCell(Map<String, String> parameters)
   {
      structure.getLast().addLast(new Cell(false));
   }

   public void doEndTableCell(Map<String, String> parameters)
   {
   }

   public void doEndTableRow(Map<String, String> parameters)
   {
   }

   @Override
   public void writeTo(XMLEmitter xml)
   {
      XMLEmitter tableXML = xml.element("table");

      //
      if (title != null)
      {
         tableXML.element("title").content(title);
      }

      // Get column count
      int columnCount = 0;
      for (LinkedList<Cell> row : structure)
      {
         columnCount = Math.max(columnCount, row.size());
      }

      // Determine potential header
      LinkedList<LinkedList<Cell>> head = new LinkedList<LinkedList<Cell>>();
      for (Iterator<LinkedList<Cell>> i = structure.iterator();i.hasNext();)
      {
         LinkedList<Cell> row = i.next();
         if (isHead(row))
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
      LinkedList<LinkedList<Cell>> foot = new LinkedList<LinkedList<Cell>>();
      for (ListIterator<LinkedList<Cell>> i = structure.listIterator(structure.size());i.hasPrevious();)
      {
         LinkedList<Cell> row = i.previous();
         if (isHead(row))
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
      ElementEmitter tgroup = tableXML.element("tgroup").withAttribute("cols", "" + columnCount);
      for (List<LinkedList<Cell>> a : Arrays.asList(head, structure, foot))
      {
         if (!a.isEmpty())
         {
            ElementEmitter elementXML;
            if (a == head)
            {
               elementXML = tgroup.element("thead");
            }
            else if (a == structure)
            {
               elementXML = tgroup.element("tbody");
            }
            else
            {
               elementXML = tgroup.element("tfoot");
            }
            for (LinkedList<Cell> row : a)
            {
               ElementEmitter rowXML = elementXML.element("row");
               for (Cell cell : row)
               {
                  cell.writeTo(rowXML.element("entry"));
               }
            }
         }
      }
   }

   private boolean isHead(LinkedList<Cell> row)
   {
      for (Cell cell : row)
      {
         if (!cell.head)
         {
            return false;
         }
      }
      return true;
   }
}
