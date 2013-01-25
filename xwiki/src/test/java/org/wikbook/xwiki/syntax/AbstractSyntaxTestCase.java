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

package org.wikbook.xwiki.syntax;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.wikbook.core.Utils;
import org.wikbook.xwiki.Format;
import org.wikbook.xwiki.SimpleXDOMDocbookBuilderContext;
import org.wikbook.core.xml.XML;
import org.wikbook.xwiki.WikbookConverter;
import org.xwiki.rendering.syntax.Syntax;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class AbstractSyntaxTestCase extends TestCase
{

   public abstract String getFolderName();

   private static class Test
   {
      private final String fileName;
      private final String syntaxId;
      private Test(String fileName, String syntaxId)
      {
         this.fileName = fileName;
         this.syntaxId = syntaxId;
      }
   }

   /** . */
   private static final Test XWIKI_2_0 = new Test("x", Syntax.XWIKI_2_0.toIdString());

   /** . */
   private static final Test CONFLUENCE_1_0 = new Test("confluence", Syntax.CONFLUENCE_1_0.toIdString());

   /** . */
   private File resultsDir;

   @Override
   protected void setUp() throws Exception
   {
      String targetDirProperty = System.getProperty("targetDir");
      if (targetDirProperty != null)
      {
         File targetDir = new File(targetDirProperty);
         if (targetDir.exists() && targetDir.isDirectory())
         {
            File resultsDir = new File(targetDir, "results");
            if (resultsDir.exists())
            {
               if (resultsDir.isFile())
               {
                  throw new AssertionFailedError("Dir " + resultsDir.getAbsolutePath() + " exists and is a file");
               }
            }
            else
            {
               if (!resultsDir.mkdir())
               {
                  throw new AssertionFailedError("Dir " + resultsDir.getAbsolutePath() + " could not be created");
               }
            }
            this.resultsDir = resultsDir;
         }
         else
         {
            throw new AssertionFailedError("No valid target dir at " + targetDirProperty);
         }
      }
      else
      {
         throw new AssertionFailedError("No valid target dir specified");
      }
   }

   protected Format getFormat()
   {
      return Format.BOOK;
   }

   public void testSyntax()
   {
      String folderName = getFolderName();
      try
      {
         doTest(folderName, XWIKI_2_0, CONFLUENCE_1_0);
      }
      catch (Exception e)
      {
         AssertionFailedError afe = new AssertionFailedError();
         afe.initCause(e);
         throw afe;
      }
   }

   private void doTest(String testPath, Test... tests) throws Exception
   {
      File base = new File(System.getProperty("basedir"));
      File path = new File(base, "src/test/resources/wiki" + testPath);
      assertTrue(path.isDirectory());
      for (Test test : tests)
      {
         // We reinstantiate the context per test in order to reset the same initial state (matters for some unit
         // test like program listing with code citations
         SimpleXDOMDocbookBuilderContext context = new SimpleXDOMDocbookBuilderContext(path);
         context.setProperty("property_name", "propertyvalue");
         File file = new File(path, test.fileName + ".wiki");
         if (file.exists())
         {
            DOMResult dom = new DOMResult();
            WikbookConverter converter = new WikbookConverter(context);
            converter.setEmitDoctype(false);
            converter.setSyntaxId(test.syntaxId);
            converter.setFormat(getFormat());
            converter.convert(test.fileName + ".wiki", dom);
            Document document = (Document)dom.getNode();
            
            //
            File f = new File(resultsDir, "test" + testPath.replace('/', '-') + "-" + test.fileName + ".xml");
            String result = XML.serialize(document);
            FileOutputStream out = new FileOutputStream(f);
            try
            {
               out.write(result.getBytes("UTF-8"));
            }
            finally
            {
               Utils.safeClose(out);
            }

            //
            File expected = new File(path, test.fileName + ".xml");
            if (!expected.exists() || !expected.isFile())
            {
               expected = new File(path, "expected.xml");
            }
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
                  String msg =
                     "expected XML:\n" + expectedXML + "\n" +
                     "effective XML:\n" + xml + "\n" +
                     "Was expecting no difference between documents for path " + testPath + " : " + diff.toString();
                  fail(msg);
               }
            }
         }
      }
   }
}