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

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
    * @param id the callout id
    * @param text the callout text value
    */
   void setCallout(String id, String text);

   /**
    * Resolve a code source such as the content of a class.
    *
    * @param id the resource id
    * @return a list of URL that will load the resource
    * @throws IOException any io exception
    */
   List<URL> resolveResources(String id) throws IOException;

}
