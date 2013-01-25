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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

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

   public Iterator<E> reverseIterator()
   {
      final ListIterator<E> delegate = elements.listIterator(elements.size());
      return new Iterator<E>()
      {
         public boolean hasNext()
         {
            return delegate.hasPrevious();
         }
         public E next()
         {
            return delegate.previous();
         }
         public void remove()
         {
            delegate.remove();
         }
      };
   }

   public Iterable<E> iterator(int from, int to)
   {
      return new ArrayList<E>(elements).subList(from, to);
   }

   public Class<E> getElementType()
   {
      return elementType;
   }

   public boolean isNotEmpty()
   {
      return !elements.isEmpty();
   }

   public boolean isEmpty()
   {
      return elements.isEmpty();
   }

   public E peekFirst()
   {
      return elements.peekFirst();
   }

   public E getLast()
   {
      return elements.getLast();
   }

   public int getSize()
   {
      return elements.size();
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
}