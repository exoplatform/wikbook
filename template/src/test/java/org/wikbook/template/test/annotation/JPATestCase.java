package org.wikbook.template.test.annotation;

import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.test.AbstractProcessorTestCase;

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

    MetaModel metaModel = buildClass("C_JPA");
    assertEquals(1, metaModel.getAnnotations().size());
    assertEquals("@Entity", metaModel.getAnnotations().get(0).getName());
    assertEquals(3, metaModel.getAnnotations().get(0).javadocValues().size());
    assertEquals("General comment. ", metaModel.getAnnotations().get(0).javadocValues().get(null));
    assertEquals("foo", metaModel.getAnnotations().get(0).javadocValues().get("author"));
    assertEquals("deprecated", metaModel.getAnnotations().get(0).javadocValues().get("deprecated"));

  }
}
