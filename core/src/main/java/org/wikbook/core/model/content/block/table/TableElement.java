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

package org.wikbook.core.model.content.block.table;

import org.wikbook.core.model.ElementContainer;
import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.content.block.BlockElement;

import java.util.Iterator;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TableElement extends BlockElement
{

   /** . */
   private ElementContainer<TableRowElement> structure;

   /** . */
   private final String title;

   public TableElement(String title)
   {
      this.structure = new ElementContainer<TableRowElement>(TableRowElement.class);
      this.title = title;
   }

   /**
    * Returns the maximum size among all the rows of the table.
    *
    * @return the maximum column size
    */
   public int getColumnMaxSize()
   {
      int columnCount = 0;

      //
      for (TableRowElement row : structure)
      {
         columnCount = Math.max(columnCount, row.getCells().getSize());
      }

      //
      return columnCount;
   }

   /**
    * Returns the index of the body, this value is the index of the first row that does not return true when the
    * method {@link TableRowElement#isHead()} is invoked starting from the first row and incremeting this row index.
    *
    * @return the body index
    */
   public int getBodyIndex()
   {
      int bodyIndex = 0;
      for (TableRowElement row : structure)
      {
         if (row.isHead())
         {
            bodyIndex++;
         }
         else
         {
            break;
         }
      }
      return bodyIndex;
   }

   /**
    * Returns the index of the footer, this value is the index of the first row that does not return true when the
    * method {@link TableRowElement#isHead()} is invoked starting from the last row of the table and by decrementing
    * this row index.
    *
    * @return the footer index
    */
   public int getFooterIndex()
   {
      int bodyIndex = getBodyIndex();
      int footerIndex = getRowSize();
      for (Iterator<TableRowElement> i = structure.reverseIterator(); i.hasNext();)
      {
         TableRowElement row = i.next();
         if (footerIndex > bodyIndex && row.isHead())
         {
            footerIndex--;
         }
         else
         {
            break;
         }
      }
      return footerIndex;
   }

   /**
    * Returns the size of the table, i.e the number of rows.
    *
    * @return the row size
    */
   public int getRowSize()
   {
      return structure.getSize();
   }

   public Iterable<TableRowElement> getHeaders()
   {
      return structure.iterator(0, getBodyIndex());
   }

   public Iterable<TableRowElement> getBody()
   {
      return structure.iterator(getBodyIndex(), getFooterIndex());
   }

   public Iterable<TableRowElement> getFooters()
   {
      return structure.iterator(getFooterIndex(), getRowSize());
   }

   public String getTitle()
   {
      return title;
   }

   @Override
   public boolean append(DocbookElement elt)
   {
      return structure.append(elt);
   }
}
