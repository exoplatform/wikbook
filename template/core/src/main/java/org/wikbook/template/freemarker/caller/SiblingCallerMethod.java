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

import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class SiblingCallerMethod implements TemplateMethodModel {

  private TemplateElement element;
  private FreemarkerDataFactory dataFactory;

  public SiblingCallerMethod(final FreemarkerDataFactory dataFactory, final TemplateElement element) {
    this.element = element;
    this.dataFactory = dataFactory;
  }

  public Object exec(final List arguments) throws TemplateModelException {

    TemplateAnnotation found = element.getAnnotation((String) arguments.get(0));
    if (found != null) {
      return dataFactory.create(found);
    }
    else {
      return null;
    }
  }
}
