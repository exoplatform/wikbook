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

  private Map<String, Object> siblingPosts;
  private Map<String, Object> siblingPaths;

  @Override
  public void setUp() throws Exception {

    super.setUp();

    Map<String, Object> data = buildModel("D");

    childrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) data.get("@AnnotationA")).get("children");
    attributeCaller = (AttributeCallerMethod) ((Map<String, Object>) data.get("@AnnotationA")).get("attribute");

    posts = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationC"));
    paths = (List<Map<String, Object>>) childrenCaller.exec(Arrays.asList("@AnnotationA"));

    postDocCaller = (JavadocCallerMethod) posts.get(0).get("doc");
    pathDocCaller = (JavadocCallerMethod) paths.get(0).get("doc");

    postSiblingCaller = (SiblingCallerMethod) posts.get(0).get("sibling");
    pathSiblingCaller = (SiblingCallerMethod) paths.get(0).get("sibling");

    siblingPaths = (Map<String, Object>) postSiblingCaller.exec(Arrays.asList("@AnnotationA"));
    siblingPosts = (Map<String, Object>) postSiblingCaller.exec(Arrays.asList("@AnnotationC"));

  }

  public void testExists() throws Exception {

    assertNotNull(childrenCaller.exec(Arrays.asList("@AnnotationC")));
    assertNotNull(childrenCaller.exec(Arrays.asList("@AnnotationA")));

  }

  public void testElementName() throws Exception {

    assertEquals("m", posts.get(0).get("elementName"));
    assertEquals("m", paths.get(0).get("elementName"));

  }

  public void testAnnotationName() throws Exception {

    assertEquals("AnnotationC", posts.get(0).get("name"));
    assertEquals("AnnotationA", paths.get(0).get("name"));

  }

  public void testTypeName() throws Exception {

    Map<String, Object> fData = buildModel("F");

    ChildrenCallerMethod dChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) fData.get("@AnnotationA")).get("children");

    List<Map<String, Object>> dGets = (List<Map<String, Object>>) dChildrenCaller.exec(Arrays.asList("@AnnotationD"));
    List<Map<String, Object>> dPaths = (List<Map<String, Object>>) dChildrenCaller.exec(Arrays.asList("@AnnotationA"));

    assertEquals("String", ((Map<String, Object>) dGets.get(0).get("type")).get("name"));
    assertEquals("String", ((Map<String, Object>) dPaths.get(1).get("type")).get("name"));

    assertEquals("java.lang.String", ((Map<String, Object>) dGets.get(0).get("type")).get("fqn"));
    assertEquals("java.lang.String", ((Map<String, Object>) dPaths.get(1).get("type")).get("fqn"));

    assertEquals("false", ((Map<String, Object>) dGets.get(0).get("type")).get("isArray"));
    assertEquals("false", ((Map<String, Object>) dPaths.get(1).get("type")).get("isArray"));

    Map<String, Object> gData = buildModel("G");

    ChildrenCallerMethod gChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) gData.get("@AnnotationA")).get("children");

    List<Map<String, Object>> gGets = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@AnnotationC"));
    List<Map<String, Object>> gPaths = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@AnnotationA"));

    assertEquals("String[]", ((Map<String, Object>) gGets.get(0).get("type")).get("name"));
    assertEquals("String[]", ((Map<String, Object>) gPaths.get(0).get("type")).get("name"));

    assertEquals("java.lang.String[]", ((Map<String, Object>) gGets.get(0).get("type")).get("fqn"));
    assertEquals("java.lang.String[]", ((Map<String, Object>) gPaths.get(0).get("type")).get("fqn"));

    assertEquals("true", ((Map<String, Object>) gGets.get(0).get("type")).get("isArray"));
    assertEquals("true", ((Map<String, Object>) gPaths.get(0).get("type")).get("isArray"));

  }

  public void testTypeNotIsArray() throws Exception {

    Map<String, Object> fData = buildModel("F");

    ChildrenCallerMethod fChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) fData.get("@AnnotationA")).get("children");

    List<Map<String, Object>> fGets = (List<Map<String, Object>>) fChildrenCaller.exec(Arrays.asList("@AnnotationD"));
    List<Map<String, Object>> fPaths = (List<Map<String, Object>>) fChildrenCaller.exec(Arrays.asList("@AnnotationA"));

    assertEquals("false", ((Map<String, Object>) fGets.get(0).get("type")).get("isArray"));
    assertEquals("false", ((Map<String, Object>) fPaths.get(1).get("type")).get("isArray"));

  }

  public void testTypeIsArray() throws Exception {

    Map<String, Object> gData = buildModel("G");

    ChildrenCallerMethod gChildrenCaller = (ChildrenCallerMethod) ((Map<String, Object>) gData.get("@AnnotationA")).get("children");

    List<Map<String, Object>> gGets = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@AnnotationC"));
    List<Map<String, Object>> gPaths = (List<Map<String, Object>>) gChildrenCaller.exec(Arrays.asList("@AnnotationA"));

    assertEquals("true", ((Map<String, Object>) gGets.get(0).get("type")).get("isArray"));
    assertEquals("true", ((Map<String, Object>) gPaths.get(0).get("type")).get("isArray"));

  }

  public void testTypeVoidName() throws Exception {

    assertEquals("", ((Map<String, Object>) posts.get(0).get("type")).get("name"));
    assertEquals("", ((Map<String, Object>) paths.get(0).get("type")).get("name"));

    assertEquals("", ((Map<String, Object>) posts.get(0).get("type")).get("fqn"));
    assertEquals("", ((Map<String, Object>) paths.get(0).get("type")).get("fqn"));

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

  public void testSiblingName() throws Exception {

    assertEquals(paths.get(0).get("name"), siblingPaths.get("name"));
    assertEquals(posts.get(0).get("name"), siblingPosts.get("name"));

  }

  public void testSiblingElementName() throws Exception {

    assertEquals(paths.get(0).get("elementName"), siblingPaths.get("elementName"));
    assertEquals(posts.get(0).get("elementName"), siblingPosts.get("elementName"));

  }

  public void testSiblingTypeName() throws Exception {

    assertEquals(paths.get(0).get("typeName"), siblingPaths.get("typeName"));
    assertEquals(posts.get(0).get("typeName"), siblingPosts.get("typeName"));

  }

  public void testSiblingTypeFullName() throws Exception {

    assertEquals(paths.get(0).get("fqn"), siblingPaths.get("fqn"));
    assertEquals(posts.get(0).get("fqn"), siblingPosts.get("fqn"));

  }

  public void testSiblingTypeIsArray() throws Exception {

    assertEquals(paths.get(0).get("isArray"), siblingPaths.get("isArray"));
    assertEquals(posts.get(0).get("isArray"), siblingPosts.get("isArray"));

  }

  public void testSiblingDoc() throws Exception {

    assertEquals(pathDocCaller.exec(Arrays.asList("since")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("since")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("since")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("since")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("author")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("author")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("author")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("author")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("deprecated")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("deprecated")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("deprecated")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("deprecated")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("foo")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("foo")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("foo")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("foo")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("list:since")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("list:since")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("list:since")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("list:since")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("list:author")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("list:author")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("list:author")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("list:author")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("list:deprecated")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("list:deprecated")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("list:deprecated")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("list:deprecated")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("list:foo")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("list:foo")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("list:foo")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("list:foo")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("flat:since")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("flat:since")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("flat:since")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("flat:since")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("flat:author")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("flat:author")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("flat:author")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("flat:author")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("flat:deprecated")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("flat:deprecated")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("flat:deprecated")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("flat:deprecated")).toString());

    assertEquals(pathDocCaller.exec(Arrays.asList("flat:foo")).toString(), ((JavadocCallerMethod) siblingPaths.get("doc")).exec(Arrays.asList("flat:foo")).toString());
    assertEquals(postDocCaller.exec(Arrays.asList("flat:foo")).toString(), ((JavadocCallerMethod) siblingPosts.get("doc")).exec(Arrays.asList("flat:foo")).toString());

  }

  public void testSiblingAttribute() throws Exception {

    assertEquals(
        ((AttributeCallerMethod) paths.get(0).get("attribute")).exec(Arrays.asList("value")).toString(),
        ((AttributeCallerMethod) siblingPaths.get("attribute")).exec(Arrays.asList("value")).toString()
    );

  }
  
}