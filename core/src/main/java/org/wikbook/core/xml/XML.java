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

package org.wikbook.core.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class XML
{

   public static String serialize(Document document) throws TransformerException
   {
      return serialize(document, new OutputFormat(2, true));
   }

   public static String serialize(Document document, OutputFormat format) throws TransformerException
   {
      Transformer transformer = createTransformer(format);
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      transformer.transform(new DOMSource(document), result);
      return writer.toString();
   }

   public static TransformerHandler createTransformerHandler(
      OutputFormat doctype) throws TransformerConfigurationException
   {
      SAXTransformerFactory factory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();

      //
      if (doctype.getIndent() != null)
      {
         // This is proprietary, so it's a best effort
         factory.setAttribute("indent-number",  doctype.getIndent());
      }

      //
      TransformerHandler handler = factory.newTransformerHandler();
      Transformer transformer = handler.getTransformer();

      //
      if (doctype.getIndent() != null)
      {
         // This is proprietary, so it's a best effort
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(doctype.getIndent()));
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      }
      else
      {
         transformer.setOutputProperty(OutputKeys.INDENT, "no");
      }

      //
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");

      //
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

      //
      if (doctype.getPublicId() != null)
      {
         transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
      }

      //
      if (doctype.getSystemId() != null)
      {
         transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
      }

      //
      handler.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, doctype.isOmitDeclaration() ? "yes" : "no");

      //
      return handler;
   }

   public static Transformer createTransformer(OutputFormat format) throws TransformerConfigurationException
   {
      return createTransformerHandler(format).getTransformer();
   }

   /**
    * Cleanup the element from any white space children it may have.
    *
    * @param elt the element to cleanup
    * @throws NullPointerException if the element is null
    */
   public static void removeWhiteSpace(Element elt) throws NullPointerException
   {
      if (elt == null)
      {
         throw new NullPointerException();
      }
      List<Node> childrenToRemove = null;
      NodeList children = elt.getChildNodes();
      for (int i = 0;i < children.getLength();i++)
      {
         Node child = children.item(i);
         if (child instanceof Text)
         {
            Text textChild = (Text)child;
            String trimmed = textChild.getData().trim();
            if (trimmed.length() == 0)
            {
               if (childrenToRemove == null)
               {
                  childrenToRemove = new LinkedList<Node>();
               }
               childrenToRemove.add(textChild);
            }
            else
            {
               textChild.setData(trimmed);
            }
         }
         else if (child instanceof Element)
         {
            Element eltChild = (Element)child;
            removeWhiteSpace(eltChild);
         }
      }
      if (childrenToRemove != null)
      {
         for (Node child : childrenToRemove)
         {
            elt.removeChild(child);
         }
      }
   }
}
