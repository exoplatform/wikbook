/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wikbook.core.render.docbook.content.block;

import org.wikbook.core.model.content.block.CalloutElement;
import org.wikbook.core.model.content.block.LanguageSyntax;
import org.wikbook.core.model.content.block.ProgramListingElement;
import org.wikbook.core.render.docbook.ElementTransformer;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;
import org.wikbook.text.Position;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ProgramListingTransformer extends ElementTransformer<ProgramListingElement>
{
   @Override
   public void write(ProgramListingElement element, XMLEmitter emitter)
   {
      ElementEmitter programListingCoXML = emitter.element("programlistingco");

      //
      ElementEmitter areaspecXML = programListingCoXML.element("areaspec").withAttribute("units", "linecolumn");

      //
      for (CalloutElement calloutElt : element.getCallouts())
      {
         LinkedHashMap<String,Position> ids = calloutElt.getIds();
         if (ids.size() == 1)
         {
            Map.Entry<String, Position> target = ids.entrySet().iterator().next();
            areaspecXML.element("area").
               withAttribute("id", target.getKey() + "_").
               withAttribute("linkends", target.getKey()).
               withAttribute("coords", (target.getValue().getLine() + 1) + " " + (target.getValue().getColumn() + 1));
         }
         else if (ids.size() > 1)
         {
            Map.Entry<String, Position> first = ids.entrySet().iterator().next();
            ElementEmitter areasetXML = areaspecXML.element("areaset").
               withAttribute("id", first.getKey() + "_").
               withAttribute("coords", "");
            for (Map.Entry<String, Position> target : ids.entrySet())
            {
               areasetXML.element("area").
                  withAttribute("id", target.getKey() + "__").
                  withAttribute("linkends", target.getKey()).
                  withAttribute("coords", (target.getValue().getLine() + 1) + " " + (target.getValue().getColumn() + 1));
            }
         }
      }

      //
      ElementEmitter programListingXML = programListingCoXML.element("programlisting");
      if (element.isHighlightCode() && element.getLanguageSyntax() != LanguageSyntax.UNKNOWN)
      {
         programListingXML.withAttribute("language", element.getLanguageSyntax().name().toLowerCase());
      }

      //
      if (element.getCallouts().isNotEmpty())
      {
         write(element.getCallouts(), false, programListingCoXML.element("calloutlist"));
      }

      //
      programListingXML.content(element.getListing(), true);
   }
}
