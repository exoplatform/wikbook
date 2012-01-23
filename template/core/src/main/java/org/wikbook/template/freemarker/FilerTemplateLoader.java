/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.wikbook.template.freemarker;

import freemarker.cache.TemplateLoader;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FilerTemplateLoader implements TemplateLoader {

  private Filer filer;

  FilerTemplateLoader(final Filer filer) {
    this.filer = filer;
  }

  public Object findTemplateSource(final String name) throws IOException {

    try {
      FileObject fo = filer.getResource(StandardLocation.SOURCE_PATH, "templates", name);
      return fo.openInputStream();
    }
    catch (FileNotFoundException e) {
      return null;
    }

  }

  public long getLastModified(final Object templateSource) {
    return 0;
  }

  public Reader getReader(final Object templateSource, final String encoding) throws IOException {
    return new InputStreamReader((InputStream) templateSource);
  }

  public void closeTemplateSource(final Object templateSource) throws IOException {
    ((InputStream) templateSource).close();
  }

}
