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

package org.wikbook.template.freemarker.caller;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.wikbook.template.freemarker.TemplateExpression;
import org.wikbook.template.freemarker.FreemarkerDataFactory;
import org.wikbook.template.processing.metamodel.TemplateAnnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class AttributeCallerMethod implements TemplateMethodModel {

  private Map<String, Object> attributes;
  private FreemarkerDataFactory dataFactory;

  public AttributeCallerMethod(final FreemarkerDataFactory dataFactory,  final Map<String, Object> attributes) {
    this.attributes = attributes;
    this.dataFactory = dataFactory;
  }

  public Object exec(final List arguments) throws TemplateModelException {

    TemplateExpression e = new TemplateExpression((String) arguments.get(0));
    Object got = attributes.get(e.getValue());
    if (got != null) {

      Class valuesClass = got.getClass();

      // Array
      if (valuesClass.isArray()) {

        // Annotation[]
        if (TemplateAnnotation.class.equals(valuesClass.getComponentType())) {

          List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();

          for (TemplateAnnotation annotation : (TemplateAnnotation[]) got) {
            l.add(dataFactory.create(annotation));
          }

          return l;
          
        }

        // Object[]
        else {

          return performArray(Arrays.asList((String[]) got), e);
          
        }

      }

      // Single value
      else {

        // Annotation
        if (got instanceof TemplateAnnotation) {
          return dataFactory.create((TemplateAnnotation) got);
        }

        // Object
        else {
          return new SimpleScalar(got.toString());
        }
      }

    }
    else {
      return null;
    }

  }

  private Object performArray(List<String> data, TemplateExpression e) {

    //
    switch (e.getOutput()) {

        case LIST:
        case NONE:
          return data;

        case FLAT:
        case BLOC:
        case NOEXPR:
          return e.flatStringList(data);

      }

    //
    if (e.getOutput().equals(TemplateExpression.Output.LIST)) {
      return Collections.emptyList();
    }

    //
    return "";

  }

}
