package org.wikbook.template.test;

import java.io.FileNotFoundException;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class NothingConfigTestCase extends AbstractProcessorTestCase {

  @Override
  public void setUp() throws Exception {

    super.setUp();
    annotations = "javax.ws.rs.Path";
  }

  public void testA() throws Exception {

    try {
      buildClass("A");
      fail();
    }
    catch (FileNotFoundException e) {
      // no processing done
    }
    
  }

  public void testB() throws Exception {

    try {
      buildClass("B");
      fail();
    }
    catch (FileNotFoundException e) {
      // no processing done
    }

  }

}