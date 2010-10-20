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

import org.wikbook.core.VAlign;
import org.wikbook.core.model.AnyElementContainer;
import org.wikbook.core.model.DocbookElement;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TableRowElement extends DocbookElement
{

   /** . */
   final AnyElementContainer<TableCellElement> cells;

   /** . */
   private final VAlign valign;

   public TableRowElement(VAlign valign)
   {
      this.cells = new AnyElementContainer<TableCellElement>(TableCellElement.class);
      this.valign = valign;
   }

   public AnyElementContainer<TableCellElement> getCells()
   {
      return cells;
   }

   public VAlign getVAlign()
   {
      return valign;
   }

   public boolean isHead()
   {
      for (TableCellElement cell : cells)
      {
         if (!cell.head)
         {
            return false;
         }
      }
      return true;
   }

   public boolean append(DocbookElement elt)
   {
      return cells.append(elt);
   }
}
