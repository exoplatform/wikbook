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
public class Annotation implements Serializable {

  private String name;
  private Map<String, String> simpleValues;
  private List<Annotation> annotationsValues;

  public Annotation(String name) {
    this.name = "@" + name;
    this.simpleValues = new HashMap<String, String>();
    this.annotationsValues = new ArrayList<Annotation>();
  }

  public String getName() {
    return name;
  }

  public void simpleValue(String name, String value) {
    simpleValues.put(name, value);
  }

  public Map<String, ? extends Object> simpleValues() {
    return simpleValues;
  }

  public void add(Annotation annotation) {
    this.annotationsValues.add(annotation);
  }

  public List<Annotation> getAnnotationsValues() {
    return annotationsValues;
  }
}
