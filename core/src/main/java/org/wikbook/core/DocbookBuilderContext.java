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

package org.wikbook.core;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class DocbookBuilderContext
{

   public abstract boolean getHighlightCode();

   public abstract ValidationMode getValidationMode();

   public abstract void log(String msg);

   public void onValidationError(String msg)
   {
      if (getValidationMode() == ValidationMode.STRICT)
      {
         throw new WikbookValidationException(msg);
      }
      else
      {
         log("Validation error:" + msg);
      }
   }

   public final URL resolveResource(ResourceType type, String id) throws IOException
   {
      List<URL> found = resolveResources(type, id);
      return found.isEmpty() ? null : found.get(0);
   }

   public abstract List<URL> resolveResources(ResourceType type, String id) throws IOException;

   public abstract String getProperty(String propertyName);

}