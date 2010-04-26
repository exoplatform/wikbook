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

package org.wikbook.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class DocumentEmitter extends XMLEmitter<Document>
{

   /** . */
   private ElementEmitter documentEmitter;

   public DocumentEmitter(Document doc)
   {
      super(doc);

      //
      this.documentEmitter = null;
   }

   @Override
   public ElementEmitter element(String qName)
   {
      return documentElement(qName);
   }

   public ElementEmitter documentElement(String qName)
   {
      if (documentEmitter != null)
      {
         throw new IllegalStateException();
      }
      if (qName == null)
      {
         throw new NullPointerException();
      }
      documentEmitter = emitChild(qName);
      return documentEmitter;
   }

   @Override
   public void content(String data, boolean cdata)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public void append(Element elt)
   {
      throw new UnsupportedOperationException();
   }
}
