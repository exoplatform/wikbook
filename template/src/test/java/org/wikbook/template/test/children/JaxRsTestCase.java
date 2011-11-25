package org.wikbook.template.test.children;

import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.test.AbstractProcessorTestCase;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class JaxRsTestCase extends AbstractProcessorTestCase {

  @Override
  public void setUp() throws Exception {

    super.setUp();
    annotations = "javax.ws.rs.Path,javax.ws.rs.POST";

  }

  public void testSimpleValue() throws Exception {

    MetaModel metaModel = buildClass("D_JaxRs");
    assertEquals(1, metaModel.getAnnotations().size());
    assertEquals("@Path", metaModel.getAnnotations().get(0).getName());
    assertEquals(1, metaModel.getAnnotations().get(0).getChildren().size());
    assertEquals("@POST", metaModel.getAnnotations().get(0).getChildren().get(0).getName());
    assertEquals(2, metaModel.getAnnotations().get(0).getChildren().get(0).getJavadoc().size());
    assertEquals("foo", metaModel.getAnnotations().get(0).getChildren().get(0).getJavadoc().get("author"));
    assertEquals("My method. ", metaModel.getAnnotations().get(0).getChildren().get(0).getJavadoc().get(null));

  }
}
