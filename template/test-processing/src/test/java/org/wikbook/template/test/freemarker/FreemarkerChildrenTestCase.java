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

import freemarker.template.TemplateModelException;
import org.wikbook.template.freemarker.FreemarkerDataFactory;
import org.wikbook.template.freemarker.caller.AnnotationCallerMethod;
import org.wikbook.template.freemarker.caller.AttributeCallerMethod;
import org.wikbook.template.freemarker.caller.ChildrenCallerMethod;
import org.wikbook.template.freemarker.caller.JavadocCallerMethod;
import org.wikbook.template.freemarker.caller.SiblingCallerMethod;
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
public class FreemarkerChildrenTestCase extends AbstractFreemarkerTestCase {

  private ChildrenCallerMethod childrenCaller;
  private AttributeCallerMethod attributeCaller;

  private List<Map<String, Object>> data1;
  private List<Map<String, Object>> data2;

  private JavadocCallerMethod docCaller1;
  private JavadocCallerMethod docCaller2;

  private SiblingCallerMethod siblingCaller1;
  private SiblingCallerMethod siblingCaller2;

  private Map<String, Object> siblingData1;
  private Map<String, Object> siblingData2;

  private Map<String, Object> d;
  private Map<String, Object> f;
  private Map<String, Object> g;

