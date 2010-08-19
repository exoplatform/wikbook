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

package org.wikbook.xwiki;

import junit.framework.TestCase;
import org.wikbook.core.ResourceType;
import org.wikbook.core.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class WikiFileEncodingTestCase extends TestCase
{
   public void testReadFileInUTF8() throws IOException
   {
      String expected = "éàç";
      File wiki = File.createTempFile("encoding", ".wiki");
      wiki.deleteOnExit();
      Writer writer = new OutputStreamWriter(new FileOutputStream(wiki), "UTF-8");
      try
      {
         writer.write(expected);
      }
      finally
      {
         writer.close();
      }

      //
      SimpleXDOMDocbookBuilderContext ctx = new SimpleXDOMDocbookBuilderContext(wiki.getParentFile());
      Reader reader = ctx._load(wiki.getName());
      String actual = Utils.read(reader);
      reader.close();
      assertEquals(expected, actual);
   }
}
