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

    if (!builtData.containsKey(className)) {

      FreemarkerModelBuilder builder = new FreemarkerModelBuilder();
      buildClass(className);
      MetaModel model = readMetaModel("model." + className + "." + ext);
      builtData.put(className, builder.build(model, model.getElements().get(0)));

    }

    return builtData.get(className);

  }
}
