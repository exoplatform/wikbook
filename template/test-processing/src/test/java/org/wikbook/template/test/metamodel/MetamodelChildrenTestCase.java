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
public class MetamodelChildrenTestCase extends AbstractProcessorTestCase {

  @Override
  public void setUp() throws Exception {

    super.setUp();

  }

  public void testExists() throws Exception {

    MetaModel metaModel = buildClass("D");
    assertEquals(1, metaModel.getElements().get(0).getAnnotations().values().iterator().next().getChildren().size());

  }

  public void testElementName() throws Exception {

    MetaModel metaModel = buildClass("D");
    assertEquals("m", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getElement().getName());
    assertEquals("m", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getElement().getName());

  }

  public void testAnnotationName() throws Exception {

    MetaModel metaModel = buildClass("D");
    assertEquals("@AnnotationC", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getName());
    assertEquals("@AnnotationA", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getName());

  }

  public void testJavadocGeneralComment() throws Exception {

    MetaModel metaModel = buildClass("D");
    assertEquals("[[ General comment.]]", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getJavadoc().get(null).toString());
    assertEquals("[[ General comment.]]", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getJavadoc().get(null).toString());

  }

  public void testJavadocSingleValue() throws Exception {

    MetaModel metaModel = buildClass("D");
    assertEquals("[[1.0]]", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getJavadoc().get("since").toString());
    assertEquals("[[1.0]]", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getJavadoc().get("since").toString());

  }

  public void testJavadocMultipleValue() throws Exception {

    MetaModel metaModel = buildClass("D");
    assertEquals("[[foo], [bar]]", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getJavadoc().get("author").toString());
    assertEquals("[[foo], [bar]]", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getJavadoc().get("author").toString());

  }

  public void testJavadocNoValue() throws Exception {

    MetaModel metaModel = buildClass("D");
    assertEquals("[[deprecated]]", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getJavadoc().get("deprecated").toString());
    assertEquals("[[deprecated]]", metaModel.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getJavadoc().get("deprecated").toString());

  }


  public void testTypeName() throws Exception {

    MetaModel metaModel = buildClass("F");
    assertEquals("String", metaModel.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().getName());
    assertEquals("java.lang.String", metaModel.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().getFullName());

    MetaModel g = buildClass("G");
    assertEquals("String[]", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().getName());
    assertEquals("java.lang.String[]", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().getFullName());

  }

  public void testTypeVoid() throws Exception {

    MetaModel metaModel = buildClass("F");
    assertEquals("", metaModel.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().getName());
    assertEquals("", metaModel.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().getFullName());
    assertEquals(false, metaModel.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().isArray().booleanValue());

  }

  public void testTypeIsArray() throws Exception {

    MetaModel f = buildClass("F");
    assertEquals(false, f.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().isArray().booleanValue());

    MetaModel g = buildClass("G");
    assertEquals(true, g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().isArray().booleanValue());

  }

}
