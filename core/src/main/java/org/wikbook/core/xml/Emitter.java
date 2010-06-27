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

package org.wikbook.core.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Emitter<N extends Node>
{

   /** . */
   protected final N node;

   public Emitter(N node)
   {
      if (node == null)
      {
         throw new NullPointerException();
      }

      //
      this.node = node;
   }

   protected final ElementEmitter emitChild(String qName)
   {
      Document doc = node instanceof Document ? (Document)node : node.getOwnerDocument();
      Element elt = (Element)node.appendChild(doc.createElement(qName));
      ElementEmitter emitter = new ElementEmitter(this, elt);
      return emitter;
   }

   public final void close()
   {
   }
}
