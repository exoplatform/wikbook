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
public class MetamodelParamTestCase extends AbstractProcessorTestCase {

  private MetaModel e;

  public MetamodelParamTestCase() throws ClassNotFoundException, IOException {

    buildClass("E");

    e = readMetaModel("model.E.src");

  }

  public void testExists() throws Exception {

    assertEquals("@AnnotationA", e.getElements().get(0).getElements().get(0).getElements().get(1).getAnnotation("@AnnotationA").getName());
    assertEquals("@AnnotationA2", e.getElements().get(0).getElements().get(0).getElements().get(2).getAnnotation("@AnnotationA2").getName());

  }

  public void testElementName() throws Exception {

    assertEquals("p1", e.getElements().get(0).getElements().get(0).getElements().get(1).getAnnotation("@AnnotationA").getElement().getName());
    assertEquals("p2", e.getElements().get(0).getElements().get(0).getElements().get(2).getAnnotation("@AnnotationA2").getElement().getName());

  }

  public void testJavadoc() throws Exception {

    assertEquals("[[P1 parameter description]]", e.getElements().get(0).getElements().get(0).getElements().get(1).getAnnotation("@AnnotationA").getJavadoc(null).toString());
    assertEquals("[[P2 parameter description]]", e.getElements().get(0).getElements().get(0).getElements().get(2).getAnnotation("@AnnotationA2").getJavadoc(null).toString());

  }

  public void testTypeName() throws Exception {

    assertEquals("String", e.getElements().get(0).getElements().get(0).getElements().get(1).getAnnotation("@AnnotationA").getElement().getType().getName());
    assertEquals("java.lang.String", e.getElements().get(0).getElements().get(0).getElements().get(1).getAnnotation("@AnnotationA").getElement().getType().getFqn());
    assertEquals(Boolean.FALSE, e.getElements().get(0).getElements().get(0).getElements().get(1).getAnnotation("@AnnotationA").getElement().getType().isArray());

  }

  public void testArrayTypeName() throws Exception {

    assertEquals("String", e.getElements().get(0).getElements().get(0).getElements().get(2).getAnnotation("@AnnotationA2").getElement().getType().getName());
    assertEquals("java.lang.String", e.getElements().get(0).getElements().get(0).getElements().get(2).getAnnotation("@AnnotationA2").getElement().getType().getFqn());
    assertEquals(Boolean.TRUE, e.getElements().get(0).getElements().get(0).getElements().get(2).getAnnotation("@AnnotationA2").getElement().getType().isArray());

  }

}
