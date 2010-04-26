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

package org.wikbook.model.structural;

import org.wikbook.model.DocbookElement;
import org.wikbook.model.ElementContainer;
import org.wikbook.model.content.ContentContainer;
import org.wikbook.model.content.ContentElement;
import org.wikbook.model.content.inline.AnchorElement;
import org.wikbook.model.content.inline.InlineElement;
import org.wikbook.xml.ElementEmitter;
import org.wikbook.xml.XMLEmitter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ComponentElement extends StructuralElement
{

   /** . */
   private boolean doTitle;

   /** . */
   protected ElementContainer<InlineElement> title;

   /** . */
   protected ContentContainer content = null;

   /** . */
   protected ElementContainer<ComponentElement> components = null;

   /** . */
   protected String id;

   /** . */
   protected int level = 0;

   public void beginTitle()
   {
      doTitle = true;
   }

   public void endTitle()
   {
      doTitle =  false;
   }

   @Override
   public boolean append(DocbookElement elt)
   {
      if (doTitle)
      {
         if (title == null)
         {
            title = new ElementContainer<InlineElement>(InlineElement.class);
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
            content = new ContentContainer();
         }
         return content.append(elt);
      }
      else if (elt instanceof ComponentElement)
      {
         if (components == null)
         {
            components = new ElementContainer<ComponentElement>(ComponentElement.class);
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

   @Override
   public void writeTo(XMLEmitter xml)
   {
      ElementEmitter chapterXML = xml.element(level == 0 ? "chapter" : "section");
      if (id != null)
      {
         chapterXML.withAttribute("id", id);
      }
      if (title != null)
      {
         title.writeTo(chapterXML.element("title"));
      }
      if (content != null)
      {
         content.writeTo(chapterXML);
      }
      if (components != null)
      {
         components.writeTo(chapterXML);
      }
   }
}
