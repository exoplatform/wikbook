package org.wikbook.template.test;

import org.wikbook.template.processing.metamodel.MetaModel;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class JPATestCase extends AbstractProcessorTestCase {

  @Override
  public void setUp() throws Exception {

    super.setUp();
    annotations = "javax.persistence.Entity";

  }

  public void testSimpleValue() throws Exception {

    MetaModel metaModel = buildClass("B_JPA");
    assertEquals(1, metaModel.getAnnotations().size());
    assertEquals("@Entity", metaModel.getAnnotations().get(0).getName());
    assertEquals(1, metaModel.getAnnotations().get(0).simpleValues().size());
    assertEquals("name", metaModel.getAnnotations().get(0).simpleValues().keySet().iterator().next());
    assertEquals("b", metaModel.getAnnotations().get(0).simpleValues().values().iterator().next());

  }
}
