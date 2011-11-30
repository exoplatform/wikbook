package org.wikbook.template.freemarker;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class JavadocCallerMethod implements TemplateMethodModel {

  private Map<String, List<String>> details = new HashMap<String, List<String>>();

  public JavadocCallerMethod(Map<String, List<String>> details) {
    this.details = details;
  }

  public Object exec(List list) throws TemplateModelException {

    switch (list.size()) {
      case 0:
        return get(details.get(null));
      case 1:
        return get(details.get(list.get(0)));
      default:
        throw new RuntimeException("Cannot have many names");

    }

  }

  private Object get(List<String> values) {

    if (values != null) {
      if (values.size() == 1) {
        return new SimpleScalar(values.get(0));
      }
      else if (values.size() > 1) {
        return new SimpleScalar(asString(values));
      }
    }

    return "";

  }

  private String asString(List<String> l) {

    StringBuilder sb = new StringBuilder();
    for (String v : l) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(v);
    }

    return sb.toString();

  }

}
