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

package org.wikbook.template.test.freemarker;

import org.wikbook.template.test.AbstractFreemarkerTestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 */
public class FreemarkerGlobalTestCase extends AbstractFreemarkerTestCase {

  private Map<String, Object> global;

  public FreemarkerGlobalTestCase() throws IOException, ClassNotFoundException {

    global = buildModel(
        array("A", "B", "C", "D", "E", "F", "G"),
        "src",
        "org.wikbook.template.test.TestGlobalTemplateProcessor",
        true);
    }
  
    public void testGlobalModel() throws Exception {

      assertEquals(1, global.size());

      Map.Entry<String, Object> entry = global.entrySet().iterator().next();
      assertEquals("@AnnotationA", entry.getKey());
      assertEquals(ArrayList.class, entry.getValue().getClass());

      ArrayList values = (ArrayList) entry.getValue();
      assertEquals(6, values.size());
      assertEquals(HashMap.class, values.get(0).getClass());
      assertEquals(HashMap.class, values.get(1).getClass());
      assertEquals(HashMap.class, values.get(2).getClass());
      assertEquals(HashMap.class, values.get(3).getClass());
      assertEquals(HashMap.class, values.get(4).getClass());
      assertEquals(HashMap.class, values.get(5).getClass());

  }
}
