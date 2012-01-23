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
import org.wikbook.template.processing.metamodel.TemplateType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
  public static final String PARAMETER = "parameter";

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

    //
    data.put(ATTRIBUTE, new AttributeCallerMethod(this, annotation.getValues()));
    data.put(JAVADOC, new JavadocCallerMethod(annotation.getJavadoc()));
    data.put(CHILDREN, new ChildrenCallerMethod(this, annotation.getChildren()));
    data.put(SIBLING, new SiblingCallerMethod(this, annotation.getElement()));
    data.put(ANNOTATION_NAME, annotation.getName().substring(1));

    if (annotation.getElement() != null) {
      data.put(ELEMENT_NAME, annotation.getElement().getName());

      if (annotation.getElement().getType() != null) {
        data.put(TYPE, createTypeData(annotation.getElement().getType()));
      }
      
    }

    //
    return data;
    
  }

  private Map<String, Object> createTypeData(final TemplateType type) {

    //
    Map<String, Object> dataType = new HashMap<String, Object>();
    List<Map<String, Object>> dataTypeParameters = new ArrayList<Map<String, Object>>();

    //
    dataType.put(NAME, type.getName());
    dataType.put(FQN, type.getFqn());
    dataType.put(IS_ARRAY, type.isArray().toString());
    dataType.put(ANNOTATION, new AnnotationCallerMethod(this, type));
    dataType.put(PARAMETER, dataTypeParameters);

    //
    for (TemplateType typeParameter : type.getParameters()) {
      dataTypeParameters.add(createTypeData(typeParameter));
    }

    //
    return dataType;

  }

  public MetaModel getModel() {
    return model;
  }
  
}
