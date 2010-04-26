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

package org.wikbook.apt.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public final class Catalog implements Serializable
{

   /** . */
   private final HashMap<String, List<Fragment>> fragmentsById;

   public Catalog()
   {
      fragmentsById = new HashMap<String, List<Fragment>>();
   }

   public void merge(Catalog catalog)
   {
      for (Map.Entry<String, List<Fragment>> entry : catalog.fragmentsById.entrySet())
      {
         addFragment(entry.getKey(), entry.getValue());
      }
   }

   public Set<String> getIds()
   {
      return fragmentsById.keySet();
   }

   public void addFragment(String fragmentId, List<Fragment> fragment)
   {
      List<Fragment> fragments = fragmentsById.get(fragmentId);
      if (fragments == null)
      {
         fragments = new ArrayList<Fragment>();
         fragmentsById.put(fragmentId, fragments);
      }
      fragments.addAll(fragment);
   }

   public void addFragment(String fragmentId, Fragment fragment)
   {
      addFragment(fragmentId, Collections.singletonList(fragment));
   }

   public void addFragment(String fragmentId, String content)
   {
      addFragment(fragmentId, new Fragment(null, content));
   }

   public List<Fragment> getFragment(String fragmentId)
   {
      List<Fragment> list = fragmentsById.get(fragmentId);
      return list == null ? Collections.<Fragment>emptyList() : Collections.unmodifiableList(list);
   }

   @Override
   public String toString()
   {
      return "Catalog[" + fragmentsById + "]";
   }
}
