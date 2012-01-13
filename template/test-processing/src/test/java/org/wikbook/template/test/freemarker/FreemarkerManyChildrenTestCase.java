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
import org.wikbook.template.test.AbstractFreemarkerTestCase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FreemarkerManyChildrenTestCase extends AbstractFreemarkerTestCase {

  private ChildrenCallerMethod childrenCaller;

  private List<Map<String, Object>> data1;
  private List<Map<String, Object>> data2;
  private List<Map<String, Object>> data3;
  private List<Map<String, Object>> data4;

  private Map<String, Object> f;

  public FreemarkerManyChildrenTestCase() throws IOException, ClassNotFoundException, TemplateModelException {

    f = buildModel("F", "src");

    childrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) f.get("@AnnotationA")).get(FreemarkerDataFactory.CHILDREN);

    data1 = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationC"));
    data3 = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationD"));
    data2 = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationA"));
    data4 = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationC", "@AnnotationD"));

  }

  public void testExists() throws Exception {

    assertNotNull(data1);
    assertNotNull(data3);
    assertNotNull(data2);

    assertEquals(2, data1.size());
    assertEquals(1, data3.size());
    assertEquals(3, data2.size());

  }

  public void testOneTypeElementName() throws Exception {

    assertEquals("m", data1.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m3", data1.get(1).get(FreemarkerDataFactory.ELEMENT_NAME));

    assertEquals("m2", data3.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));

    assertEquals("m", data2.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m2", data2.get(1).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m3", data2.get(2).get(FreemarkerDataFactory.ELEMENT_NAME));

  }

  public void testOneTypeAnnotationName() throws Exception {

    assertEquals("AnnotationC", data1.get(0).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationC", data1.get(1).get(FreemarkerDataFactory.NAME));

    assertEquals("AnnotationD", data3.get(0).get(FreemarkerDataFactory.NAME));

    assertEquals("AnnotationA", data2.get(0).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationA", data2.get(1).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationA", data2.get(2).get(FreemarkerDataFactory.NAME));

  }

  public void testManyTypeElementName() throws Exception {

    assertEquals("m", data4.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m2", data4.get(1).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m3", data4.get(2).get(FreemarkerDataFactory.ELEMENT_NAME));

  }

  public void testManyTypeAnnotationName() throws Exception {

    assertEquals("AnnotationC", data4.get(0).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationD", data4.get(1).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationC", data4.get(2).get(FreemarkerDataFactory.NAME));

  }
  
}