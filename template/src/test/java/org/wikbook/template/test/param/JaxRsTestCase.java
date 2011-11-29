package org.wikbook.template.test.param;

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
    annotations = "javax.ws.rs.Path,javax.ws.rs.POST,javax.ws.rs.PathParam,javax.ws.rs.QueryParam";

  }

  public void testSimpleValue() throws Exception {

    MetaModel metaModel = buildClass("E_JaxRs");

  }
}
