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

package org.wikbook.core.model.structural;

import org.wikbook.core.model.AnyElementContainer;
import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.content.ContentElementContainer;
import org.wikbook.core.model.content.ContentElement;
import org.wikbook.core.model.content.inline.AnchorElement;
import org.wikbook.core.model.content.inline.InlineElement;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ComponentElement extends StructuralElement
{

   /** . */
   private boolean doTitle;

   /** . */
   protected AnyElementContainer<InlineElement> title;

   /** . */
   protected ContentElementContainer content = null;

   /** . */
   protected AnyElementContainer<ComponentElement> components = null;

   /** . */
   protected String id;

   /** . */
   protected int level = 0;

   public String getId()
   {
      return id;
   }

   public int getLevel()
   {
      return level;
   }

   public AnyElementContainer<InlineElement> getTitle()
   {
      return title;
   }

   public AnyElementContainer<ComponentElement> getComponents()
   {
      return components;
   }

   public ContentElementContainer getContent()
   {
      return content;
   }

   public void beginTitle()
   {
      doTitle = true;
   }

   public void endTitle()
   {
      doTitle = false;
   }

   @Override
   public boolean append(DocbookElement elt)
   {
      if (doTitle)
      {
         if (title == null)
         {
            title = new AnyElementContainer<InlineElement>(InlineElement.class);
         }
         if (elt instanceof AnchorElement)
         {
            id = ((AnchorElement)elt).getId();
            return true;
         }
         else
         {
            return title.append(elt);
         }
      }
      else if (elt instanceof ContentElement)
      {
         if (components != null)
         {
            throw new AssertionError();
         }
         if (content == null)
         {
            content = new ContentElementContainer();
         }
         return content.append(elt);
      }
      else if (elt instanceof ComponentElement)
      {
         if (components == null)
         {
            components = new AnyElementContainer<ComponentElement>(ComponentElement.class);
         }
         ComponentElement componentElt = (ComponentElement)elt;
         componentElt.level = level + 1;
         return components.append(elt);
      }
      else
      {
         return false;
      }
   }
}
