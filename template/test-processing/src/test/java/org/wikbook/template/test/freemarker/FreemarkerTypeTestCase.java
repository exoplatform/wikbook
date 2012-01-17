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

package org.wikbook.template.test.freemarker;

import org.wikbook.template.freemarker.FreemarkerDataFactory;
import org.wikbook.template.freemarker.caller.AttributeCallerMethod;
import org.wikbook.template.freemarker.caller.JavadocCallerMethod;
import org.wikbook.template.test.AbstractFreemarkerTestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FreemarkerTypeTestCase extends AbstractFreemarkerTestCase {

  private Map<String, Object> b;
  private Map<String, Object> c;

  public FreemarkerTypeTestCase() throws IOException, ClassNotFoundException {

    b = buildModel("B", "src");
    c = buildModel("C", "src");
    
  }

  public void testExist() throws Exception {

    assertEquals(4, b.size());

  }

  public void testAnnotationSimpleValues() throws Exception {

    AttributeCallerMethod attributeCaller = (AttributeCallerMethod) ((Map<String, Object>) b.get("@AnnotationA")).get(FreemarkerDataFactory.ATTRIBUTE);
    assertEquals("b", attributeCaller.exec(Arrays.asList("value")).toString());

  }

  public void testAnnotationMultipleValues() throws Exception {

    AttributeCallerMethod attributeCaller = (AttributeCallerMethod) ((Map<String, Object>) b.get("@AnnotationB")).get(FreemarkerDataFactory.ATTRIBUTE);
    assertEquals("[a, b]", attributeCaller.exec(Arrays.asList("value")).toString());

  }

  public void testAnnotationMultipleListValues() throws Exception {

    AttributeCallerMethod attributeCaller = (AttributeCallerMethod) ((Map<String, Object>) b.get("@AnnotationB")).get(FreemarkerDataFactory.ATTRIBUTE);
    assertEquals("[a, b]", attributeCaller.exec(Arrays.asList("list:value")).toString());

  }

  public void testAnnotationMultipleFlatValues() throws Exception {

    AttributeCallerMethod attributeCaller = (AttributeCallerMethod) ((Map<String, Object>) b.get("@AnnotationB")).get(FreemarkerDataFactory.ATTRIBUTE);
    assertEquals("a, b", attributeCaller.exec(Arrays.asList("flat:value")).toString());

  }

  public void testAnnotationAnnotationValues() throws Exception {

    AttributeCallerMethod attributeCaller = (AttributeCallerMethod) ((Map<String, Object>) b.get("@AnnotationB2")).get(FreemarkerDataFactory.ATTRIBUTE);
    Map<String, Object> subAnnotation = (Map<String, Object>) attributeCaller.exec(Arrays.asList("value"));

    assertEquals("AnnotationA", subAnnotation.get(FreemarkerDataFactory.NAME));
    assertEquals("a", ((AttributeCallerMethod) subAnnotation.get(FreemarkerDataFactory.ATTRIBUTE)).exec(Arrays.asList("value")).toString());

  }

  public void testAnnotationManyAnnotationValues() throws Exception {

    AttributeCallerMethod attributeCaller = (AttributeCallerMethod) ((Map<String, Object>) b.get("@AnnotationB3")).get(FreemarkerDataFactory.ATTRIBUTE);

    List<Map<String, Object>> subAnnotations = (List<Map<String, Object>>) attributeCaller.exec(Arrays.asList("value"));

     assertEquals(2, subAnnotations.size());

    assertEquals("AnnotationA", subAnnotations.get(0).get(FreemarkerDataFactory.NAME));
    assertEquals("a1", ((AttributeCallerMethod) subAnnotations.get(0).get(FreemarkerDataFactory.ATTRIBUTE)).exec(Arrays.asList("value")).toString());

    assertEquals("AnnotationA", subAnnotations.get(1).get(FreemarkerDataFactory.NAME));
    assertEquals("a2", ((AttributeCallerMethod) subAnnotations.get(1).get(FreemarkerDataFactory.ATTRIBUTE)).exec(Arrays.asList("value")).toString());

  }

  public void testTypeName() throws Exception {

    assertEquals("B", ((Map<String, Map<String, String>>) b.get("@AnnotationB")).get(FreemarkerDataFactory.TYPE).get(FreemarkerDataFactory.NAME));
    assertEquals("model.B", ((Map<String, Map<String, String>>) b.get("@AnnotationB")).get(FreemarkerDataFactory.TYPE).get(FreemarkerDataFactory.FQN));
    assertEquals("false", ((Map<String, Map<String, String>>) b.get("@AnnotationB")).get(FreemarkerDataFactory.TYPE).get(FreemarkerDataFactory.IS_ARRAY));

  }

  public void testElementName() throws Exception {

    assertEquals("B", ((Map<String, String>) b.get("@AnnotationA")).get(FreemarkerDataFactory.ELEMENT_NAME));

  }

  public void testAnnotationName() throws Exception {

    assertEquals("AnnotationA", ((Map<String, String>) b.get("@AnnotationA")).get(FreemarkerDataFactory.NAME));

  }

  public void testJavadocGeneralComment() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("General comment.", docCaller.exec(new ArrayList()).toString());

  }

  public void testJavadocSingleValue() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("[1.0]", docCaller.exec(Arrays.asList("since")).toString());

  }

  public void testJavadocMultipleValue() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("[foo, bar]", docCaller.exec(Arrays.asList("author")).toString());

  }

  public void testJavadocNoValue() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("[deprecated]", docCaller.exec(Arrays.asList("deprecated")).toString());

  }

  public void testJavadocDoesntExist() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("", docCaller.exec(Arrays.asList("foo")).toString());

  }

  public void testJavadocSingleListValue() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("[1.0]", docCaller.exec(Arrays.asList("list:since")).toString());

  }

  public void testJavadocMultipleListValue() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("[foo, bar]", docCaller.exec(Arrays.asList("list:author")).toString());

  }

  public void testJavadocListNoValue() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("[deprecated]", docCaller.exec(Arrays.asList("list:deprecated")).toString());

  }

  public void testJavadocListDoesntExist() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("[]", docCaller.exec(Arrays.asList("list:foo")).toString());

  }

  public void testJavadocSingleFlatValue() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("1.0", docCaller.exec(Arrays.asList("flat:since")).toString());

  }

  public void testJavadocMultipleFlatValue() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("foo, bar", docCaller.exec(Arrays.asList("flat:author")).toString());

  }

  public void testJavadocFlatNoValue() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("deprecated", docCaller.exec(Arrays.asList("flat:deprecated")).toString());

  }

  public void testJavadocFlatDoesntExist() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals("", docCaller.exec(Arrays.asList("flat:foo")).toString());

  }

  public void testJavadocBloc() throws Exception {

    JavadocCallerMethod docCaller = (JavadocCallerMethod) ((Map<String, Object>) c.get("@AnnotationA")).get(FreemarkerDataFactory.JAVADOC);
    assertEquals(
        " here there is\n" +
        "   a\n" +
        " bloc",
        docCaller.exec(Arrays.asList("bloc:data")).toString());

  }
  
}
