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

package org.wikbook.model;

import org.wikbook.xml.XMLEmitter;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ElementContainer<E extends DocbookElement> implements Iterable<E>
{

   /** . */
   protected final LinkedList<E> elements;

   /** . */
   private final Class<E> elementType;

   public ElementContainer(Class<E> elementType)
   {
      this.elements = new LinkedList<E>();
      this.elementType = elementType;
   }

   public Iterator<E> iterator()
   {
      return elements.iterator();
   }

   public boolean isNotEmpty()
   {
      return !elements.isEmpty();
   }

   public boolean isEmpty()
   {
      return elements.isEmpty();
   }

   public E getLast()
   {
      return elements.getLast();
   }

   public boolean append(DocbookElement elt)
   {
      if (elementType.isInstance(elt))
      {
         elements.add(elementType.cast(elt));
         return true;
      }
      else
      {
         return false;
      }
   }

   public void writeTo(XMLEmitter emitter)
   {
      for (E elt : elements)
      {
         elt.writeTo(emitter);
      }
   }
}