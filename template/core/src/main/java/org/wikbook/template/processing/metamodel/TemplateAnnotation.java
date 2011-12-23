package org.wikbook.template.processing.metamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class TemplateAnnotation implements Serializable {

  private String name;
  private Map<String, Object> values;
  private TemplateElement element;

  public TemplateAnnotation(String name, TemplateElement element) {
    this.name = "@" + name;
    this.element = element;
    this.values = new HashMap<String, Object>();
  }

  public String getName() {
    return name;
  }

  public void addValue(String name, Object value) {
    values.put(name, value);
  }

  public Map<String, Object> getValues() {
    return values;
  }

  public List<TemplateElement> getChildren() {

    if (element == null) {
      return Collections.emptyList();
    }

    return element.getElement();
  }

  public TemplateElement getElement() {
    return element;
  }

  public void addJavadoc(String name, List<List<String>> value) {

    if (element == null) {
      return;
    }
    
    element.addJavadoc(name, value);

  }

  public Map<String, List<List<String>>> getJavadoc() {

    if (element == null) {
      return Collections.emptyMap();
    }

    return element.getJavadoc();

  }

  public List<List<String>> getJavadoc(String name) {

    if (element == null) {
      return Collections.emptyList();
    }

    return element.getJavadoc(name);

  }
  
}
