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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.TransformerHandler;

/**
 * A filter that strips the first element of a document and black list the wikbook prefix.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class DocumentFormatterFilter implements ContentHandler, LexicalHandler, DTDHandler
{

   /** . */
   private final TransformerHandler delegate;

   /** . */
   private int depth;

   public DocumentFormatterFilter(TransformerHandler delegate)
   {
      this.delegate = delegate;
      this.depth = 0;
   }

   public void setResult(Result result) throws IllegalArgumentException
   {
      delegate.setResult(result);
   }

   public void setSystemId(String s)
   {
      delegate.setSystemId(s);
   }

   public String getSystemId()
   {
      return delegate.getSystemId();
   }

   public Transformer getTransformer()
   {
      return delegate.getTransformer();
   }

   public void setDocumentLocator(Locator locator)
   {
      delegate.setDocumentLocator(locator);
   }

   public void startDocument() throws SAXException
   {
      delegate.startDocument();
   }

   public void endDocument() throws SAXException
   {
      delegate.endDocument();
   }

   public void startPrefixMapping(String s, String s1) throws SAXException
   {
      if (!s.equals("wikbook"))
      {
         delegate.startPrefixMapping(s, s1);
      }
   }

   public void endPrefixMapping(String s) throws SAXException
   {
      if (!s.equals("wikbook"))
      {
         delegate.endPrefixMapping(s);
      }
   }

   public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException
   {
      if (depth > 0)
      {
         delegate.startElement(s, s1, s2, attributes);
      }
      depth++;
   }

   public void endElement(String s, String s1, String s2) throws SAXException
   {
      depth--;
      if (depth > 0)
      {
         delegate.endElement(s, s1, s2);
      }
   }

   public void characters(char[] chars, int i, int i1) throws SAXException
   {
      delegate.characters(chars, i, i1);
   }

   public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException
   {
      delegate.ignorableWhitespace(chars, i, i1);
   }

   public void processingInstruction(String s, String s1) throws SAXException
   {
      delegate.processingInstruction(s, s1);
   }

   public void skippedEntity(String s) throws SAXException
   {
      delegate.skippedEntity(s);
   }

   public void startDTD(String s, String s1, String s2) throws SAXException
   {
      delegate.startDTD(s, s1, s2);
   }

   public void endDTD() throws SAXException
   {
      delegate.endDTD();
   }

   public void startEntity(String s) throws SAXException
   {
      delegate.startEntity(s);
   }

   public void endEntity(String s) throws SAXException
   {
      delegate.endEntity(s);
   }

   public void startCDATA() throws SAXException
   {
      delegate.startCDATA();
   }

   public void endCDATA() throws SAXException
   {
      delegate.endCDATA();
   }

   public void comment(char[] chars, int i, int i1) throws SAXException
   {
      delegate.comment(chars, i, i1);
   }

   public void notationDecl(String s, String s1, String s2) throws SAXException
   {
      delegate.notationDecl(s, s1, s2);
   }

   public void unparsedEntityDecl(String s, String s1, String s2, String s3) throws SAXException
   {
      delegate.unparsedEntityDecl(s, s1, s2, s3);
   }
}
