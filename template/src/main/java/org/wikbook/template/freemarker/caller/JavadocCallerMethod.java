package org.wikbook.template.freemarker.caller;

import freemarker.ext.beans.ArrayModel;
import freemarker.ext.beans.CollectionModel;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import org.wikbook.template.freemarker.ExpressionHandler;

import java.util.Arrays;
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
        ExpressionHandler eh = new ExpressionHandler();
      return get(details.get(null), eh);
      case 1:
        ExpressionHandler eh2 = new ExpressionHandler((String) list.get(0));
        return get(details.get(eh2.getValue()), eh2);
      default:
        throw new RuntimeException("Cannot have many names");

    }

  }

  private Object get(List<String> values, ExpressionHandler eh) {

    if (values != null) {

      //
      switch (eh.getOutput()) {

        case FLAT:
          return new SimpleScalar(asString(values));

        case LIST:
          return new CollectionModel(values, new DefaultObjectWrapper());

        case NONE:
          return new CollectionModel(values, new DefaultObjectWrapper());

        case NOEXPR:
          return new SimpleScalar(asString(values));

      }

    }

    //
    if (eh.getOutput().equals(ExpressionHandler.Output.LIST)) {
      return new CollectionModel(Arrays.asList(), new DefaultObjectWrapper());
    }

    //
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
