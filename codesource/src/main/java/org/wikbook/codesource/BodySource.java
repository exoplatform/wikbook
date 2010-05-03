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

package org.wikbook.codesource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class BodySource
{

   /** . */
   private final Clip clip;

   /** . */
   private final String javaDoc;

   protected BodySource(Clip clip, String javaDoc)
   {
      if (clip == null)
      {
         throw new NullPointerException();
      }

      //
      this.clip = clip;
      this.javaDoc = javaDoc;
   }

   public final String getClip()
   {
      return getType().source.clip(clip);
   }

   public final String getJavaDoc()
   {
      return javaDoc;
   }

   public final List<Anchor> getAnchors()
   {
      ArrayList<Anchor> anchors = new ArrayList<Anchor>();
      TypeSource type = getType();
      for (Anchor anchor : type.anchors)
      {
         Coordinate relative = clip.getRelative(anchor.getPosition());
         if (relative != null)
         {
            anchors.add(new Anchor(anchor.getId(), relative));
         }
      }
      return anchors;
   }

   protected abstract TypeSource getType();
}
