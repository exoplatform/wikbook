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

  public static final String ATTRIBUTE = "attribute";
  public static final String JAVADOC = "doc";
  public static final String CHILDREN = "children";
  public static final String SIBLING = "sibling";
  public static final String ELEMENT_NAME = "elementName";
  public static final String ANNOTATION_NAME = "name";
  public static final String TYPE = "type";

  public static final String NAME = "name";
  public static final String FQN = "fqn";
  public static final String IS_ARRAY = "isArray";
  public static final String ANNOTATION = "annotation";

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

    data.put(ATTRIBUTE, new AttributeCallerMethod(this, annotation.getValues()));
    data.put(JAVADOC, new JavadocCallerMethod(annotation.getJavadoc()));
    data.put(CHILDREN, new ChildrenCallerMethod(this, annotation.getChildren()));
    data.put(SIBLING, new SiblingCallerMethod(this, annotation.getElement()));
    data.put(ELEMENT_NAME, annotation.getElement().getName());
    data.put(ANNOTATION_NAME, annotation.getName().substring(1));
    data.put(TYPE, dataType);

    dataType.put(NAME, annotation.getElement().getType().getName());
    dataType.put(FQN, annotation.getElement().getType().getFqn());
    dataType.put(IS_ARRAY, annotation.getElement().getType().isArray().toString());
    dataType.put(ANNOTATION, new AnnotationCallerMethod(this, annotation.getElement().getType()));

    return data;
    
  }

  public MetaModel getModel() {
    return model;
  }
  
}
