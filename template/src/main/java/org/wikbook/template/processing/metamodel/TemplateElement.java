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

package org.wikbook.template.processing.metamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class TemplateElement implements Serializable {

  private String name;
  private Map<String, TemplateAnnotation> annotations;
  private List<TemplateElement> children;
  private Map<String, List<List<String>>> javadoc;

  public TemplateElement(final String name) {
    this.name = name;
    this.annotations = new HashMap<String, TemplateAnnotation>();
    this.children = new ArrayList<TemplateElement>();
    this.javadoc = new HashMap<String, List<List<String>>>();
  }

  public String getName() {
    return name;
  }

  public void addAnnotation(TemplateAnnotation annotation) {
    annotations.put(annotation.getName(), annotation);
  }

  public TemplateAnnotation getAnnotation(String name) {
    return annotations.get(name);
  }

  public Map<String, TemplateAnnotation> getAnnotations() {
    return annotations;
  }

  public void addElement(TemplateElement element) {
    children.add(element);
  }

  public List<TemplateElement> getElement() {
    return children;
  }

  public void addJavadoc(String name, List<List<String>> value) {
    javadoc.put(name, value);
  }

  public Map<String, List<List<String>>> getJavadoc() {
    return javadoc;
  }

  public List<List<String>> getJavadoc(String name) {
    List<List<String>> got = javadoc.get(name);
    if (got == null) {
      javadoc.put(name, got = new ArrayList<List<String>>());
    }
    return got;
  }
  
}
