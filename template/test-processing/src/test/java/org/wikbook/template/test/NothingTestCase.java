package org.wikbook.template.test;

import org.wikbook.template.processing.metamodel.MetaModel;

import java.io.FileNotFoundException;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class NothingTestCase extends AbstractProcessorTestCase {

  public void testA() throws Exception {

    try {
      buildClass("A");
      readMetaModel("model.A.src");
      fail();
    }
    catch (FileNotFoundException e) {
      // no processing donen it's ok
    }
    
  }

  public void testB() throws Exception {

    buildClass("B");
    MetaModel metaModel = readMetaModel("model.B.src");
    assertNotNull(metaModel);

  }

}