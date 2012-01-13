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

package org.wikbook.template.test.metamodel;

import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.test.AbstractProcessorTestCase;

import java.io.IOException;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class MetamodelPackageTestCase extends AbstractProcessorTestCase {

  private MetaModel m;

  public MetamodelPackageTestCase() throws ClassNotFoundException, IOException {

    buildClass("package-info");

    m = readMetaModel("model.pkg");

  }

  public void testPackage() throws Exception {

    assertEquals(1, m.getElements().size());
    assertEquals("model", m.getElements().get(0).getName());
    assertEquals(null, m.getElements().get(0).getType());

  }

  public void testAnnotation() throws Exception {

    assertEquals(1, m.getElements().get(0).getAnnotations().size());
    assertEquals("@AnnotationPackage", m.getElements().get(0).getAnnotations().keySet().iterator().next());

  }

  public void testChildren() throws Exception {

    assertEquals(6, m.getElements().get(0).getElement().size());
    assertEquals("B", m.getElements().get(0).getElement().get(0).getName());
    assertEquals("B", m.getElements().get(0).getElement().get(0).getType().getName());
    assertEquals("model.B", m.getElements().get(0).getElement().get(0).getType().getFqn());

  }

  public void testJavaDoc() throws Exception {

    assertEquals(3, m.getElements().get(0).getJavadoc().size());

  }

}
