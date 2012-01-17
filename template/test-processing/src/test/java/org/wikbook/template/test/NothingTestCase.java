package org.wikbook.template.test;

import org.wikbook.template.processing.metamodel.MetaModel;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class NothingTestCase extends AbstractProcessorTestCase {

  /*public void testA() throws Exception {

    try {
      buildClass("A");
      fail();
    }
    catch (FileNotFoundException e) {
      // no processing done
    }
    
  }*/

  public void testB() throws Exception {

    buildClass("B");
    MetaModel metaModel = readMetaModel("model.B.src");
    assertNotNull(metaModel);

  }

}