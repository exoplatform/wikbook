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
import org.wikbook.template.freemarker.caller.ChildrenCallerMethod;
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
public class FreemarkerParamTestCase extends AbstractFreemarkerTestCase {

  private ChildrenCallerMethod childrenCaller1;
  private ChildrenCallerMethod childrenCaller2;

  private List<Map<String, Object>> data1;
  private List<Map<String, Object>> data2;
  private List<Map<String, Object>> data3;

  private JavadocCallerMethod docCaller1;
  private JavadocCallerMethod docCaller2;

  private Map<String, Object> e;

  public FreemarkerParamTestCase() throws IOException, ClassNotFoundException, TemplateModelException {

    e = buildModel("E", "src");

    childrenCaller1 = (ChildrenCallerMethod) ((Map<String, Object>) e.get("@AnnotationA")).get(FreemarkerDataFactory.CHILDREN);
    data1 = (List<Map<String, Object>>) childrenCaller1.exec(Arrays.asList("@AnnotationA"));

    childrenCaller2 = (ChildrenCallerMethod) data1.get(0).get(FreemarkerDataFactory.CHILDREN);
    data2 = (List<Map<String, Object>>) childrenCaller2.exec(Arrays.asList("@AnnotationA"));
    data3 = (List<Map<String, Object>>) childrenCaller2.exec(Arrays.asList("@AnnotationA2"));

    docCaller1 = (JavadocCallerMethod) data2.get(0).get(FreemarkerDataFactory.JAVADOC);
    docCaller2 = (JavadocCallerMethod) data3.get(0).get(FreemarkerDataFactory.JAVADOC);

  }

  public void testExists() throws Exception {

    assertEquals("AnnotationA", data2.get(0).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationA2", data3.get(0).get(FreemarkerDataFactory.NAME));

  }

  public void testElementName() throws Exception {

    assertEquals("p1", data2.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("p2", data3.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));

  }

  public void testTypeName() throws Exception {

    assertEquals("String", ((Map<String, String>) data2.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));
    assertEquals("java.lang.String", ((Map<String, String>) data2.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));
    assertEquals("false", ((Map<String, String>) data2.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));

  }

  public void testArrayTypeName() throws Exception {

    assertEquals("String", ((Map<String, String>) data3.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.NAME));
    assertEquals("java.lang.String", ((Map<String, String>) data3.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.FQN));
    assertEquals("true", ((Map<String, String>) data3.get(0).get(FreemarkerDataFactory.TYPE)).get(FreemarkerDataFactory.IS_ARRAY));

  }

  public void testJavadoc() throws Exception {

    assertEquals("P1 parameter description", docCaller1.exec(new ArrayList()).toString());
    assertEquals("P2 parameter description", docCaller2.exec(new ArrayList()).toString());

  }

}
