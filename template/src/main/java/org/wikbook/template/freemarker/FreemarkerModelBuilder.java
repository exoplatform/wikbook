package org.wikbook.template.freemarker;

import org.wikbook.template.freemarker.caller.AttributeCallerMethod;
import org.wikbook.template.freemarker.caller.ChildrenCallerMethod;
import org.wikbook.template.freemarker.caller.JavadocCallerMethod;
import org.wikbook.template.freemarker.caller.SiblingCallerMethod;
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

  public Map<String, Object> build(MetaModel model) {

    Map<String, Object> root = new HashMap<String, Object>();

    for (TemplateElement el : model.getElements()) {

      for (TemplateAnnotation annotation : el.getAnnotations().values()) {

        //
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("attribute", new AttributeCallerMethod(annotation.getValues()));
        data.put("doc", new JavadocCallerMethod(annotation.getJavadoc()));
        data.put("children", new ChildrenCallerMethod(annotation.getChildren()));
        data.put("sibling", new SiblingCallerMethod(annotation.getElement()));
        data.put("elementName", annotation.getElement().getName());
        data.put("name", annotation.getName().substring(1));

        //
        root.put(annotation.getName(), data);

      }

    }

    return root;

  }
  
}
