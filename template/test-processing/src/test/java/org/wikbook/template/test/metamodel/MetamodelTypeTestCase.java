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

  }

  public void testExist() throws Exception {

    MetaModel metaModel = buildClass("B");
    assertEquals(2, metaModel.getElements().get(0).getAnnotations().size());
    assertNotNull(metaModel.getElements().get(0).getAnnotation("@Path"));

  }

  public void testAnnotationSimpleValues() throws Exception {

    MetaModel metaModel = buildClass("B");
    assertEquals("value", metaModel.getElements().get(0).getAnnotation("@Path").getValues().keySet().iterator().next());
    assertEquals("b", metaModel.getElements().get(0).getAnnotation("@Path").getValues().values().iterator().next());

  }

  public void testElementName() throws Exception {

    MetaModel metaModel = buildClass("B");
    assertEquals("B", metaModel.getElements().get(0).getAnnotation("@Path").getElement().getName());

  }

  public void testAnnotationName() throws Exception {

    MetaModel metaModel = buildClass("B");
    assertEquals("@Path", metaModel.getElements().get(0).getAnnotation("@Path").getName());

  }

  public void testJavadocGeneralComment() throws Exception {

    MetaModel metaModel = buildClass("C");
    assertEquals("[[General comment.]]", metaModel.getElements().get(0).getAnnotation("@Path").getJavadoc().get(null).toString());

  }

  public void testJavadocSingleValue() throws Exception {

    MetaModel metaModel = buildClass("C");
    assertEquals("[[1.0]]", metaModel.getElements().get(0).getAnnotation("@Path").getJavadoc().get("since").toString());

  }

  public void testJavadocMultipleValue() throws Exception {

    MetaModel metaModel = buildClass("C");
    assertEquals("[[foo], [bar]]", metaModel.getElements().get(0).getAnnotation("@Path").getJavadoc().get("author").toString());

  }

  public void testJavadocNoValue() throws Exception {

    MetaModel metaModel = buildClass("C");
    assertEquals("[[deprecated]]", metaModel.getElements().get(0).getAnnotation("@Path").getJavadoc().get("deprecated").toString());

  }
  
}
