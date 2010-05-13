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

import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.ElementContainer;
import org.wikbook.core.model.content.ContentContainer;
import org.wikbook.core.model.content.ContentElement;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class BookElement extends StructuralElement
{

   /** . */
   private final ElementContainer<ComponentElement> chapters = new ElementContainer<ComponentElement>(ComponentElement.class);

   /** . */
   private ContentContainer preface = new ContentContainer();

   /** . */
   private String prefaceTitle;

   /** . */
   private boolean omitRootNode;

   public BookElement()
   {
      this.omitRootNode = false;
      this.prefaceTitle = "Preface";
   }

   public String getPrefaceTitle()
   {
      return prefaceTitle;
   }

   public void setPrefaceTitle(String prefaceTitle)
   {
      this.prefaceTitle = prefaceTitle;
   }

   public boolean getOmitRootNode()
   {
      return omitRootNode;
   }

   public void setOmitRootNode(boolean omitRootNode)
   {
      this.omitRootNode = omitRootNode;
   }

   @Override
   public boolean append(DocbookElement elt)
   {
      if (elt instanceof ContentElement)
      {
         return preface.append(elt);
      }
      else if (elt instanceof ComponentElement)
      {
         return chapters.append(elt);
      }
      return false;
   }

   @Override
   public void writeTo(XMLEmitter xml)
   {
      if (!omitRootNode)
      {
         xml = xml.element("book");
      }
      if (preface.isNotEmpty())
      {
         ElementEmitter prefaceXML = xml.element("preface");
         if (prefaceTitle != null)
         {
            prefaceXML.element("title").content("Preface");
         }
         preface.writeTo(prefaceXML);
      }
      chapters.writeTo(xml);
   }
}
