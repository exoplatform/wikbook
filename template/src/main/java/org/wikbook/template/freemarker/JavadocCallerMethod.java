package org.wikbook.template.freemarker;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class JavadocCallerMethod implements TemplateMethodModel {

  private Map<String, String> details = new HashMap<String, String>();

  public JavadocCallerMethod(Map<String, String> details) {
    this.details = details;
  }

  public Object exec(List list) throws TemplateModelException {

    switch (list.size()) {
      case 0:
        return new SimpleScalar(details.get(null));
      case 1:
        return new SimpleScalar(details.get(list.get(0)));
      default:
        throw new RuntimeException("Cannot have many names");

    }

  }
}
