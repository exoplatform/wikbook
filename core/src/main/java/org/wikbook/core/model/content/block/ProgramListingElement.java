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

import org.w3c.dom.*;
import org.wikbook.core.ResourceType;
import org.wikbook.core.WikletContext;
import org.wikbook.core.codesource.CodeContext;
import org.wikbook.core.codesource.CodeProcessor;
import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.ElementContainer;
import org.wikbook.core.transform.XDOMTransformer;
import org.wikbook.core.wiki.WikiLoader;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.OutputFormat;
import org.wikbook.core.xml.XML;
import org.wikbook.core.xml.XMLEmitter;
import org.wikbook.text.Position;
import org.wikbook.text.TextArea;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xwiki.rendering.block.XDOM;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.LinkedHashMap;
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
   private final ElementContainer<CalloutElement> callouts;

   /** . */
   private final LanguageSyntax languageSyntax;

   /** . */
   private final Integer indent;

   /** . */
   private final String content;

   /** . */
   private final WikletContext context;

   /** . */
   private final boolean highlightCode;

   /** . */
   private String listing;

   /** . */
   private final DocumentBuilder documentBuilder;

   public ProgramListingElement(
      final WikletContext context,
      LanguageSyntax languageSyntax,
      Integer indent,
      String content,
      boolean highlightCode) throws ParserConfigurationException {

      //
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setIgnoringElementContentWhitespace(true);
      dbf.setCoalescing(true);
      dbf.setNamespaceAware(true);
      dbf.setXIncludeAware(true);
      DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

      documentBuilder.setEntityResolver(new EntityResolver()
      {
         public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
         {
            if (systemId.startsWith("wikbook://"))
            {
               String id = systemId.substring("wikbook://".length());
               URL resolved = context.resolveResource(ResourceType.XML, id);
               if (resolved != null)
               {
                  return new InputSource(resolved.openStream());
               }
            }
            return null;
         }
      });

      //
      this.context = context;
      this.languageSyntax = languageSyntax;
      this.indent = indent;
      this.content = content;
      this.highlightCode = highlightCode;
      this.callouts = new ElementContainer<CalloutElement>(CalloutElement.class);
      this.documentBuilder = documentBuilder;
   }

   private void clearBase(Element elt) 
   {
      if (elt.hasAttribute("xml:base"))
      {
         elt.removeAttribute("xml:base");
      }
      for (Element childElt : XML.elements(elt))
      {
         clearBase(childElt);
      }
   }

   public void process(XDOMTransformer _transformer)
   {
      String bilto;
      switch (languageSyntax)
      {
         case XML:
            try
            {
               // Output buffer
               StringWriter writer = new StringWriter();

               // Create transformer handler
               Transformer transformer = XML.createTransformer(new OutputFormat(indent, false));

               // Surround with an element to take care of special case
               String data = "<root xmlns:xi=\"http://www.w3.org/2001/XInclude\" xml:base=\"wikbook://\">" + content + "</root>";

               //
               Document doc = documentBuilder.parse(new InputSource(new StringReader(data)));

               // Perform inclusions
               clearBase(doc.getDocumentElement());

               // Remove white spaces
               if (indent != null) {
                  XML.removeWhiteSpace(doc.getDocumentElement());
               }

               // Keep only the content under the root
               DocumentFragment fragment = doc.createDocumentFragment();
               NodeList a = doc.getDocumentElement().getChildNodes();
               for (int i = 0;i < a.getLength();i++)
               {
                  Node n = a.item(i);
                  if (n instanceof Attr)
                  {
                     continue;
                  }
                  fragment.appendChild(n);
               }

               //
               transformer.transform(new DOMSource(fragment), new StreamResult(writer));

               // Get the original data
               data = writer.toString();

               //
               TextArea ta = new TextArea(data);
               ta.trimLeft();

               //
               bilto = ta.getText();
            }
            catch (Exception e)
            {
               e.printStackTrace();
               bilto = "Exception occured, see logs";
            }
            break;
         case JAVA:

            CodeContextImpl ctx = new CodeContextImpl();

            //
            new CodeProcessor().parse(content, ctx);

            // Create the resulting callouts
            for (Map.Entry<String, Callout> callout : ctx.callouts.entrySet()) {
               if (callout.getValue().text != null)
               {
                  CalloutElement calloutElt = new CalloutElement(callout.getValue().ids, callout.getValue().text);
                  push(calloutElt);

                  WikiLoader loader = new WikiLoader(context);
                  XDOM dom = loader.load(new StringReader(callout.getValue().text), null);
                  dom.traverse(_transformer);
                  
                  merge();
               }
            }

            //
            bilto = ctx.sb.toString();

            //
            break;
         default:
            bilto = content;
            break;
      }

      //
      this.listing = bilto;
   }

   @Override
   public boolean append(DocbookElement elt)
   {
      return callouts.append(elt);
   }

   @Override
   public void writeTo(XMLEmitter xml)
   {
      ElementEmitter programListingCoXML = xml.element("programlistingco");

      //
      ElementEmitter areaspecXML = programListingCoXML.element("areaspec").withAttribute("units", "linecolumn");

      //
      for (CalloutElement calloutElt : callouts)
      {
         for (Map.Entry<String, Position> target : calloutElt.getIds().entrySet())
         {
            areaspecXML.element("area").
               withAttribute("id", target.getKey() + "-co").
               withAttribute("linkends", target.getKey()).
               withAttribute("coords",(target.getValue().getLine() + 1) + " " + (target.getValue().getColumn() + 1));
         }
      }

      //
      ElementEmitter programListingXML = programListingCoXML.element("programlisting");
      if (highlightCode && languageSyntax != LanguageSyntax.UNKNOWN)
      {
         programListingXML.withAttribute("language", languageSyntax.name().toLowerCase());
      }

      //
      if (callouts.isNotEmpty())
      {
         callouts.writeTo(programListingCoXML.element("calloutlist"));
      }

      //
      programListingXML.content(listing, true);
   }

   private class CodeContextImpl implements CodeContext
   {

      /** . */
      private final StringBuilder sb = new StringBuilder();

      /** . */
      private final TreeMap<String, Callout> callouts = new TreeMap<String, Callout>();

      /** . */
      private final Random random = new Random();

      private CodeContextImpl()
      {
      }

      public void writeContent(String content)
      {
         sb.append(content);
      }

      public void writeCallout(String index)
      {
         String coId = "" + Math.abs(random.nextLong());

         //
         TextArea ta = new TextArea(sb.toString());
         Position pos = ta.position(sb.length());

         //
         Callout callout = callouts.get(index);
         if (callout == null)
         {
            callout = new Callout();
            callouts.put(index, callout);
         }

         // Write a white space so we are sure that the position for the callout exist in the text
         writeContent(" ");

         //
         callout.ids.put(coId, pos);
      }

      public void setCallout(String index, String text)
      {
         Callout callout = callouts.get(index);
         if (callout == null)
         {
            callout = new Callout();
            callouts.put(index, callout);
         }
         callout.text = text;
      }

      public InputStream resolveResources(String id) throws IOException
      {
         List<URL> list = context.resolveResources(ResourceType.JAVA, id);
         if (list.size() > 0)
         {
            return list.get(0).openStream();
         }
         return null;
      }
   }

   private static class Callout
   {

      /** . */
      private String text;

      /** . */
      private final LinkedHashMap<String, Position> ids = new LinkedHashMap<String, Position>();

   }
}
