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
import org.wikbook.template.freemarker.ExpressionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class AttributeCallerMethod implements TemplateMethodModel {

  private Map<String, Object> attributes;

  public AttributeCallerMethod(final Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  public Object exec(final List arguments) throws TemplateModelException {

    ExpressionHandler eh = new ExpressionHandler((String) arguments.get(0));
    Object got = attributes.get(eh.getValue());
    if (got != null) {

      if (got.getClass().isArray()) {
        return eh.getAttribute(Arrays.asList((String[])got));
      }
      else {
        return new SimpleScalar(got.toString());
      }

    }
    else {
      return null;
    }

  }

}
