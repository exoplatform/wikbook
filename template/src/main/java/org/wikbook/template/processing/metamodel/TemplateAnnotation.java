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
public class TemplateAnnotation implements Serializable {

  private String name;
  private Map<String, Object> values;
  private Map<String, List<String>> javadoc;
  private List<TemplateAnnotation> childs;
  private TemplateElement element;

  public TemplateAnnotation(String name, TemplateElement element) {
    this.name = "@" + name;
    this.element = element;
    this.values = new HashMap<String, Object>();
    this.javadoc = new HashMap<String, List<String>>();
    this.childs = new ArrayList<TemplateAnnotation>();
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

  public void addJavadoc(String name, List<String> value) {
    javadoc.put(name, value);
  }

  public Map<String, List<String>> getJavadoc() {
    return javadoc;
  }

  public List<String> getJavadoc(String name) {
    List<String> got = javadoc.get(name);
    if (got == null) {
      javadoc.put(name, got = new ArrayList<String>());
    }
    return got;
  }

  public void addChild(TemplateAnnotation annotation) {
    this.childs.add(annotation);
  }

  public List<TemplateAnnotation> getChildren() {
    return childs;
  }

  public TemplateElement getElement() {
    return element;
  }
  
}
