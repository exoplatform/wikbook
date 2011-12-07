package org.wikbook.template.processing.metamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class MetaModel implements Serializable {

  private List<TemplateElement> elements;

  public MetaModel() {
    this.elements = new ArrayList<TemplateElement>();
  }

  public List<TemplateElement> getElements() {
    return elements;
  }

  public void add(TemplateElement element) {
    this.elements.add(element);
  }
  
}
