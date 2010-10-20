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

import org.wikbook.core.Align;
import org.wikbook.core.VAlign;
import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.ElementContainer;
import org.wikbook.core.model.content.ContentElementContainer;
import org.wikbook.core.model.content.inline.InlineElement;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TableCellElement extends DocbookElement
{

   /** . */
   final boolean head;

   /** . */
   private ContentElementContainer content;

   /** . */
   private ElementContainer<InlineElement> inline;

   /** . */
   private final Align align;

   /** . */
   private final VAlign valign;

   public TableCellElement(boolean head, Align align, VAlign valign)
   {
      this.head = head;
      this.align = align;
      this.valign = valign;
   }

   public ElementContainer<InlineElement> getInline()
   {
      return inline;
   }

   public ContentElementContainer getContent()
   {
      return content;
   }

   public Align getAlign()
   {
      return align;
   }

   public VAlign getVAlign()
   {
      return valign;
   }

   public boolean append(DocbookElement elt)
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
               content = new ContentElementContainer();
            }
            return content.append(elt);
         }
         else
         {
            content = new ContentElementContainer();
            for (InlineElement i : inline)
            {
               content.append(i);
            }
            inline = null;
            return content.append(elt);
         }
      }
   }
}
