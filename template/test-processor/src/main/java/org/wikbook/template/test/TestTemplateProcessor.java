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

package org.wikbook.template.test;

import org.wikbook.template.api.AnnotationA;
import org.wikbook.template.api.AnnotationA2;
import org.wikbook.template.api.AnnotationB;
import org.wikbook.template.api.AnnotationB2;
import org.wikbook.template.api.AnnotationB3;
import org.wikbook.template.api.AnnotationC;
import org.wikbook.template.api.AnnotationD;
import org.wikbook.template.processing.AbstractTemplateProcessor;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
@SupportedSourceVersion(SourceVersion.RELEASE_5)
@SupportedAnnotationTypes({"*"})
public class TestTemplateProcessor extends AbstractTemplateProcessor {

  @Override
  protected Class[] annotations() {
    return new Class[] {
        AnnotationA.class,
        AnnotationA2.class,
        AnnotationB.class,
        AnnotationB2.class,
        AnnotationB3.class,
        AnnotationC.class,
        AnnotationD.class
    };
  }

  @Override
  protected String templateName() {
    return "src.tmpl";
  }

  @Override
  protected String generatedDirectory() {
    return "generated";
  }

  @Override
  protected String ext() {
    return "src";
  }

  @Override
  protected boolean writeModel() {
    return true;
  }

}
