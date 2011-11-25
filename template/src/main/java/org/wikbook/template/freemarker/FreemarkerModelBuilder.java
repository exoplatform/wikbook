package org.wikbook.template.freemarker;

import org.wikbook.template.processing.metamodel.Annotation;
import org.wikbook.template.processing.metamodel.MetaModel;

import java.util.HashMap;
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
      data.putAll(annotation.getValues());
      data.put("doc", new JavadocCallerMethod(annotation.getJavadoc()));
      data.put("children", new ChildrenCallerMethod(annotation.getChildren()));

      //
      root.put(annotation.getName(), data);
      
    }

    return root;

  }
  
}
