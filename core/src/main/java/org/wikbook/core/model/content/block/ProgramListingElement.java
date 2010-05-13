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

package org.wikbook.core.model.content.block;

import org.wikbook.core.ResourceType;
import org.wikbook.core.WikletContext;
import org.wikbook.core.codesource.CodeContext;
import org.wikbook.core.codesource.CodeProcessor;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.OutputFormat;
import org.wikbook.core.xml.XML;
import org.wikbook.core.xml.XMLEmitter;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ProgramListingElement extends BlockElement
{

   /** . */
   private final String language;

   /** . */
   private final Integer indent;

   /** . */
   private final String content;

   /** . */
   private final WikletContext context;

   public ProgramListingElement(
      WikletContext context,
      String language,
      Integer indent,
      String content)
   {
      this.context = context;
      this.language = language;
      this.indent = indent;
      this.content = content;
   }

   @Override
   public void writeTo(XMLEmitter xml)
   {
      ElementEmitter programListingCoXML = xml.element("programlistingco");
      ElementEmitter programListingXML = programListingCoXML.element("programlisting");
      CodeLanguage codeLanguage = CodeLanguage.UNKNOWN;
      if (language != null)
      {
         if ("java".equalsIgnoreCase(language))
         {
            codeLanguage = CodeLanguage.JAVA;
         }
         else if ("xml".equalsIgnoreCase(language))
         {
            codeLanguage = CodeLanguage.XML;
         }
      }

      String bilto = content;
      switch (codeLanguage)
      {
         case XML:
            if (indent != null)
            {
               try
               {
                  Transformer transformer = XML.createTransformer(new OutputFormat(indent, true));

                  // Output buffer
                  StringWriter writer = new StringWriter();

                  // Perform identity transformation
                  transformer.transform(new StreamSource(new StringReader(content)), new StreamResult(writer));

                  //
                  bilto = writer.toString();
               }
               catch (Exception e)
               {
                  e.printStackTrace();
                  bilto = "Exception occured, see logs";
               }
            }
            programListingXML.content(bilto, true);
            break;
         case UNKNOWN:
            programListingXML.content(bilto, true);
            break;
         case JAVA:

            CodeContextImpl ctx = new CodeContextImpl(programListingXML);

            //
            new CodeProcessor().parse(bilto, ctx);

            //
            if (ctx.callouts.size() > 0)
            {
               ElementEmitter calloutListXML = programListingCoXML.element("calloutlist");
               for (Map.Entry<String, Callout> callout : ctx.callouts.entrySet())
               {
                  if (callout.getValue().text != null)
                  {
                     StringBuffer sb = new StringBuffer();
                     for (String coId : callout.getValue().ids)
                     {
                        if (sb.length() > 0)
                        {
                           sb.append(" ");
                        }
                        sb.append(coId);
                     }
                     calloutListXML.element("callout").withAttribute("arearefs", sb.toString()).element("para").content(callout.getValue().text, true);
                  }
               }
            }

            //
            break;
      }
   }

   private class CodeContextImpl implements CodeContext
   {

      /** . */
      private final XMLEmitter programListingElt;

      /** . */
      private final TreeMap<String, Callout> callouts = new TreeMap<String, Callout>();

      /** . */
      private final Random random = new Random();

      private CodeContextImpl(XMLEmitter programListingElt)
      {
         this.programListingElt = programListingElt;
      }

      public void writeContent(String content)
      {
         programListingElt.content(content, true);
      }

      public void writeCallout(String id)
      {
         String coId = "" + Math.abs(random.nextLong());

         //
         programListingElt.element("co").withAttribute("id", coId);

         //
         Callout callout = callouts.get(id);
         if (callout == null)
         {
            callout = new Callout();
            callouts.put(id, callout);
         }

         //
         callout.ids.addLast(coId);
      }

      public void setCallout(String id, String text)
      {
         Callout callout = callouts.get(id);
         if (callout == null)
         {
            callout = new Callout();
            callouts.put(id, callout);
         }
         callout.text = text;
      }

      public List<URL> resolveResources(String id) throws IOException
      {
         return context.resolveResources(ResourceType.JAVA, id);
      }
   }

   private static class Callout
   {

      /** . */
      private String text;

      /** . */
      private final LinkedList<String> ids = new LinkedList<String>();

   }
}
