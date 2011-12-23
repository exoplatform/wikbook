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

package org.wikbook.template.processing.metamodel;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class TemplateType implements Serializable {

  private String name;
  private String fqn;
  private Boolean isArray;
  private TemplateType[] parameters;

  public TemplateType(final String name, final String fullname, final Boolean isArray, final TemplateType[] parameters) {
    this.name = name;
    this.fqn = fullname;
    this.isArray = isArray;
    this.parameters = parameters;
  }

  public TemplateType(final String name, final Boolean isArray, final TemplateType[] parameters) {
    this(name, name, isArray, parameters);
  }

  public String getName() {
    return name;
  }

  public String getFqn() {
    return fqn;
  }

  public Boolean isArray() {
    return isArray;
  }

  public TemplateType[] getParameters() {
    return parameters;
  }
  
}
