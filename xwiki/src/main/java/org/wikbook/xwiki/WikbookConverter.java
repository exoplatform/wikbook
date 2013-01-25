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

package org.wikbook.xwiki;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.wikbook.core.model.DocbookBuilder;
import org.wikbook.core.WikbookException;
import org.wikbook.core.model.structural.BookElement;
import org.wikbook.core.model.structural.ComponentElement;
import org.wikbook.core.render.docbook.ElementTransformer;
import org.wikbook.core.xml.DocumentEmitter;
import org.wikbook.core.xml.OutputFormat;
import org.wikbook.core.xml.XML;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Collections;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class WikbookConverter
{

   /** . */
   private final AbstractXDOMDocbookBuilderContext context;

   /** . */
   private boolean emitDoctype;

   /** . */
   private String syntaxId;

   /** . */
   private DocumentFragment beforeBookBodyXML;

   /** . */
   private DocumentFragment afterBookBodyXML;

   /** . */
   private String charsetName;

   /** . */
   private String bookId;

   /** . */
   private Format format;

   public WikbookConverter(AbstractXDOMDocbookBuilderContext context) throws IOException, ClassNotFoundException
   {
      this.context = context;
      this.emitDoctype = true;
      this.syntaxId = null;
      this.charsetName = "UTF-8";
      this.bookId = null;
      this.format = Format.BOOK;
   }

   public boolean getEmitDoctype()
   {
      return emitDoctype;
   }

   public void setEmitDoctype(boolean emitDoctype)
   {
      this.emitDoctype = emitDoctype;
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

   public Format getFormat()
   {
      return format;
   }

   public void setFormat(Format format)
   {
      this.format = format;
   }

   public DocumentFragment getBeforeBookBodyXML()
   {
      return beforeBookBodyXML;
   }

   public void setBeforeBookBodyXML(DocumentFragment beforeBookBodyXML)
   {
      this.beforeBookBodyXML = beforeBookBodyXML;
   }

   public DocumentFragment getAfterBookBodyXML()
   {
      return afterBookBodyXML;
   }

   public void setAfterBookBodyXML(DocumentFragment afterBookBodyXML)
   {
      this.afterBookBodyXML = afterBookBodyXML;
   }

   public String getCharsetName()
   {
      return charsetName;
   }

   public void setCharsetName(String charsetName)
   {
      this.charsetName = charsetName;
   }

   public String getBookId()
   {
      return bookId;
   }

   public void setBookId(String bookId)
   {
      this.bookId = bookId;
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
      Reader reader = context.read(Collections.<String>emptyList(), id);

      //
      BookElement elt = new BookElement();

      //
      DocbookBuilder builder = new DocbookBuilder(context, elt);

      //
      builder.build(reader, syntaxId);

      // Configure the book element
      elt.setId(bookId);
      elt.setBeforeBodyXML(beforeBookBodyXML);
      elt.setAfterBodyXML(afterBookBodyXML);

      //
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

      // For now we handle this here
      if (format == null || format == Format.BOOK)
      {
         ElementTransformer<BookElement> writer = ElementTransformer.getWriter(elt);
         writer.write(elt, new DocumentEmitter(doc));
      }
      else
      {
         ComponentElement chapterElt = elt.getChapters().peekFirst();
         if (chapterElt != null)
         {
            ElementTransformer<ComponentElement> writer = ElementTransformer.getWriter(chapterElt);
            writer.write(chapterElt, new DocumentEmitter(doc));
         }
      }

      //
      Transformer transformer = XML.createTransformer(new OutputFormat(
         2,
         emitDoctype,
         "-//OASIS//DTD DocBook XML V4.5//EN",
         "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd"
      ));

      //
      transformer.transform(new DOMSource(doc), result);
   }
}
