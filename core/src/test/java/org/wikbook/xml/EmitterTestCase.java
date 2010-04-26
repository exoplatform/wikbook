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

package org.wikbook.xml;

import junit.framework.TestCase;

import java.io.StringWriter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class EmitterTestCase extends TestCase
{

   /** . */
   private StringWriter writer;

   /** . */
   private DocumentEmitter emitter;

   @Override
   protected void setUp() throws Exception
   {
/*
      StringWriter writer = new StringWriter();
      SAXTransformerFactory factory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
      TransformerHandler handler = factory.newTransformerHandler();
      handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
      handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "no");
      handler.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      handler.setResult(new StreamResult(writer));
      DocumentEmitter emitter = new DocumentEmitter(new Handler(handler, handler));
*/

      //
      this.emitter = emitter;
      this.writer = writer;
   }

   public void testFoo()
   {

   }

/*
   public void testDocumentElementFail()
   {
      try
      {
         emitter.close();
         fail();
      }
      catch (IllegalStateException ignore)
      {
      }
   }

   public void testDocumentElement()
   {
      emitter.documentElement("a");
      emitter.close();
      assertEquals("<a/>", writer.toString());
   }

   public void testElement()
   {
      emitter.documentElement("a").element("b");
      emitter.close();
      assertEquals("<a><b/></a>", writer.toString());
   }

   public void testCloseElement()
   {
      emitter.documentElement("a").element("b").close();
      emitter.close();
      assertEquals("<a><b/></a>", writer.toString());
   }

   public void testContent()
   {
      emitter.documentElement("a").content("b");
      emitter.close();
      assertEquals("<a>b</a>", writer.toString());
   }
*/
}
