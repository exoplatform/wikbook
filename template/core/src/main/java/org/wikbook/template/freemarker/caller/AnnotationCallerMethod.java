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

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.wikbook.template.freemarker.FreemarkerDataFactory;
import org.wikbook.template.processing.ProcessingUtils;
import org.wikbook.template.processing.TemplateElementVisitor;
import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.TemplateElement;
import org.wikbook.template.processing.metamodel.TemplateType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class AnnotationCallerMethod implements TemplateMethodModel {

  private TemplateType type;
  private FreemarkerDataFactory dataFactory;

  public AnnotationCallerMethod(final FreemarkerDataFactory dataFactory, final TemplateType type) {
    this.type = type;
    this.dataFactory = dataFactory;
  }

  public Object exec(final List arguments) throws TemplateModelException {

    for (TemplateElement element : dataFactory.getModel().getElements()) {
      if (element.getName().equals(type.getName())) {

        TemplateAnnotation annotation = element.getAnnotation((String) arguments.get(0));

        if (annotation != null) {
          return dataFactory.create(annotation);
        }
        
      }
    }

    // Try runtime
    String fqn = type.getFqn();
    try {
      Class c = Class.forName(fqn);
      for (Annotation a : c.getAnnotations()) {
        String name = "@" + a.annotationType().getSimpleName();
        if (name.equals(arguments.get(0))) {
          return dataFactory.create(ProcessingUtils.createAnnotation(a));
        }
      }
    }
    catch (ClassNotFoundException e) {
      // Not found
    }

    return null;

  }

}
