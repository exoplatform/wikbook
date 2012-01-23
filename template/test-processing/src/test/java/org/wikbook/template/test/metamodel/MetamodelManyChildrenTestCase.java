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
import org.wikbook.template.processing.metamodel.TemplateElement;
import org.wikbook.template.test.AbstractProcessorTestCase;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class MetamodelManyChildrenTestCase extends AbstractProcessorTestCase {

  private MetaModel f;

  public MetamodelManyChildrenTestCase() throws ClassNotFoundException, IOException {

    buildClass("F");

    f = readMetaModel("model.F.src");

  }

  public void testExists() throws Exception {

    assertEquals(3, f.getElements().get(0).getAnnotations().values().iterator().next().getChildren().size());

  }

  public void testChildrenName() throws Exception {

    List<TemplateElement> elements = f.getElements().get(0).getAnnotations().values().iterator().next().getChildren();
    assertEquals("m", elements.get(0).getName());
    assertEquals("m2", elements.get(1).getName());
    assertEquals("m3", elements.get(2).getName());

  }

  public void testChildrenAnnotations() throws Exception {

    List<TemplateElement> elements = f.getElements().get(0).getAnnotations().values().iterator().next().getChildren();
    assertEquals(2, elements.get(0).getAnnotations().size());
    assertEquals(2, elements.get(1).getAnnotations().size());
    assertEquals(2, elements.get(2).getAnnotations().size());

  }

}