  public FreemarkerChildrenTestCase() throws IOException, ClassNotFoundException, TemplateModelException {

    d = buildModel("D", "src");
    f = buildModel("F", "src");
    g = buildModel("G", "src");

    childrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) d.get("@AnnotationA")).get(FreemarkerDataFactory.CHILDREN);
    attributeCaller = (AttributeCallerMethod) ((Map<String, Object>) d.get("@AnnotationA")).get(FreemarkerDataFactory.ATTRIBUTE);

    data1 = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationC"));
    data2 = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationA"));

    docCaller1 = (JavadocCallerMethod) data1.get(0).get(FreemarkerDataFactory.JAVADOC);
    docCaller2 = (JavadocCallerMethod) data2.get(0).get(FreemarkerDataFactory.JAVADOC);

    siblingCaller1 = (SiblingCallerMethod) data1.get(0).get(FreemarkerDataFactory.SIBLING);
    siblingCaller2 = (SiblingCallerMethod) data2.get(0).get(FreemarkerDataFactory.SIBLING);

    siblingData2 = (Map<String, Object>) siblingCaller1.exec(Arrays.asList("@AnnotationA"));
    siblingData1 = (Map<String, Object>) siblingCaller1.exec(Arrays.asList("@AnnotationC"));

  }

  public void testExists() throws Exception {

    assertNotNull(childrenCaller.exec(Arrays.asList("@AnnotationC")));
    assertNotNull(childrenCaller.exec(Arrays.asList("@AnnotationA")));

  }

  public void testElementName() throws Exception {

    assertEquals("m", data1.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m", data2.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));

  }

  public void testAnnotationName() throws Exception {

    assertEquals("AnnotationC", data1.get(0).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationA", data2.get(0).get(FreemarkerDataFactory.NAME));

  }

  public void testTypeName() throws Exception {

    ChildrenCallerMethod dChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) f.get("@AnnotationA")).get(FreemarkerDataFactory.CHILDREN);

    List<Map<String, Object>> dGets = (List<Map<String, Object>>) dChildrenCaller.exec(Arrays.asList("@AnnotationD"));
    List<Map<String, Object>> dPaths = (List<Map<String, Object>>) dChildrenCaller.exec(Arrays.asList("@AnnotationA"));

    assertEquals("String", ((Map<String, Object>) dGets.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));
    assertEquals("String", ((Map<String, Object>) dPaths.get(1).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));

    assertEquals("java.lang.String", ((Map<String, Object>) dGets.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));
    assertEquals("java.lang.String", ((Map<String, Object>) dPaths.get(1).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));

    assertEquals("false", ((Map<String, Object>) dGets.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));
    assertEquals("false", ((Map<String, Object>) dPaths.get(1).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));

    ChildrenCallerMethod gChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) g.get("@AnnotationA")).get(FreemarkerDataFactory.CHILDREN);

    List<Map<String, Object>> gGets = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@AnnotationC"));
    List<Map<String, Object>> gPaths = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@AnnotationA"));

    assertEquals("String", ((Map<String, Object>) gGets.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));
    assertEquals("String", ((Map<String, Object>) gPaths.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));

    assertEquals("java.lang.String", ((Map<String, Object>) gGets.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));
    assertEquals("java.lang.String", ((Map<String, Object>) gPaths.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));

    assertEquals("true", ((Map<String, Object>) gGets.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));
    assertEquals("true", ((Map<String, Object>) gPaths.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));

    assertEquals(0, ((List) ((Map<String, Object>) gGets.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.PARAMETER)).size());
    assertEquals(0, ((List) ((Map<String, Object>) gPaths.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.PARAMETER)).size());

    assertEquals(1, ((List) ((Map<String, Object>) gGets.get(1).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.PARAMETER)).size());
    assertEquals(1, ((List) ((Map<String, Object>) gPaths.get(1).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.PARAMETER)).size());

    assertEquals(2, ((List) ((Map<String, Object>) gGets.get(2).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.PARAMETER)).size());
    assertEquals(2, ((List) ((Map<String, Object>) gPaths.get(2).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.PARAMETER)).size());

    AnnotationCallerMethod annotationCallerMethod = (AnnotationCallerMethod) ((Map<String, Object>) gPaths.get(3).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.ANNOTATION);
    AttributeCallerMethod attributeCallerMethod = (AttributeCallerMethod) ((Map<String, Object>) annotationCallerMethod.exec(Arrays.asList("@AnnotationA"))).get("attribute");

    assertEquals("Existing", ((Map<String, Object>) gGets.get(3).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));
    assertEquals("org.wikbook.template.existing.Existing", ((Map<String, Object>) gGets.get(3).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));
    assertEquals("foo", attributeCallerMethod.exec(Arrays.asList("value")).toString());

  }

  public void testTypeNotIsArray() throws Exception {

    ChildrenCallerMethod fChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) f.get("@AnnotationA")).get(FreemarkerDataFactory.CHILDREN);

    List<Map<String, Object>> fGets = (List<Map<String, Object>>) fChildrenCaller.exec(Arrays.asList("@AnnotationD"));
    List<Map<String, Object>> fPaths = (List<Map<String, Object>>) fChildrenCaller.exec(Arrays.asList("@AnnotationA"));

    assertEquals("false", ((Map<String, Object>) fGets.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));
    assertEquals("false", ((Map<String, Object>) fPaths.get(1).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));

  }

  public void testTypeIsArray() throws Exception {

    ChildrenCallerMethod gChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) g.get("@AnnotationA")).get(FreemarkerDataFactory.CHILDREN);

    List<Map<String, Object>> gGets = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@AnnotationC"));
    List<Map<String, Object>> gPaths = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@AnnotationA"));

    assertEquals("true", ((Map<String, Object>) gGets.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));
    assertEquals("true", ((Map<String, Object>) gPaths.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));

  }

  public void testTypeVoidName() throws Exception {

    assertEquals("", ((Map<String, Object>) data1.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));
    assertEquals("", ((Map<String, Object>) data2.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));

    assertEquals("", ((Map<String, Object>) data1.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));
    assertEquals("", ((Map<String, Object>) data2.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));

    assertEquals("false", ((Map<String, Object>) data1.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));
    assertEquals("false", ((Map<String, Object>) data2.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));

  }

  public void testJavadocGeneralComment() throws Exception {

    assertEquals("General comment.", docCaller1.exec(new ArrayList()).toString());
    assertEquals("General comment.", docCaller2.exec(new ArrayList()).toString());

  }

  public void testJavadocSingleValue() throws Exception {

    assertEquals("[1.0]", docCaller1.exec(Arrays.asList("since")).toString());
    assertEquals("[1.0]", docCaller2.exec(Arrays.asList("since")).toString());

  }

  public void testJavadocMultipleValue() throws Exception {

    assertEquals("[foo, bar]", docCaller1.exec(Arrays.asList("author")).toString());
    assertEquals("[foo, bar]", docCaller2.exec(Arrays.asList("author")).toString());

  }

  public void testJavadocNoValue() throws Exception {

    assertEquals("[deprecated]", docCaller1.exec(Arrays.asList("deprecated")).toString());
    assertEquals("[deprecated]", docCaller2.exec(Arrays.asList("deprecated")).toString());

  }

  public void testJavadocDoesntExist() throws Exception {

    assertEquals("", docCaller1.exec(Arrays.asList("foo")).toString());
    assertEquals("", docCaller2.exec(Arrays.asList("foo")).toString());

  }

  public void testJavadocSingleListValue() throws Exception {

    assertEquals("[1.0]", docCaller1.exec(Arrays.asList("list:since")).toString());
    assertEquals("[1.0]", docCaller2.exec(Arrays.asList("list:since")).toString());

  }

  public void testJavadocMultipleListValue() throws Exception {

    assertEquals("[foo, bar]", docCaller1.exec(Arrays.asList("list:author")).toString());
    assertEquals("[foo, bar]", docCaller2.exec(Arrays.asList("list:author")).toString());

  }

  public void testJavadocListNoValue() throws Exception {

    assertEquals("[deprecated]", docCaller1.exec(Arrays.asList("list:deprecated")).toString());
    assertEquals("[deprecated]", docCaller2.exec(Arrays.asList("list:deprecated")).toString());

  }

  public void testJavadocListDoesntExist() throws Exception {

    assertEquals("[]", docCaller1.exec(Arrays.asList("list:foo")).toString());
    assertEquals("[]", docCaller2.exec(Arrays.asList("list:foo")).toString());

  }

  public void testJavadocSingleFlatValue() throws Exception {

    assertEquals("1.0", docCaller1.exec(Arrays.asList("flat:since")).toString());
    assertEquals("1.0", docCaller2.exec(Arrays.asList("flat:since")).toString());

  }

  public void testJavadocMultipleFlatValue() throws Exception {

    assertEquals("foo, bar", docCaller1.exec(Arrays.asList("flat:author")).toString());
    assertEquals("foo, bar", docCaller2.exec(Arrays.asList("flat:author")).toString());

  }

  public void testJavadocFlatNoValue() throws Exception {

    assertEquals("deprecated", docCaller1.exec(Arrays.asList("flat:deprecated")).toString());
    assertEquals("deprecated", docCaller2.exec(Arrays.asList("flat:deprecated")).toString());

  }

  public void testJavadocFlatDoesntExist() throws Exception {

    assertEquals("", docCaller1.exec(Arrays.asList("flat:foo")).toString());
    assertEquals("", docCaller2.exec(Arrays.asList("flat:foo")).toString());

  }

  public void testSiblingName() throws Exception {

    assertEquals(data2.get(0).get(FreemarkerDataFactory.NAME), siblingData2.get(FreemarkerDataFactory.NAME));
    assertEquals(data1.get(0).get(FreemarkerDataFactory.NAME), siblingData1.get(FreemarkerDataFactory.NAME));

  }

  public void testSiblingElementName() throws Exception {

    assertEquals(data2.get(0).get(FreemarkerDataFactory.ELEMENT_NAME), siblingData2.get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals(data1.get(0).get(FreemarkerDataFactory.ELEMENT_NAME), siblingData1.get(FreemarkerDataFactory.ELEMENT_NAME));

  }

  public void testSiblingTypeName() throws Exception {

    assertEquals(
        ((Map<String, String>) data2.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME),
        ((Map<String, String>) siblingData2.get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));
    assertEquals(
        ((Map<String, String>) data1.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME),
        ((Map<String, String>) siblingData2.get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));

  }

  public void testSiblingTypeFullName() throws Exception {

    assertEquals(
        ((Map<String, String>) data2.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN),
        ((Map<String, String>) siblingData2.get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));
    assertEquals(
        ((Map<String, String>) data1.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN),
        ((Map<String, String>) siblingData2.get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));

  }

  public void testSiblingTypeIsArray() throws Exception {

    assertEquals(
        ((Map<String, String>) data2.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY),
        ((Map<String, String>) siblingData2.get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));
    assertEquals(
        ((Map<String, String>) data1.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY),
        ((Map<String, String>) siblingData2.get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));

  }

  public void testSiblingDoc() throws Exception {

    assertEquals(
        docCaller2.exec(Arrays.asList("since")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("since")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("since")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("since")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("author")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("author")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("author")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("author")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("deprecated")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("deprecated")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("deprecated")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("deprecated")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("foo")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("foo")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("foo")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("foo")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("list:since")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("list:since")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("list:since")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("list:since")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("list:author")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("list:author")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("list:author")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("list:author")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("list:deprecated")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("list:deprecated")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("list:deprecated")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("list:deprecated")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("list:foo")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("list:foo")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("list:foo")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("list:foo")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("flat:since")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("flat:since")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("flat:since")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("flat:since")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("flat:author")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("flat:author")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("flat:author")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("flat:author")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("flat:deprecated")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("flat:deprecated")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("flat:deprecated")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("flat:deprecated")).toString());

    assertEquals(
        docCaller2.exec(Arrays.asList("flat:foo")).toString(),
        ((JavadocCallerMethod) siblingData2.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("flat:foo")).toString());
    assertEquals(
        docCaller1.exec(Arrays.asList("flat:foo")).toString(),
        ((JavadocCallerMethod) siblingData1.get(FreemarkerDataFactory.JAVADOC)).exec(Arrays.asList("flat:foo")).toString());

  }

  public void testSiblingAttribute() throws Exception {

    assertEquals(
        ((AttributeCallerMethod) data2.get(0).get(FreemarkerDataFactory.ATTRIBUTE)).exec(Arrays.asList("value")).toString(),
        ((AttributeCallerMethod) siblingData2.get(FreemarkerDataFactory.ATTRIBUTE)).exec(Arrays.asList("value")).toString()
    );

  }
  
}