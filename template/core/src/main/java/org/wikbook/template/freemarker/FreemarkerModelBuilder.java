package org.wikbook.template.freemarker;

import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.TemplateElement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FreemarkerModelBuilder {

  public Map<String, Object> build(MetaModel model, TemplateElement element) {

    FreemarkerDataFactory handler = new FreemarkerDataFactory(model);
    Map<String, Object> root = new HashMap<String, Object>();

    for (TemplateAnnotation annotation : element.getAnnotations().values()) {

      Map<String, Object> data = handler.create(annotation);
      root.put(annotation.getName(), data);

    }

    return root;

  }
  
}
