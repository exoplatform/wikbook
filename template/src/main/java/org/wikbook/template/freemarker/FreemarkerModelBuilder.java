package org.wikbook.template.freemarker;

import org.wikbook.template.processing.metamodel.Annotation;
import org.wikbook.template.processing.metamodel.MetaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FreemarkerModelBuilder {

  public Map<String, Object> build(MetaModel model) {

    Map<String, Object> root = new HashMap<String, Object>();

    for (Annotation annotation : model.getAnnotations()) {

      // Root data
      root.put(annotation.getName(), annotation.simpleValues());

      List<Map<String, ? extends Object>> child = new ArrayList<Map<String, ? extends Object>>();

      for (Annotation annotationChild : annotation.getAnnotationsValues()) {
        child.add(annotationChild.simpleValues());
      }
      ((Map<String, Object>)root.get(annotation.getName())).put("@Path", child);
    }

    return root;
  }

  private List<String> getValue(Map<String, Object> data, String parentName, String key) {

    List<String> l = ((Map<String, List<String>>)data.get(parentName)).get(key);

    if (l == null) {
      l = new ArrayList<String>();
      ((Map<String, List<String>>)data.get(parentName)).put(key, l);
    }

    return l;
  }
  
}
