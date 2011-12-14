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

import org.wikbook.template.freemarker.caller.AttributeCallerMethod;
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
public class FreemarkerChildrenTestCase extends AbstractFreemarkerTestCase {

  private ChildrenCallerMethod childrenCaller;
  private AttributeCallerMethod attributeCaller;

  private List<Map<String, Object>> posts;
  private List<Map<String, Object>> paths;

  private JavadocCallerMethod postDocCaller;
  private JavadocCallerMethod pathDocCaller;

  private SiblingCallerMethod postSiblingCaller;
  private SiblingCallerMethod pathSiblingCaller;

  @Override
  public void setUp() throws Exception {

    super.setUp();

    Map<String, Object> data = buildModel("D");

    childrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) data.get("@Path")).get("children");
    attributeCaller = (AttributeCallerMethod) ((Map<String, Object>) data.get("@Path")).get("attribute");

    posts = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@POST"));
    paths = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@Path"));

    postDocCaller = (JavadocCallerMethod) posts.get(0).get("doc");
    pathDocCaller = (JavadocCallerMethod) paths.get(0).get("doc");

    postSiblingCaller = (SiblingCallerMethod) posts.get(0).get("sibling");
    pathSiblingCaller = (SiblingCallerMethod) paths.get(0).get("sibling");

  }

  public void testExists() throws Exception {

    assertNotNull(childrenCaller.exec(Arrays.asList("@POST")));
    assertNotNull(childrenCaller.exec(Arrays.asList("@Path")));

  }

  public void testElementName() throws Exception {

    assertEquals("m", posts.get(0).get("elementName"));
    assertEquals("m", paths.get(0).get("elementName"));

  }

  public void testAnnotationName() throws Exception {

    assertEquals("POST", posts.get(0).get("name"));
    assertEquals("Path", paths.get(0).get("name"));

  }

  public void testTypeName() throws Exception {

    Map<String, Object> fData = buildModel("F");

    ChildrenCallerMethod dChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) fData.get("@Path")).get("children");

    List<Map<String, Object>> dGets = (List<Map<String, Object>>) dChildrenCaller.exec(Arrays.asList("@GET"));
    List<Map<String, Object>> dPaths = (List<Map<String, Object>>) dChildrenCaller.exec(Arrays.asList("@Path"));

    assertEquals("Response", ((Map<String, Object>) dGets.get(0).get("type")).get("name"));
    assertEquals("Response", ((Map<String, Object>) dPaths.get(1).get("type")).get("name"));

    assertEquals("javax.ws.rs.core.Response", ((Map<String, Object>) dGets.get(0).get("type")).get("fullName"));
    assertEquals("javax.ws.rs.core.Response", ((Map<String, Object>) dPaths.get(1).get("type")).get("fullName"));

    assertEquals("false", ((Map<String, Object>) dGets.get(0).get("type")).get("isArray"));
    assertEquals("false", ((Map<String, Object>) dPaths.get(1).get("type")).get("isArray"));

    Map<String, Object> gData = buildModel("G");

    ChildrenCallerMethod gChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) gData.get("@Path")).get("children");

    List<Map<String, Object>> gGets = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@GET"));
    List<Map<String, Object>> gPaths = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@Path"));

    assertEquals("Response[]", ((Map<String, Object>) gGets.get(0).get("type")).get("name"));
    assertEquals("Response[]", ((Map<String, Object>) gPaths.get(0).get("type")).get("name"));

    assertEquals("javax.ws.rs.core.Response[]", ((Map<String, Object>) gGets.get(0).get("type")).get("fullName"));
    assertEquals("javax.ws.rs.core.Response[]", ((Map<String, Object>) gPaths.get(0).get("type")).get("fullName"));

    assertEquals("true", ((Map<String, Object>) gGets.get(0).get("type")).get("isArray"));
    assertEquals("true", ((Map<String, Object>) gPaths.get(0).get("type")).get("isArray"));

  }

  public void testTypeNotIsArray() throws Exception {

    Map<String, Object> fData = buildModel("F");

    ChildrenCallerMethod fChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) fData.get("@Path")).get("children");

    List<Map<String, Object>> fGets = (List<Map<String, Object>>) fChildrenCaller.exec(Arrays.asList("@GET"));
    List<Map<String, Object>> fPaths = (List<Map<String, Object>>) fChildrenCaller.exec(Arrays.asList("@Path"));

    assertEquals("false", ((Map<String, Object>) fGets.get(0).get("type")).get("isArray"));
    assertEquals("false", ((Map<String, Object>) fPaths.get(1).get("type")).get("isArray"));

  }

  public void testTypeIsArray() throws Exception {

    Map<String, Object> gData = buildModel("G");

    ChildrenCallerMethod gChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) gData.get("@Path")).get("children");

    List<Map<String, Object>> gGets = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@GET"));
    List<Map<String, Object>> gPaths = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@Path"));

    assertEquals("true", ((Map<String, Object>) gGets.get(0).get("type")).get("isArray"));
    assertEquals("true", ((Map<String, Object>) gPaths.get(0).get("type")).get("isArray"));

  }

  public void testTypeVoidName() throws Exception {

    assertEquals("", ((Map<String, Object>) posts.get(0).get("type")).get("name"));
    assertEquals("", ((Map<String, Object>) paths.get(0).get("type")).get("name"));

    assertEquals("", ((Map<String, Object>) posts.get(0).get("type")).get("fullName"));
    assertEquals("", ((Map<String, Object>) paths.get(0).get("type")).get("fullName"));

    assertEquals("false", ((Map<String, Object>) posts.get(0).get("type")).get("isArray"));
    assertEquals("false", ((Map<String, Object>) paths.get(0).get("type")).get("isArray"));

  }

  public void testJavadocGeneralComment() throws Exception {

    assertEquals("General comment.", postDocCaller.exec(new ArrayList()).toString());
    assertEquals("General comment.", pathDocCaller.exec(new ArrayList()).toString());

  }

  public void testJavadocSingleValue() throws Exception {

    assertEquals("[1.0]", postDocCaller.exec(Arrays.asList("since")).toString());
    assertEquals("[1.0]", pathDocCaller.exec(Arrays.asList("since")).toString());

  }

  public void testJavadocMultipleValue() throws Exception {

    assertEquals("[foo, bar]", postDocCaller.exec(Arrays.asList("author")).toString());
    assertEquals("[foo, bar]", pathDocCaller.exec(Arrays.asList("author")).toString());

  }

  public void testJavadocNoValue() throws Exception {

    assertEquals("[deprecated]", postDocCaller.exec(Arrays.asList("deprecated")).toString());
    assertEquals("[deprecated]", pathDocCaller.exec(Arrays.asList("deprecated")).toString());

  }

  public void testJavadocDoesntExist() throws Exception {

    assertEquals("", postDocCaller.exec(Arrays.asList("foo")).toString());
    assertEquals("", pathDocCaller.exec(Arrays.asList("foo")).toString());

  }

  public void testJavadocSingleListValue() throws Exception {

    assertEquals("[1.0]", postDocCaller.exec(Arrays.asList("list:since")).toString());
    assertEquals("[1.0]", pathDocCaller.exec(Arrays.asList("list:since")).toString());

  }

  public void testJavadocMultipleListValue() throws Exception {

    assertEquals("[foo, bar]", postDocCaller.exec(Arrays.asList("list:author")).toString());
    assertEquals("[foo, bar]", pathDocCaller.exec(Arrays.asList("list:author")).toString());

  }

  public void testJavadocListNoValue() throws Exception {

    assertEquals("[deprecated]", postDocCaller.exec(Arrays.asList("list:deprecated")).toString());
    assertEquals("[deprecated]", pathDocCaller.exec(Arrays.asList("list:deprecated")).toString());

  }

  public void testJavadocListDoesntExist() throws Exception {

    assertEquals("[]", postDocCaller.exec(Arrays.asList("list:foo")).toString());
    assertEquals("[]", pathDocCaller.exec(Arrays.asList("list:foo")).toString());

  }

  public void testJavadocSingleFlatValue() throws Exception {

    assertEquals("1.0", postDocCaller.exec(Arrays.asList("flat:since")).toString());
    assertEquals("1.0", pathDocCaller.exec(Arrays.asList("flat:since")).toString());

  }

  public void testJavadocMultipleFlatValue() throws Exception {

    assertEquals("foo, bar", postDocCaller.exec(Arrays.asList("flat:author")).toString());
    assertEquals("foo, bar", pathDocCaller.exec(Arrays.asList("flat:author")).toString());

  }

  public void testJavadocFlatNoValue() throws Exception {

    assertEquals("deprecated", postDocCaller.exec(Arrays.asList("flat:deprecated")).toString());
    assertEquals("deprecated", pathDocCaller.exec(Arrays.asList("flat:deprecated")).toString());

  }

  public void testJavadocFlatDoesntExist() throws Exception {

    assertEquals("", postDocCaller.exec(Arrays.asList("flat:foo")).toString());
    assertEquals("", pathDocCaller.exec(Arrays.asList("flat:foo")).toString());

  }

  /*public void testSibling() throws Exception {

    assertEquals(paths.get(0), postSiblingCaller.exec(Arrays.asList("@Path")));
    assertEquals(posts.get(0), pathSiblingCaller.exec(Arrays.asList("@POST")));

  }*/
  
}