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

package org.wikbook.template.test;

import org.wikbook.template.freemarker.FreemarkerModelBuilder;
import org.wikbook.template.processing.metamodel.MetaModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public abstract class AbstractFreemarkerTestCase extends AbstractProcessorTestCase {

  public static Map<String, Map<String, Object>> builtData = new HashMap<String, Map<String, Object>>();

  public Map<String, Object> buildModel(String className, String ext) throws ClassNotFoundException, IOException {
    return buildModel(array(className), ext, "model." + className);
  }

  public Map<String, Object> buildModel(String[] classNames, String ext, String modelName) throws ClassNotFoundException, IOException {
    return buildModel(classNames, ext, modelName, false);
  }

  public Map<String, Object> buildModel(String[] classNames, String ext, String modelName, boolean global) throws ClassNotFoundException, IOException {

    String key = key(classNames);

    if (!builtData.containsKey(key)) {

      FreemarkerModelBuilder builder = new FreemarkerModelBuilder();
      buildClass(classNames);
      MetaModel model = readMetaModel(modelName + "." + ext);
      if (global) {
        builtData.put(key, (Map<String, Object>) builder.build(model));
      }
      else {
        builtData.put(key, (Map<String, Object>) builder.build(model, model.getElements().get(0)));
      }

    }

    return builtData.get(key);

  }

  protected String[] array(String... values) {
    return values;
  }

  private String key(String... ns) {

    StringBuffer sb = new StringBuffer();

    for (String n : ns) {
      sb.append(n);
    }

    return sb.toString();

  }

}
