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

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class MetamodelTypeTestCase extends AbstractProcessorTestCase {

  @Override
  public void setUp() throws Exception {

    super.setUp();
    annotations = "javax.ws.rs.Path,javax.ws.rs.Consumes";

  }

  public void testExist() throws Exception {

    MetaModel metaModel = buildClass("B");
    assertEquals(2, metaModel.getAnnotations().size());
    assertEquals("@Path", metaModel.getAnnotations().get(0).getName());
    assertEquals("@Consumes", metaModel.getAnnotations().get(1).getName());

  }

  public void testAnnotationSimpleValues() throws Exception {

    MetaModel metaModel = buildClass("B");
    assertEquals(1, metaModel.getAnnotations().get(0).getValues().size());
    assertEquals("value", metaModel.getAnnotations().get(0).getValues().keySet().iterator().next());
    assertEquals("b", metaModel.getAnnotations().get(0).getValues().values().iterator().next());

  }

  public void testElementName() throws Exception {

    MetaModel metaModel = buildClass("B");
    assertEquals("B", metaModel.getAnnotations().get(0).getElement().getName());

  }

  public void testAnnotationName() throws Exception {

    MetaModel metaModel = buildClass("B");
    assertEquals("@Path", metaModel.getAnnotations().get(0).getName());

  }

  public void testJavadocGeneralComment() throws Exception {

    MetaModel metaModel = buildClass("C");
    assertEquals("[General comment.]", metaModel.getAnnotations().get(0).getJavadoc().get(null).toString());

  }

  public void testJavadocSingleValue() throws Exception {

    MetaModel metaModel = buildClass("C");
    assertEquals("[1.0]", metaModel.getAnnotations().get(0).getJavadoc().get("since").toString());

  }

  public void testJavadocMultipleValue() throws Exception {

    MetaModel metaModel = buildClass("C");
    assertEquals("[foo, bar]", metaModel.getAnnotations().get(0).getJavadoc().get("author").toString());

  }

  public void testJavadocNoValue() throws Exception {

    MetaModel metaModel = buildClass("C");
    assertEquals("[deprecated]", metaModel.getAnnotations().get(0).getJavadoc().get("deprecated").toString());

  }
  
}
