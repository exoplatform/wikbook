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

package org.wikbook.core.codesource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Contract for interacting with a code processor.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public interface CodeContext
{

   /**
    * Write code content.
    *
    * @param content the content
    */
   void writeContent(String content);

   /**
    * Write a callout
    *
    * @param id the callout id
    */
   void writeCallout(String id);

   /**
    * Define a callout text value.
    *
    * @param id   the callout id
    * @param text the callout text value
    */
   void setCallout(String id, String text);

   /**
    * Resolve a code resource and returns an {{code InputStream}} otherwise returns null if no such resource can be
    * resolved.
    *
    * @param id the resource id
    * @return the resource input stream
    * @throws IOException any io exception
    */
   InputStream resolveResources(String id) throws IOException;

}
