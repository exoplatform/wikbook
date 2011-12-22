/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.wikbook.template.freemarker;

import org.wikbook.template.freemarker.caller.AnnotationCallerMethod;
import org.wikbook.template.freemarker.caller.AttributeCallerMethod;
import org.wikbook.template.freemarker.caller.ChildrenCallerMethod;
import org.wikbook.template.freemarker.caller.JavadocCallerMethod;
import org.wikbook.template.freemarker.caller.SiblingCallerMethod;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.TemplateAnnotation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FreemarkerDataFactory {

  private final MetaModel model;

  public FreemarkerDataFactory(final MetaModel model) {

    if (model == null) {
      throw new NullPointerException();
    }
    this.model = model;
    
  }

  public Map<String, Object> create(final TemplateAnnotation annotation) {

    //
    Map<String, Object> data = new HashMap<String, Object>();
    Map<String, Object> dataType = new HashMap<String, Object>();

    data.put("attribute", new AttributeCallerMethod(this, annotation.getValues()));
    data.put("doc", new JavadocCallerMethod(annotation.getJavadoc()));
    data.put("children", new ChildrenCallerMethod(this, annotation.getChildren()));
    data.put("sibling", new SiblingCallerMethod(this, annotation.getElement()));
    data.put("elementName", annotation.getElement().getName());
    data.put("name", annotation.getName().substring(1));
    data.put("type", dataType);

    dataType.put("name", annotation.getElement().getType().getName());
    dataType.put("fqn", annotation.getElement().getType().getFqn());
    dataType.put("isArray", annotation.getElement().getType().isArray().toString());
    dataType.put("annotation", new AnnotationCallerMethod(this, annotation.getElement().getType()));

    return data;
    
  }

  public MetaModel getModel() {
    return model;
  }
  
}
