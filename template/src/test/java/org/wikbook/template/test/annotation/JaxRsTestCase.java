package org.wikbook.template.test.annotation;

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
    annotations = "javax.ws.rs.Path";

  }

  public void testSimpleValue() throws Exception {

    MetaModel metaModel = buildClass("C_JaxRs");
    assertEquals(1, metaModel.getAnnotations().size());
    assertEquals("@Path", metaModel.getAnnotations().get(0).getName());
    assertEquals(3, metaModel.getAnnotations().get(0).getJavadoc().size());
    assertEquals("General comment. ", metaModel.getAnnotations().get(0).getJavadoc().get(null));
    assertEquals("foo", metaModel.getAnnotations().get(0).getJavadoc().get("author"));
    assertEquals("deprecated", metaModel.getAnnotations().get(0).getJavadoc().get("deprecated"));

  }
}
