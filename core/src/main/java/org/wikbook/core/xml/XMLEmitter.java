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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class XMLEmitter<N extends Node> extends Emitter<N>
{
   public XMLEmitter(N node)
   {
      super(node);
   }

   public final void comment(String data)
   {
      if (data == null)
      {
         throw new NullPointerException();
      }
      node.appendChild(node.getOwnerDocument().createTextNode(data));
   }

   public abstract ElementEmitter element(String qName);

   public void content(String data)
   {
      content(data, false);
   }

   public abstract void content(String data, boolean cdata);

   public abstract void append(Element elt);
}
