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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.wikbook.core.model.DocbookBuilderContext;
import org.wikbook.core.ResourceType;
import org.wikbook.core.WikbookException;
import org.wikbook.core.codesource.CodeContext;
import org.wikbook.core.codesource.CodeProcessor;
import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.ElementContainer;
import org.wikbook.core.xml.OutputFormat;
import org.wikbook.core.xml.XML;
import org.wikbook.text.Position;
import org.wikbook.text.TextArea;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
   private final DocbookBuilderContext context;

   /** . */
   private final boolean highlightCode;

   /** . */
   private String listing;

   /** . */
   private final DocumentBuilder documentBuilder;

   /** . */
   private final XPath xpath;

   public ProgramListingElement(
      final DocbookBuilderContext context,
      LanguageSyntax languageSyntax,
      Integer indent,
      String content,
      boolean highlightCode)
   {

      //
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setIgnoringElementContentWhitespace(true);
      dbf.setCoalescing(true);
      dbf.setNamespaceAware(true);
      dbf.setXIncludeAware(false);

      //
      DocumentBuilder documentBuilder;
      try
      {
         documentBuilder = dbf.newDocumentBuilder();
      }
      catch (ParserConfigurationException e)
      {
         throw new WikbookException(e);
      }

      //
      this.context = context;
      this.languageSyntax = languageSyntax;
      this.indent = indent;
      this.content = content;
      this.highlightCode = highlightCode;
      this.callouts = new ElementContainer<CalloutElement>(CalloutElement.class);
      this.documentBuilder = documentBuilder;
      this.xpath = XPathFactory.newInstance().newXPath();
   }

   public String getListing()
   {
      return listing;
   }

   public LanguageSyntax getLanguageSyntax()
   {
      return languageSyntax;
   }

   public boolean isHighlightCode()
   {
      return highlightCode;
   }

   public ElementContainer<CalloutElement> getCallouts()
   {
      return callouts;
   }

   private void performIncludes(Element elt)
   {
      if ("urn:wikbook:internal".equals(elt.getNamespaceURI()) && "include".equals(elt.getLocalName()))
      {
         String hrefAttr = elt.getAttribute("href");
         List<Node> replacementElts = null;
         try
         {
            URL url = assertContextualized().resolveResource(ResourceType.XML, hrefAttr);
            if (url != null)
            {
               Document includedDoc = documentBuilder.parse(url.openStream());

               //
               if (elt.hasAttribute("xpath"))
               {
                  String xpathAttr = elt.getAttribute("xpath");
                  XPathExpression xpathExpr = xpath.compile(xpathAttr);
                  replacementElts = new ArrayList<Node>();
                  NodeList resolvedNodes = (NodeList)xpathExpr.evaluate(includedDoc, XPathConstants.NODESET);
                  for (int i = 0;i < resolvedNodes.getLength();i++)
                  {
                     Node resolvedNode = resolvedNodes.item(i);
                     if (resolvedNode.getNodeType() != Document.ATTRIBUTE_NODE)
                     {
                        replacementElts.add(elt.getOwnerDocument().importNode(resolvedNode, true));
                     }
                     else
                     {
                        // Log that somehow
                        // throw new Exception("Target of xpath expression " + xpathAttr + " does not resolve to an XML element but to " + resolvedNode);
                     }
                  }
               }
               else
               {
                  Element includedElt = includedDoc.getDocumentElement();
                  replacementElts = Collections.singletonList(elt.getOwnerDocument().importNode(includedElt, true));
               }
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
            Element errorElt = elt.getOwnerDocument().createElement("wikbook:error");
            Text text = elt.getOwnerDocument().createTextNode(e.getMessage());
            errorElt.appendChild(text);
            replacementElts = Collections.<Node>singletonList(errorElt);
         }

         // Insert elements
         for (Node replacement : replacementElts)
         {
            elt.getParentNode().insertBefore(replacement, elt);
         }

         // Remove include
         elt.getParentNode().removeChild(elt);
      }
      else
      {
         for (Element childElt : XML.elements(elt))
         {
            performIncludes(childElt);
         }
      }
   }

   public void process()
   {
      String bilto;
      switch (languageSyntax)
      {
         case XML:
            try
            {
               // Wrap the whole document with a root and declares the internal wikbook namespace
               String data = "<root xmlns:wikbook=\"urn:wikbook:internal\">" + content.replaceAll("<\\?xml.*\\?>", "") + "</root>";

               // Parse the resulting document
               Document doc = documentBuilder.parse(new InputSource(new StringReader(data)));
               Element docElt = doc.getDocumentElement();

               // Perform inclusions
               performIncludes(docElt);

               // Remove white spaces
               // todo : check if we can do it in the {@link DocumentFormatterFilter} directly
               if (indent != null)
               {
                  XML.removeWhiteSpace(docElt);
               }

               // The output buffer
               StringWriter writer = new StringWriter();

               // Create transformer handler that will serialize to a steam
               TransformerHandler serializer = XML.createTransformerHandler(new OutputFormat(indent, false));

               // Set the result that will receive the stream
               serializer.setResult(new StreamResult(writer));

               // This transformer will transform a DOM into a serie of sax events
               Transformer domToSAX = TransformerFactory.newInstance().newTransformer();

               // Now transform the DOM into a serie of filtered event to the stream receiver
               domToSAX.transform(new DOMSource(doc), new SAXResult(new DocumentFormatterFilter(serializer)));

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
            for (Map.Entry<String, Callout> callout : ctx.callouts.entrySet())
            {
               if (callout.getValue().text != null)
               {
                  CalloutElement calloutElt = new CalloutElement(callout.getValue().ids, callout.getValue().text);

                  //
                  assertContextualized().build(new StringReader(callout.getValue().text), calloutElt);

                  //
                  merge(calloutElt);
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
