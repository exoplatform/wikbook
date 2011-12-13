package org.wikbook.template.processing.metamodel;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class ModelContext {

  private TemplateElement typeElement;
  private TemplateElement executableElement;
  private TemplateElement variableElement;
  private Class[] annotations;
  private Elements elementsUtils;
  private Types typesUtils;

  public Class[] getAnnotations() {
    return annotations;
  }

  public void setClasses(Class[] classes) {
    this.annotations = classes;
  }

  public Elements getElementsUtils() {
    return elementsUtils;
  }

  public void setElementsUtils(final Elements elementsUtils) {
    this.elementsUtils = elementsUtils;
  }

  public Types getTypesUtils() {
    return typesUtils;
  }

  public void setTypesUtils(final Types typesUtils) {
    this.typesUtils = typesUtils;
  }

  public TemplateElement getTypeElement() {
    return typeElement;
  }

  public void setTypeElement(final TemplateElement typeElement) {
    this.typeElement = typeElement;
  }

  public TemplateElement getExecutableElement() {
    return executableElement;
  }

  public void setExecutableElement(final TemplateElement executableElement) {
    this.executableElement = executableElement;
  }

  public TemplateElement getVariableElement() {
    return variableElement;
  }

  public void setVariableElement(final TemplateElement variableElement) {
    this.variableElement = variableElement;
  }
  
}
