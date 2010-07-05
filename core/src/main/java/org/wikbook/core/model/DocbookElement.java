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

package org.wikbook.core.model;

import org.wikbook.core.model.content.inline.InlineElement;
import org.wikbook.core.xml.XMLEmitter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class DocbookElement
{

   /** The context. */
   DocbookElementContext context;

   /**
    * The current child element that is pushed onto the stack. The chained list created by following the
    * {@code currentChildElt} references from the root maintain the stack structure. The structure is maintained
    * by this class only.
    */
   private DocbookElement currentChildElt;

   protected DocbookElement(DocbookElementContext context)
   {
      if (context == null)
      {
         throw new NullPointerException();
      }

      //
      this.context = context;
   }

   protected DocbookElement()
   {
      this.context = null;
   }

   protected final DocbookElementContext assertContextualized()
   {
      if (context == null)
      {
         throw new IllegalStateException("No context");
      }
      return context;
   }

   public final void close()
   {
      DocbookElement currentElt = currentChildElt;
      currentChildElt = null;

      //
      if (currentElt != null)
      {
         if (!append(currentElt))
         {
            throw new AssertionError("Could not append element " + currentElt.getClass().getName() + " to element " + getClass().getName());
         }

         //
         currentElt.close();
      }
   }

   private DocbookElement current(boolean remove)
   {
      DocbookElement currentElt = currentChildElt;

      //
      if (currentElt != null)
      {
         DocbookElement nextElt = currentElt.current(remove);

         //
         if (nextElt == null)
         {
            if (remove)
            {
               currentChildElt = null;
               currentElt.context = null;
            }
         }
         else
         {
            currentElt = nextElt;
         }
      }

      //
      return currentElt;
   }

   public final DocbookElement peek()
   {
      return current(false);
   }

   public final DocbookElement pop()
   {
      return current(true);
   }

   public final <E extends DocbookElement> E push(E elt)
   {
      if (context == null)
      {
         throw new IllegalStateException("No context");
      }
      if (elt.context != null)
      {
         throw new IllegalArgumentException("Already has a parent");
      }
      DocbookElement currentElt = currentChildElt;
      if (currentElt == null)
      {
         currentChildElt = elt;
         elt.context = context;
         return elt;
      }
      else
      {
         return currentElt.push(elt);
      }
   }

   public final void merge()
   {
      merge(pop());
   }

   public final <E extends DocbookElement> E merge(E elt)
   {
      if (elt == null)
      {
         throw new NullPointerException();
      }
      if (context == null)
      {
         throw new IllegalStateException("No context");
      }
      if (elt.context != null)
      {
         throw new IllegalArgumentException("Already has a parent");
      }

      //
      DocbookElement appendedElt = peek();

      //
      if (appendedElt == null)
      {
         appendedElt = this;
      }

      //
      if (!appendedElt.append(elt))
      {
         throw new AssertionError("Could not append element " + elt.getClass().getName() + " to element " + getClass().getName());
      }

      //
      if (elt instanceof InlineElement)
      {
         appendedElt.inlineAppended = true;
      }

      //
      elt.context = context;

      //
      return elt;
   }

   private boolean inlineAppended = false;

   public boolean hasInlineAppended()
   {
      return inlineAppended;
   }

   protected boolean append(DocbookElement elt)
   {
      return false;
   }

   public void writeTo(XMLEmitter xml)
   {
      throw new UnsupportedOperationException("Class " + getClass().getName() + " does not implement writeTo");
   }
}
