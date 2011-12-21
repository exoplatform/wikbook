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

import freemarker.ext.beans.CollectionModel;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.wikbook.template.freemarker.ExpressionHandler;
import org.wikbook.template.freemarker.MemberHandler;
import org.wikbook.template.processing.metamodel.TemplateAnnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class AttributeCallerMethod implements TemplateMethodModel {

  private Map<String, Object> attributes;
  private MemberHandler handler;

  public AttributeCallerMethod(final MemberHandler handler,  final Map<String, Object> attributes) {
    this.attributes = attributes;
    this.handler = handler;
  }

  public Object exec(final List arguments) throws TemplateModelException {

    ExpressionHandler eh = new ExpressionHandler((String) arguments.get(0));
    Object got = attributes.get(eh.getValue());
    if (got != null) {

      Class valuesClass = got.getClass();

      // Array
      if (valuesClass.isArray()) {

        // Annotation[]
        if (TemplateAnnotation.class.equals(valuesClass.getComponentType())) {

          List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();

          for (TemplateAnnotation annotation : (TemplateAnnotation[]) got) {
            l.add(handler.handle(annotation));
          }

          return l;

          //return new CollectionModel(l, new DefaultObjectWrapper());
          
        }

        // Object[]
        else {
          return eh.getAttribute(Arrays.asList((String[])got));
        }

      }

      // Single value
      else {

        // Annotation
        if (got instanceof TemplateAnnotation) {
          return handler.handle((TemplateAnnotation) got);
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

}
