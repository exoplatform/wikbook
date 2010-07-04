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

import org.wikbook.core.ResourceType;
import org.wikbook.core.ValidationMode;
import org.wikbook.core.WikbookValidationException;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;

/**
 * The contract between a {@link DocbookBuilder} and its client.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class DocbookBuilderContext
{

   /**
    * The behavior of this method depends on the context validation mode.
    * @param msg the message
    */
   public final void onValidationError(String msg)
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

   /**
    * Returns true if the code should be highlighted
    *
    * @return the code highlight mode
    */
   public abstract boolean getHighlightCode();

   /**
    * Returns the validation mode.
    *
    * @return the validation mode
    */
   public abstract ValidationMode getValidationMode();

   /**
    * Logs a message that is destined to be read by a human being.
    *
    * @param msg the message to log
    */
   public abstract void log(String msg);

   /**
    * Returns a list of candidate URLs for resolving a resource. The resource type is used to distinguish
    * where to find the resource of the specified type.
    *
    * @param type the resource type
    * @param id the resource id
    * @return the list of resolved resources
    * @throws IOException any IOException
    */
   public abstract List<URL> resolveResources(ResourceType type, String id) throws IOException;

   /**
    * Returns a property of this context.
    *
    * @param propertyName the property name
    * @return the property value
    */
   public abstract String getProperty(String propertyName);

   /**
    * Build with the provided reader stream and the provided builder.
    *
    * @param reader the reader
    * @param builder the builder
    */
   public abstract void build(Reader reader, String syntaxId, DocbookBuilder builder);

}
