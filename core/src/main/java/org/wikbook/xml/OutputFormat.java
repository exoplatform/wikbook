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

package org.wikbook.xml;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class OutputFormat
{

   /** . */
   private final Integer indent;

   /** . */
   private final boolean omitDeclaration;

   /** . */
   private final String publicId;

   /** . */
   private final String systemId;

   public OutputFormat(Integer indent, boolean omitDeclaration)
   {
      this(indent, omitDeclaration, null, null);
   }

   public OutputFormat(Integer indent, boolean omitDeclaration, String publicId, String systemId)
   {
      this.indent = indent;
      this.omitDeclaration = omitDeclaration;
      this.publicId = publicId;
      this.systemId = systemId;
   }

   public Integer getIndent()
   {
      return indent;
   }

   public boolean isOmitDeclaration()
   {
      return omitDeclaration;
   }

   public String getPublicId()
   {
      return publicId;
   }

   public String getSystemId()
   {
      return systemId;
   }
}
