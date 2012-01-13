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
public class MetamodelChildrenTestCase extends AbstractProcessorTestCase {

  private MetaModel d;
  private MetaModel f;
  private MetaModel g;

  public MetamodelChildrenTestCase() throws ClassNotFoundException, IOException {

    buildClass("D");
    buildClass("F");
    buildClass("G");

    d = readMetaModel("model.D.src");
    f = readMetaModel("model.F.src");
    g = readMetaModel("model.G.src");

  }


  public void testExists() throws Exception {

    assertEquals(1, d.getElements().get(0).getAnnotations().values().iterator().next().getChildren().size());

  }

  public void testElementName() throws Exception {

    assertEquals("m", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getElement().getName());
    assertEquals("m", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getElement().getName());

  }

  public void testAnnotationName() throws Exception {

    assertEquals("@AnnotationC", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getName());
    assertEquals("@AnnotationA", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getName());

  }

  public void testJavadocGeneralComment() throws Exception {

    assertEquals("[[ General comment.]]", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getJavadoc().get(null).toString());
    assertEquals("[[ General comment.]]", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getJavadoc().get(null).toString());

  }

  public void testJavadocSingleValue() throws Exception {

    assertEquals("[[1.0]]", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getJavadoc().get("since").toString());
    assertEquals("[[1.0]]", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getJavadoc().get("since").toString());

  }

  public void testJavadocMultipleValue() throws Exception {

    assertEquals("[[foo], [bar]]", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getJavadoc().get("author").toString());
    assertEquals("[[foo], [bar]]", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getJavadoc().get("author").toString());

  }

  public void testJavadocNoValue() throws Exception {

    assertEquals("[[deprecated]]", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationC").getJavadoc().get("deprecated").toString());
    assertEquals("[[deprecated]]", d.getElements().get(0).getElement().get(0).getAnnotation("@AnnotationA").getJavadoc().get("deprecated").toString());

  }


  public void testTypeName() throws Exception {
    
    assertEquals("String", f.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().getName());
    assertEquals("java.lang.String", f.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().getFqn());


    assertEquals("String", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().getName());
    assertEquals("java.lang.String", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().getFqn());

    assertEquals("Collection", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().getName());
    assertEquals("java.util.Collection", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().getFqn());
    assertEquals(1, g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().getParameters().length);
    assertEquals("String", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().getParameters()[0].getName());
    assertEquals("java.lang.String", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().getParameters()[0].getFqn());
    assertEquals(Boolean.FALSE, g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().getParameters()[0].isArray());

    assertEquals("Map", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(2).getType().getName());
    assertEquals("java.util.Map", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(2).getType().getFqn());
    assertEquals(2, g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(2).getType().getParameters().length);
    assertEquals("Float", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(2).getType().getParameters()[0].getName());
    assertEquals("java.lang.Float", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(2).getType().getParameters()[0].getFqn());
    assertEquals(Boolean.FALSE, g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(2).getType().getParameters()[0].isArray());
    assertEquals("Integer", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(2).getType().getParameters()[1].getName());
    assertEquals("java.lang.Integer", g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(2).getType().getParameters()[1].getFqn());
    assertEquals(Boolean.TRUE, g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(2).getType().getParameters()[1].isArray());

  }

  public void testTypeVoid() throws Exception {

    assertEquals("", f.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().getName());
    assertEquals("", f.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().getFqn());
    assertEquals(false, f.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().isArray().booleanValue());

  }

  public void testTypeIsArray() throws Exception {

    assertEquals(false, f.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(1).getType().isArray().booleanValue());
    assertEquals(true, g.getElements().get(0).getAnnotation("@AnnotationA").getChildren().get(0).getType().isArray().booleanValue());

  }

}
