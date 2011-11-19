package org.wikbook.template.test;

import org.wikbook.template.processing.metamodel.MetaModel;

import java.io.FileNotFoundException;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class NothingNoConfigTestCase extends AbstractProcessorTestCase {

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