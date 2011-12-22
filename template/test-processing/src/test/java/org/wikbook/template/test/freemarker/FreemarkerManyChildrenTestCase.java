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
import org.wikbook.template.freemarker.caller.ChildrenCallerMethod;
import org.wikbook.template.test.AbstractFreemarkerTestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FreemarkerManyChildrenTestCase extends AbstractFreemarkerTestCase {

  private ChildrenCallerMethod childrenCaller;

  private List<Map<String, Object>> posts;
  private List<Map<String, Object>> paths;
  private List<Map<String, Object>> gets;
  private List<Map<String, Object>> postsgets;

  @Override
  public void setUp() throws Exception {

    super.setUp();

    Map<String, Object> data = buildModel("F");

    childrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) data.get("@AnnotationA")).get(FreemarkerDataFactory.CHILDREN);

    posts = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationC"));
    gets = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationD"));
    paths = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationA"));
    postsgets = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationC", "@AnnotationD"));

  }

  public void testExists() throws Exception {

    assertNotNull(posts);
    assertNotNull(gets);
    assertNotNull(paths);

    assertEquals(2, posts.size());
    assertEquals(1, gets.size());
    assertEquals(3, paths.size());

  }

  public void testOneTypeElementName() throws Exception {

    assertEquals("m", posts.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m3", posts.get(1).get(FreemarkerDataFactory.ELEMENT_NAME));

    assertEquals("m2", gets.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));

    assertEquals("m", paths.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m2", paths.get(1).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m3", paths.get(2).get(FreemarkerDataFactory.ELEMENT_NAME));

  }

  public void testOneTypeAnnotationName() throws Exception {

    assertEquals("AnnotationC", posts.get(0).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationC", posts.get(1).get(FreemarkerDataFactory.NAME));

    assertEquals("AnnotationD", gets.get(0).get(FreemarkerDataFactory.NAME));

    assertEquals("AnnotationA", paths.get(0).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationA", paths.get(1).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationA", paths.get(2).get(FreemarkerDataFactory.NAME));

  }

  public void testManyTypeElementName() throws Exception {

    assertEquals("m", postsgets.get(0).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m2", postsgets.get(1).get(FreemarkerDataFactory.ELEMENT_NAME));
    assertEquals("m3", postsgets.get(2).get(FreemarkerDataFactory.ELEMENT_NAME));

  }

  public void testManyTypeAnnotationName() throws Exception {

    assertEquals("AnnotationC", postsgets.get(0).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationD", postsgets.get(1).get(FreemarkerDataFactory.NAME));
    assertEquals("AnnotationC", postsgets.get(2).get(FreemarkerDataFactory.NAME));

  }
  
}