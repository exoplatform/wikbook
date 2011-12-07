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

import org.wikbook.template.freemarker.caller.ChildrenCallerMethod;
import org.wikbook.template.freemarker.caller.JavadocCallerMethod;
import org.wikbook.template.freemarker.caller.SiblingCallerMethod;
import org.wikbook.template.test.AbstractFreemarkerTestCase;

import java.util.ArrayList;
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

  @Override
  public void setUp() throws Exception {

    super.setUp();
    annotations = "javax.ws.rs.Path,javax.ws.rs.POST,javax.ws.rs.GET";

    Map<String, Object> data = buildModel("F");

    childrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) data.get("@Path")).get("children");

    posts = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@POST"));
    gets = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@GET"));
    paths = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@Path"));

  }

  public void testExists() throws Exception {

    assertNotNull(posts);
    assertNotNull(gets);
    assertNotNull(paths);

    assertEquals(2, posts.size());
    assertEquals(1, gets.size());
    assertEquals(3, paths.size());

  }

  public void testElementName() throws Exception {

    assertEquals("m", posts.get(0).get("elementName"));
    assertEquals("m3", posts.get(1).get("elementName"));

    assertEquals("m2", gets.get(0).get("elementName"));

    assertEquals("m", paths.get(0).get("elementName"));
    assertEquals("m2", paths.get(1).get("elementName"));
    assertEquals("m3", paths.get(2).get("elementName"));

  }

  public void testAnnotationName() throws Exception {

    assertEquals("POST", posts.get(0).get("name"));
    assertEquals("POST", posts.get(1).get("name"));

    assertEquals("GET", gets.get(0).get("name"));

    assertEquals("Path", paths.get(0).get("name"));
    assertEquals("Path", paths.get(1).get("name"));
    assertEquals("Path", paths.get(2).get("name"));

  }
  
}