package org.wikbook.template.freemarker.caller;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.wikbook.template.freemarker.ExpressionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class JavadocCallerMethod implements TemplateMethodModel {

  private Map<String, List<List<String>>> details = new HashMap<String, List<List<String>>>();

  public JavadocCallerMethod(Map<String, List<List<String>>> details) {
    this.details = details;
  }

  public Object exec(List list) throws TemplateModelException {

    switch (list.size()) {
      case 0:
        ExpressionHandler eh = new ExpressionHandler();
      return eh.getJavadoc(details.get(null));
      case 1:
        ExpressionHandler eh2 = new ExpressionHandler((String) list.get(0));
        return eh2.getJavadoc(details.get(eh2.getValue()));
      default:
        throw new RuntimeException("Cannot have many names");

    }

  }

}
