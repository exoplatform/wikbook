/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.wikbook.template.test.metamodel;

import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.test.AbstractProcessorTestCase;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class MetamodelParamTestCase extends AbstractProcessorTestCase {

  @Override
  public void setUp() throws Exception {

    super.setUp();
    annotations = "javax.ws.rs.Path,javax.ws.rs.POST,javax.ws.rs.PathParam,javax.ws.rs.QueryParam";

  }

  public void testExists() throws Exception {

    MetaModel metaModel = buildClass("E");
    assertEquals("@PathParam", metaModel.getAnnotations().get(0).getChildren().get(0).getChildren().get(0).getName());
    assertEquals("@QueryParam", metaModel.getAnnotations().get(0).getChildren().get(0).getChildren().get(1).getName());

  }

  public void testElementName() throws Exception {

    MetaModel metaModel = buildClass("E");
    assertEquals("pathParameter", metaModel.getAnnotations().get(0).getChildren().get(0).getChildren().get(0).getElement().getName());
    assertEquals("queryParameter", metaModel.getAnnotations().get(0).getChildren().get(0).getChildren().get(1).getElement().getName());

  }

  public void testJavadoc() throws Exception {

    MetaModel metaModel = buildClass("E");
    assertEquals("[Path parameter description]", metaModel.getAnnotations().get(0).getChildren().get(0).getChildren().get(0).getJavadoc(null).toString());
    assertEquals("[Query parameter description]", metaModel.getAnnotations().get(0).getChildren().get(0).getChildren().get(1).getJavadoc(null).toString());

  }

}
