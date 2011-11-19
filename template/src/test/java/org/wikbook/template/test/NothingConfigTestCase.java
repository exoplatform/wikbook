package org.wikbook.template.test;

import org.wikbook.template.processing.metamodel.MetaModel;

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

    MetaModel metaModel = buildClass("B");
    assertEquals(1, metaModel.getAnnotations().size());
    assertEquals("@Path", metaModel.getAnnotations().get(0).getName());
    assertEquals(1, metaModel.getAnnotations().get(0).simpleValues().size());
    assertEquals("value", metaModel.getAnnotations().get(0).simpleValues().keySet().iterator().next());
    assertEquals("b", metaModel.getAnnotations().get(0).simpleValues().values().iterator().next());

  }

}