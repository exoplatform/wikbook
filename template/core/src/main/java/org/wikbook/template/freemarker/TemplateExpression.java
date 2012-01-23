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

import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class TemplateExpression {

  public static enum Output {

    FLAT () {
      public String prefix() {
        return "flat:";
      }
    },

    LIST () {
      public String prefix() {
        return "list:";
      }
    },

    BLOC () {
      public String prefix() {
        return "bloc:";
      }
    },

    NONE () {
      public String prefix() {
        return "";
      }
    },

    NOEXPR () {
      public String prefix() {
        return "";
      }
    };

    public abstract String prefix();

  }

  private String expression;
  private String value;
  private Output output;

  private TemplateExpression() {
    output = Output.NOEXPR;
  }

  public TemplateExpression(final String expression) {
    this.expression = expression;

    if (expression.startsWith(Output.FLAT.prefix())) {
      output = Output.FLAT;
      value = expression.substring(Output.FLAT.prefix().length());
    }
    else if (expression.startsWith(Output.BLOC.prefix())) {
      output = Output.BLOC;
      value = expression.substring(Output.BLOC.prefix().length());
    }
    else if (expression.startsWith(Output.LIST.prefix())) {
      output = Output.LIST;
      value = expression.substring(Output.LIST.prefix().length());
    }
    else {
      output = Output.NONE;
      value = expression;
    }
  }

  public static TemplateExpression noExpression() {
    return new TemplateExpression();
  }

  public String getValue() {
    return value;
  }

  public String toString() {
    return expression;
  }

  public Output getOutput() {
    return output;
  }

  public String flatStringList(List<? extends Object> l) {

    StringBuilder sb = new StringBuilder();
    for (Object v : l) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(v.toString());
    }

    return sb.toString();

  }
  
}
