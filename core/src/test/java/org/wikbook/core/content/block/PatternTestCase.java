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

package org.wikbook.core.content.block;

import junit.framework.TestCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.wikbook.core.codesource.CodeProcessor.CALLOUT_ANCHOR_PATTERN;
import static org.wikbook.core.codesource.CodeProcessor.CALLOUT_DEF_PATTERN;
import static org.wikbook.core.codesource.CodeProcessor.BLANK_LINE_PATTERN;
import static org.wikbook.core.codesource.CodeProcessor.BEGIN_CHUNK_PATTERN;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class PatternTestCase extends TestCase
{


   
/*
   public void testEmptyLinePattern()
   {
      assertMatches(BILTO, " ", " ");
      assertMatches(BILTO, "\n", "");
      assertMatches(BILTO, "\n\n", "", "");
      assertMatches(BILTO, "a\nb");
      assertMatches(BILTO, "a\n \nb", " ");
   }
*/

   public void testBlankLinePattern()
   {
      assertTrue(BLANK_LINE_PATTERN.matcher("").matches());
      assertTrue(BLANK_LINE_PATTERN.matcher(" ").matches());
      assertTrue(BLANK_LINE_PATTERN.matcher("\t").matches());
      assertTrue(BLANK_LINE_PATTERN.matcher("\t ").matches());
   }

   public void testSeparatorPattern()
   {
      assertMatches(BEGIN_CHUNK_PATTERN, "//-1-", "//-1-");
      assertMatches(BEGIN_CHUNK_PATTERN, "//-1- ", "//-1- ");
      assertMatches(BEGIN_CHUNK_PATTERN, "// -1-", "// -1-");
      assertMatches(BEGIN_CHUNK_PATTERN, "// -1- ", "// -1- ");
      assertMatches(BEGIN_CHUNK_PATTERN, " //-1-", " //-1-");
      assertMatches(BEGIN_CHUNK_PATTERN, " // -1-", " // -1-");
      assertMatches(BEGIN_CHUNK_PATTERN, " //-1- ", " //-1- ");
      assertMatches(BEGIN_CHUNK_PATTERN, " // -1- ", " // -1- ");

      //
      assertMatches(BEGIN_CHUNK_PATTERN, "//-1-a");
      assertMatches(BEGIN_CHUNK_PATTERN, "//-1- a");
      assertMatches(BEGIN_CHUNK_PATTERN, "a//-1-");
      assertMatches(BEGIN_CHUNK_PATTERN, "a //-1-");
   }

   public void testCalloutAnchor_Multiline()
   {
      String s = "a\n// <1> a\n=";
      Matcher matcher = CALLOUT_ANCHOR_PATTERN.matcher(s);
      assertTrue(matcher.find());
      assertEquals("// <1> a", matcher.group(0));
      assertEquals("1", matcher.group(1));
      assertEquals(" a", matcher.group(2));
      assertFalse(matcher.find());
   }

   public void testCalloutDef()
   {
      assertMatches(CALLOUT_DEF_PATTERN, "// =1= a", "// =1= a");
      assertMatches(CALLOUT_DEF_PATTERN, " // =1= a", " // =1= a");

      //
      assertMatches(CALLOUT_DEF_PATTERN, "// =1=");
      assertMatches(CALLOUT_DEF_PATTERN, "// =1= ");
      assertMatches(CALLOUT_DEF_PATTERN, "// =1=a");
      assertMatches(CALLOUT_DEF_PATTERN, "a// =1= a");
   }

   public void testCalloutDef_Multiline()
   {
      String s = "a\n// =1= a\n=";
      Matcher matcher = CALLOUT_DEF_PATTERN.matcher(s);
      assertTrue(matcher.find());
      assertEquals("// =1= a", matcher.group(0));
      assertEquals("1", matcher.group(1));
      assertEquals("a", matcher.group(2));
      assertFalse(matcher.find());
   }

   public void testCalloutDef_Groups()
   {
      String s = "abc\n// =1= a b";
      Matcher matcher = CALLOUT_DEF_PATTERN.matcher(s);
      assertTrue(matcher.find());
      assertEquals("// =1= a b", matcher.group(0));
      assertEquals("1", matcher.group(1));
      assertEquals("a b", matcher.group(2));
      assertFalse(matcher.find());
   }

   private void assertMatches(Pattern pattern, String s, String... matches)
   {
      Matcher matcher = pattern.matcher(s);
      for (String match : matches)
      {
         assertTrue(s + " should have matched /" + match + "/", matcher.find());
         assertEquals(match, matcher.group(0));
      }
      if (matcher.find())
      {
         fail("Was not expecting match " + matcher.group(0));
      }
   }
}
