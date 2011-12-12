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
import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.TemplateElement;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class SiblingCallerMethod implements TemplateMethodModel {

  private TemplateElement element;

  public SiblingCallerMethod(final TemplateElement element) {
    this.element = element;
  }

  public Object exec(final List arguments) throws TemplateModelException {

    TemplateAnnotation found = element.getAnnotation((String) arguments.get(0));
    if (found != null) {
      Map<String, Object> data = found.getValues();
      data.put("attribute", new AttributeCallerMethod(found.getValues()));
      data.put("doc", new JavadocCallerMethod(element.getJavadoc()));
      data.put("children", new ChildrenCallerMethod(found.getChildren()));
      data.put("sibling", new SiblingCallerMethod(element));
      data.put("elementName", element.getName());
      data.put("name", found.getName().substring(1));
      return data;
    }
    else {
      return null;
    }
  }
}
