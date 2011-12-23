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

package org.wikbook.template.processing;

import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.TemplateElement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class ProcessingUtils {

  public static TemplateAnnotation createAnnotation(Annotation a, TemplateElement element) {

    if (a == null) {
      return null;
    }

    Class clazz = a.annotationType();

    TemplateAnnotation annotation = new TemplateAnnotation(clazz.getSimpleName(), element);
    for (Method method : clazz.getMethods()) {
      if (method.getDeclaringClass().equals(clazz)) {
        try {

          Class type = method.getReturnType();

          // Annotation[]
          if (type.isArray() && type.getComponentType().isAnnotation()) {

            Annotation[] annotations = (Annotation[]) method.invoke(a);
            List<TemplateAnnotation> tAs = new ArrayList<TemplateAnnotation>();

            for (Annotation currentAnnotation : annotations) {
              TemplateAnnotation currentTA = createAnnotation(currentAnnotation, element);
              tAs.add(currentTA);
            }

            annotation.addValue(method.getName(), tAs.toArray(new TemplateAnnotation[]{}));

          }

          // Annotation
          else if (type.isAnnotation()) {
            TemplateAnnotation sub = createAnnotation((Annotation) method.invoke(a), element);
            annotation.addValue(method.getName(), sub);
          }

          // Object and Object[]
          else {
            annotation.addValue(method.getName(), method.invoke(a));
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    return annotation;

  }

  public static TemplateAnnotation createAnnotation(Annotation a) {
    return createAnnotation(a, null);
  }

}
