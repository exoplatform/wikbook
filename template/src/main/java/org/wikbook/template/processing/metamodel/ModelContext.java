package org.wikbook.template.processing.metamodel;

import javax.lang.model.util.Elements;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class ModelContext {

  private TemplateAnnotation currentAnnotation;
  private Class[] classes;
  private Elements utils;

  public Class[] getClasses() {
    return classes;
  }

  public void setClasses(Class[] classes) {
    this.classes = classes;
  }

  public Elements getUtils() {
    return utils;
  }

  public void setUtils(Elements utils) {
    this.utils = utils;
  }

  public TemplateAnnotation getCurrentAnnotation() {
    return currentAnnotation;
  }

  public void setCurrentAnnotation(TemplateAnnotation currentAnnotation) {
    this.currentAnnotation = currentAnnotation;
  }
  
}
