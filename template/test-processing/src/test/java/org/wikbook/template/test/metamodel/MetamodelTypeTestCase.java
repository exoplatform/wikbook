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
import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.test.AbstractProcessorTestCase;

import java.io.IOException;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class MetamodelTypeTestCase extends AbstractProcessorTestCase {

  private MetaModel b;
  private MetaModel c;

  public MetamodelTypeTestCase() throws ClassNotFoundException, IOException {

    buildClass("B");
    buildClass("C");

    b = readMetaModel("model.B.src");
    c = readMetaModel("model.C.src");

  }

  public void testExist() throws Exception {

    assertEquals(4, b.getElements().get(0).getAnnotations().size());
    assertNotNull(b.getElements().get(0).getAnnotation("@AnnotationA"));

  }

  public void testAnnotationSimpleValues() throws Exception {

    assertEquals("value", b.getElements().get(0).getAnnotation("@AnnotationA").getValues().keySet().iterator().next());
    assertEquals("b", b.getElements().get(0).getAnnotation("@AnnotationA").getValues().values().iterator().next());

  }

  public void testAnnotationAnnotationValues() throws Exception {

    Map<String, Object> b2Values = b.getElements().get(0).getAnnotation("@AnnotationB2").getValues();

    assertEquals("value", b2Values.keySet().iterator().next());

    TemplateAnnotation a = (TemplateAnnotation) b2Values.values().iterator().next();

    assertEquals("@AnnotationA", a.getName());
    assertEquals(1, a.getValues().size());
    assertEquals("value", a.getValues().keySet().iterator().next());
    assertEquals("a", a.getValues().values().iterator().next());

  }

  public void testAnnotationManyAnnotationValues() throws Exception {

    Map<String, Object> b3Values = b.getElements().get(0).getAnnotation("@AnnotationB3").getValues();

    assertEquals("value", b3Values.keySet().iterator().next());

    TemplateAnnotation[] annotations = (TemplateAnnotation[]) b3Values.values().iterator().next();

    assertEquals(2, annotations.length);

    assertEquals("@AnnotationA", annotations[0].getName());
    assertEquals(1, annotations[0].getValues().size());
    assertEquals("value", annotations[0].getValues().keySet().iterator().next());
    assertEquals("a1", annotations[0].getValues().values().iterator().next());

    assertEquals("@AnnotationA", annotations[1].getName());
    assertEquals(1, annotations[1].getValues().size());
    assertEquals("value", annotations[1].getValues().keySet().iterator().next());
    assertEquals("a2", annotations[1].getValues().values().iterator().next());
    
  }

  public void testTypeName() throws Exception {

    assertEquals("B", b.getElements().get(0).getAnnotation("@AnnotationA").getElement().getType().getName());
    assertEquals("model.B", b.getElements().get(0).getAnnotation("@AnnotationA").getElement().getType().getFqn());
    assertEquals(Boolean.FALSE, b.getElements().get(0).getAnnotation("@AnnotationA").getElement().getType().isArray());

  }

  public void testElementName() throws Exception {

    assertEquals("B", b.getElements().get(0).getAnnotation("@AnnotationA").getElement().getName());

  }

  public void testAnnotationName() throws Exception {

    assertEquals("@AnnotationA", b.getElements().get(0).getAnnotation("@AnnotationA").getName());

  }

  public void testJavadocGeneralComment() throws Exception {

    assertEquals("[[ General comment.]]", c.getElements().get(0).getAnnotation("@AnnotationA").getJavadoc().get(null).toString());

  }

  public void testJavadocSingleValue() throws Exception {

    assertEquals("[[1.0]]", c.getElements().get(0).getAnnotation("@AnnotationA").getJavadoc().get("since").toString());

  }

  public void testJavadocMultipleValue() throws Exception {

    assertEquals("[[foo], [bar]]", c.getElements().get(0).getAnnotation("@AnnotationA").getJavadoc().get("author").toString());

  }

  public void testJavadocNoValue() throws Exception {

    assertEquals("[[deprecated]]", c.getElements().get(0).getAnnotation("@AnnotationA").getJavadoc().get("deprecated").toString());

  }

  public void testJavadocBloc() throws Exception {

    assertEquals(" here there is", c.getElements().get(0).getAnnotation("@AnnotationA").getJavadoc().get("data").get(0).get(0));
    assertEquals("   a", c.getElements().get(0).getAnnotation("@AnnotationA").getJavadoc().get("data").get(0).get(1));
    assertEquals(" bloc", c.getElements().get(0).getAnnotation("@AnnotationA").getJavadoc().get("data").get(0).get(2));

  }

}
