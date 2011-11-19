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

      //
      Map<String, Object> data = new HashMap<String, Object>();
      data.putAll(annotation.simpleValues());
      data.put("@", new JavadocCallerMethod(annotation.javadocValues()));

      //
      root.put(annotation.getName(), data);

      //
      List<Map<String, ? extends Object>> child = new ArrayList<Map<String, ? extends Object>>();
      for (Annotation annotationChild : annotation.getAnnotationsValues()) {
        child.add(annotationChild.simpleValues());
      }
      ((Map<String, Object>)root.get(annotation.getName())).put("@Path", child);
    }

    return root;

  }
  
}
