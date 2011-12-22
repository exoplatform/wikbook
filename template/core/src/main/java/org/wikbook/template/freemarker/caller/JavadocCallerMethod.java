package org.wikbook.template.freemarker.caller;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.wikbook.template.freemarker.TemplateExpression;

import java.util.ArrayList;
import java.util.Collections;
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
      
      // General javadoc
      case 0:
        return perform(details.get(null));

      // Keyed javadoc
      case 1:
        TemplateExpression eh = new TemplateExpression((String) list.get(0));
        return perform(details.get(eh.getValue()), eh);

      // Error
      default:
        throw new RuntimeException("Cannot have many names");

    }

  }
  
  private Object perform(List<List<String>> values) {

    return perform(values, TemplateExpression.noExpression());
    
  }

  private Object perform(List<List<String>> values, TemplateExpression e) {

    if (values != null) {

      List<String> builtDoc = buildDocValue(values, e.getOutput().equals(TemplateExpression.Output.BLOC));

      //
      switch (e.getOutput()) {

        case LIST:
        case NONE:
          return builtDoc;

        case FLAT:
        case NOEXPR:
          return e.flatStringList(builtDoc).trim();

        case BLOC:
          return e.flatStringList(builtDoc);

      }

    }

    //
    if (e.getOutput().equals(TemplateExpression.Output.LIST)) {
      return Collections.emptyList();
    }

    //
    return "";

  }

  private List<String> buildDocValue(List<List<String>> data, boolean isBloc) {

    List<String> c = new ArrayList<String>();
    for (List<String> lv : data) {
      StringBuffer sb = new StringBuffer();
      for (String v : lv) {
        if (isBloc) {
          if (sb.length() > 0) {
            sb.append("\n");
          }
          sb.append(v);
        }
        else {
          sb.append(v);
        }
      }
      c.add(sb.toString());
    }

    return c;
    
  }

}
