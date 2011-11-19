package org.wikbook.template.processing.metamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class MetaModel implements Serializable {

  private List<Annotation> annotations;

  public MetaModel() {
    this.annotations = new ArrayList<Annotation>();
  }

  public List<Annotation> getAnnotations() {
    return annotations;
  }

  public void add(Annotation annotation) {
    this.annotations.add(annotation);
  }
  
}
