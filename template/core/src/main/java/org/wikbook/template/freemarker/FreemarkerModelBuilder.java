package org.wikbook.template.freemarker;

import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.TemplateElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FreemarkerModelBuilder {

  public Map<String, ? extends Object> build(MetaModel model, TemplateElement element) {

    FreemarkerDataFactory builder = new FreemarkerDataFactory(model);
    Map<String, Object> root = new HashMap<String, Object>();

    for (TemplateAnnotation annotation : element.getAnnotations().values()) {

      Map<String, Object> data = builder.create(annotation);
      root.put(annotation.getName(), data);

    }

    return root;

  }

  public Map<String, ? extends Object> build(MetaModel model) {

    FreemarkerDataFactory builder = new FreemarkerDataFactory(model);
    Map<String, List<Object>> root = new HashMap<String, List<Object>>();

    for (TemplateElement element : model.getElements()) {

      for (TemplateAnnotation annotation : element.getAnnotations().values()) {

        List<Object> got = root.get(annotation.getName());
        if (got == null) {
          got = new ArrayList<Object>();
        }

        got.add(builder.create(annotation));
        root.put(annotation.getName(), got);

      }
      
    }

    return root;

  }
  
}
