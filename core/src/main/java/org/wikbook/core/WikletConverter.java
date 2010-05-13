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

package org.wikbook.core;

import org.w3c.dom.Document;
import org.wikbook.core.wiki.WikiLoader;
import org.wikbook.core.transform.XDOMTransformer;
import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.xml.DocumentEmitter;
import org.wikbook.core.xml.OutputFormat;
import org.wikbook.core.xml.XML;
import org.xwiki.rendering.block.XDOM;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class WikletConverter
{

   /** . */
   private final WikletContext context;

   /** . */
   private boolean emitDTD;

   /** . */
   private String syntaxId;

   public WikletConverter(WikletContext context) throws IOException, ClassNotFoundException
   {
      this.context = context;
      this.emitDTD = true;
      this.syntaxId = null;
   }

   public boolean isEmitDTD()
   {
      return emitDTD;
   }

   public void setEmitDTD(boolean emitDTD)
   {
      this.emitDTD = emitDTD;
   }

   public String convert()
   {
      return convert("main.wiki");
   }

   public void convert(Result result) throws WikbookException
   {
      convert("main.wiki", result);
   }

   public String getSyntaxId()
   {
      return syntaxId;
   }

   public void setSyntaxId(String syntaxId)
   {
      this.syntaxId = syntaxId;
   }

   public void convert(String id, Result result) throws WikbookException
   {
      try
      {
         _convert2(id, result);
      }
      catch (Exception e)
      {
         WikbookException ce;
         if (e instanceof WikbookException)
         {
            ce = (WikbookException)e;
         }
         else
         {
            ce = new WikbookException(e);
         }
         throw ce;
      }

   }

   public String convert(String id) throws WikbookException
   {
      StringWriter writer = new StringWriter();
      convert(id, new StreamResult(writer));
      return writer.toString();
   }

   private void _convert2(String id, Result result) throws Exception
   {
      WikiLoader loader = new WikiLoader(context);

      //
      XDOM main = loader.load(id, syntaxId);

      //
      XDOMTransformer xdomTransformer = new XDOMTransformer(context);

      //
      DocbookElement elt = xdomTransformer.transform(main);

      //
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

      //
      elt.writeTo(new DocumentEmitter(doc));

      //
      Transformer transformer = XML.createTransformer(new OutputFormat(
         2,
         false,
         "-//OASIS//DTD DocBook XML V4.5//EN",
         "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd"
      ));

      //
      transformer.transform(new DOMSource(doc), result);
   }

/*
   private void _convert(String id, Result result) throws Exception
   {
      WikletParser parser = new WikletParser(context);

      //
      XDOM main = parser.parse(id);

      //
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

      //
      DocbookSerializer serializer = new DocbookSerializer(doc, context);

      //
      main.traverse(serializer);

      //
      Transformer transformer = XML.createTransformer(new OutputFormat(
         2,
         false,
         "-//OASIS//DTD DocBook XML V4.5//EN",
         "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd"
      ));

      //
      transformer.transform(new DOMSource(doc), result);
   }
*/

}
