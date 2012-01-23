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

package org.wikbook.template.test.rendered;

import junit.framework.AssertionFailedError;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.test.AbstractProcessorTestCase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class ExpectationTestCase extends AbstractProcessorTestCase {

  private MetaModel m;

  public ExpectationTestCase() throws ClassNotFoundException, IOException {

    buildClass("package-info", "B", "C");

    m = readMetaModel("model.pkg");

  }

  public void testModelPkg() throws Exception {

    assertTrue(sameContent("expected/model.pkg", "target/test-classes/generated/model.pkg"));
    
  }

  public void testBSrc() throws Exception {

    assertTrue(sameContent("expected/model.B.src", "target/test-classes/generated/model.B.src"));

  }

  public void testCSrc() throws Exception {

    assertTrue(sameContent("expected/model.C.src", "target/test-classes/generated/model.C.src"));

  }

  private boolean sameContent(String location1, String location2) throws IOException {

    InputStream is1 = Thread.currentThread().getContextClassLoader().getResourceAsStream(location1);
    InputStream is2 = new FileInputStream(location2);

    BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
    BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));

    while(true) {

      String line1 = br1.readLine();
      String line2 = br2.readLine();

      // If completed
      if (line1 == null && line2 == null) {
        break;
      }

      if (!line1.equals(line2)) {
        throw new AssertionFailedError(line1 + " != " + line2);
      }
      
    }

    return true;

  }
  
}
