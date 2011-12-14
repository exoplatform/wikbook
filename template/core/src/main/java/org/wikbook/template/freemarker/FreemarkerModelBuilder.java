package org.wikbook.template.freemarker;

import org.wikbook.template.freemarker.caller.AnnotationCallerMethod;
import org.wikbook.template.freemarker.caller.AttributeCallerMethod;
import org.wikbook.template.freemarker.caller.ChildrenCallerMethod;
import org.wikbook.template.freemarker.caller.JavadocCallerMethod;
import org.wikbook.template.freemarker.caller.SiblingCallerMethod;
import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.TemplateElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FreemarkerModelBuilder {

  public Map<String, Object> build(MetaModel model, TemplateElement element) {

    MemberHandler handler = new MemberHandler(model);
    Map<String, Object> root = new HashMap<String, Object>();

    for (TemplateAnnotation annotation : element.getAnnotations().values()) {

      Map<String, Object> data = handler.handle(annotation);
      root.put(annotation.getName(), data);

    }

    return root;

  }
  
}
