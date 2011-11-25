package org.wikbook.template.test.annotation;

import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.test.AbstractProcessorTestCase;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class ChromatticTestCase extends AbstractProcessorTestCase {

  @Override
  public void setUp() throws Exception {

    super.setUp();
    annotations = "org.chromattic.api.annotations.PrimaryType";

  }

  public void testSimpleValue() throws Exception {

    MetaModel metaModel = buildClass("C_Chromattic");
    assertEquals(1, metaModel.getAnnotations().size());
    assertEquals("@PrimaryType", metaModel.getAnnotations().get(0).getName());
    assertEquals(3, metaModel.getAnnotations().get(0).getJavadoc().size());
    assertEquals("General comment. ", metaModel.getAnnotations().get(0).getJavadoc().get(null));
    assertEquals("foo", metaModel.getAnnotations().get(0).getJavadoc().get("author"));
    assertEquals("deprecated", metaModel.getAnnotations().get(0).getJavadoc().get("deprecated"));

  }
}
