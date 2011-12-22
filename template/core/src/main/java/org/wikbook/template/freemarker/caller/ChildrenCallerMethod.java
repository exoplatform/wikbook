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
import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.TemplateElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class ChildrenCallerMethod implements TemplateMethodModel {

  private List<TemplateElement> children;
  private FreemarkerDataFactory dataFactory;

  public ChildrenCallerMethod(final FreemarkerDataFactory dataFactory, final List<TemplateElement> children) {
    this.children = children;
    this.dataFactory = dataFactory;
  }

  public Object exec(final List arguments) throws TemplateModelException {

    List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();

    for(TemplateElement child : children) {
      for (String argument : (List<String>) arguments) {

        TemplateAnnotation childAnnotation = child.getAnnotation(argument);

        if (childAnnotation != null) {

          Map<String, Object> data = dataFactory.create(childAnnotation);
          l.add(data);

        }

      }
    }

    return l;
    
  }
  
}
