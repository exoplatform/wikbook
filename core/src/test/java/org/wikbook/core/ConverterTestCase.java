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

import junit.framework.TestCase;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.wikbook.core.xml.XML;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;
import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ConverterTestCase extends TestCase
{

   private void doTest(String testPath) throws Exception
   {
      File base = new File(System.getProperty("basedir"));
      File path = new File(base, "src/test/resources/wiki" + testPath);
      assertTrue(path.isDirectory());
      SimpleWikletContext context = new SimpleWikletContext(path);
      context.setProperty("property_name", "property_value");
      context.addPerson("foo", new Person("foo", "bar", "foo@bar.com", "writer"));
      WikletConverter converter = new WikletConverter(context);
      converter.setEmitDoctype(false);
      DOMResult dom = new DOMResult();
      converter.convert(dom);
      Document document = (Document)dom.getNode();
      File expected = new File(path, "expected.xml");
      if (expected.exists() && expected.isFile())
      {
         Document expectedDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(expected);
         XMLUnit.setIgnoreAttributeOrder(true);
         XMLUnit.setIgnoreComments(true);
         XMLUnit.setIgnoreWhitespace(true);
         DetailedDiff diff = new DetailedDiff(new Diff(expectedDocument, document));
         List diffs = diff.getAllDifferences();
         if (diffs.size() > 0)
         {
            String expectedXML = XML.serialize(expectedDocument);
            String xml = XML.serialize(document);
            System.out.println("expectedXML: " + expectedXML);
            System.out.println("xml: " + xml);
            fail("Was expecting no difference between documents for path "+ testPath + " : " + diff.toString());
         }
      }
   }

   public void testHorizontalRule() throws Exception
   {
      doTest("/horizontalrule");
   }

   public void testSection() throws Exception
   {
      doTest("/section");
   }

   public void testSimple() throws Exception
   {
      doTest("/simple");
   }

   public void testBold() throws Exception
   {
      doTest("/bold");

   }

   public void testItalic() throws Exception
   {
      doTest("/italic");
   }

   public void testMonospace() throws Exception
   {
      doTest("/monospace");
   }

   public void testSuperscript() throws Exception
   {
      doTest("/superscript");
   }

   public void testSubscript() throws Exception
   {
      doTest("/subscript");
   }

   public void testUnderline() throws Exception
   {
      doTest("/underline");
   }

   public void testStrike() throws Exception
   {
      doTest("/strike");
   }

   public void testLineBreak() throws Exception
   {
      doTest("/linebreak");
   }

   public void testChapter() throws Exception
   {
      doTest("/chapter");
   }

   public void testPara() throws Exception
   {
      doTest("/para");
   }

   public void testBulletList() throws Exception
   {
      doTest("/bulletlist");
   }

   public void testNumbertList() throws Exception
   {
      doTest("/numberlist");
   }

   public void testAdmonitions() throws Exception
   {
      doTest("/admonitions");
   }

   public void testScreen() throws Exception
   {
      doTest("/screen");
   }

   public void testLink() throws Exception
   {
      doTest("/link");
   }

   public void testAnchor() throws Exception
   {
      doTest("/anchor");
   }

   public void testProperty() throws Exception
   {
      doTest("/property");
   }

   public void testCode() throws Exception
   {
      doTest("/code");
   }

   public void testInclude() throws Exception
   {
      doTest("/include");
   }

   public void testTable() throws Exception
   {
      doTest("/table");
   }

   public void testIncludeSection() throws Exception
   {
      doTest("/includesection");
   }

   public void testVerbatim() throws Exception
   {
      doTest("/verbatim");
   }

   public void testQuotation() throws Exception
   {
      doTest("/quotation");
   }

   public void testDocbook() throws Exception
   {
      doTest("/docbook");
   }

   public void testImage() throws Exception
   {
      doTest("/image");
   }

   public void testTableWithGroup() throws Exception
   {
      doTest("/tablewithgroup");
   }

   public void testDefinitionLists() throws Exception
   {
      doTest("/definitionlists");
   }

   public void testSyntax() throws Exception
   {
      doTest("/syntax");
   }

   public void testIncludeVerbatim() throws Exception
   {
      doTest("/includeverbatim");
   }

   public void testExample() throws Exception
   {
      doTest("/example");
   }

   public void testExampleInclude() throws Exception
   {
      doTest("/exampleinclude");
   }

   public void testXML() throws Exception
   {
      doTest("/xml");
   }
}
