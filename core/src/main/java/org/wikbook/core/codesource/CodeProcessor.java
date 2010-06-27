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

package org.wikbook.core.codesource;

import org.wikbook.codesource.CodeSource;
import org.wikbook.codesource.CodeSourceBuilder;
import org.wikbook.codesource.CodeSourceBuilderContext;
import org.wikbook.codesource.SignedMemberSource;
import org.wikbook.codesource.TypeSource;
import org.wikbook.core.Utils;
import org.wikbook.text.TextArea;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class CodeProcessor
{
   /** . */
   public static final String WHITE_NON_CR = "[ \t\\x0B\f\r]";

   /** . */
   public static final Pattern CALLOUT_ANCHOR_PATTERN = Pattern.compile(
      "//" + WHITE_NON_CR + "*" + "<([0-9]*)>" + "(.*)$", Pattern.MULTILINE);

   /** . */
   public static final Pattern CALLOUT_DEF_PATTERN = Pattern.compile(
      "^" + WHITE_NON_CR + "*//" + WHITE_NON_CR + "*" + "=([0-9]+)=" + WHITE_NON_CR + "(\\S.*)$", Pattern.MULTILINE);

   /** . */
   public static final Pattern BEGIN_CHUNK_PATTERN = Pattern.compile(
      "^" + WHITE_NON_CR + "*//" + WHITE_NON_CR + "*" + "-([0-9]+)-" + WHITE_NON_CR + "*" + "$");

   /** . */
   public static final Pattern BLANK_LINE_PATTERN = Pattern.compile(
      "^" + WHITE_NON_CR + "*" + "$");

   /** . */
   public static final Pattern BILTO = Pattern.compile(
      "^(?:" +
         "(?:" + WHITE_NON_CR + "*//" + WHITE_NON_CR + "*" + "-([0-9]+)-" + WHITE_NON_CR + "*)" +
         "|" +
         "(?:" + WHITE_NON_CR + "*)" +
         ")$", Pattern.MULTILINE);

   /** . */
   public static final Pattern JAVA_INCLUDE_PATTERN = Pattern.compile(
      "\\{" + "@(include|javadoc)" + "\\s+" + "([^\\s]+)" + "\\s*" + "(\\{[0-9]+(?:,[0-9]+)*\\})?" + "\\s*" + "\\}"
   );

   /** The last index found. */
   int calloutIndex = 0;

   /** The index sub increments. */
   int calloutSubIndex = 0;

   /**
    * Updates the current callout index with the provided id. When the calloutId argument is an empty string, the last
    * found index is incremented. When the calloutId is an integer string, the last callout index is updated.
    *
    * @param calloutId the callout id.
    * @return the effective id value to use
    */
   private String updateId(String calloutId)
   {
      if (calloutId.length() == 0)
      {
         return "" + (calloutIndex * 1000 + calloutSubIndex++);
      }
      else
      {
         calloutIndex = Integer.parseInt(calloutId);
         calloutSubIndex = 1;
         return "" + calloutIndex * 1000;
      }
   }

   /**
    * Convert the calloutId argument as an effective callout.
    *
    * @param calloutId the calloutId
    * @return the effective id value to use
    */
   private String getId(String calloutId)
   {
      if (calloutId.length() == 0)
      {
         throw new AssertionError();
      }
      else
      {
         int index = Integer.parseInt(calloutId);
         return "" + index * 1000;
      }
   }

   private void printJavaLine(String s, CodeContext ctx)
   {
      // Process all callout definitions
      Matcher coDefMatcher = CALLOUT_DEF_PATTERN.matcher(s);
      int pre = 0;
      StringBuilder buf = new StringBuilder();
      while (coDefMatcher.find())
      {
         String calloutId = coDefMatcher.group(1);
         String calloutText = coDefMatcher.group(2).trim();

         //
         String id = getId(calloutId);

         //
         buf.append(s, pre, coDefMatcher.start());

         //
         ctx.setCallout(id, calloutText);

         //
         pre = coDefMatcher.end();
      }
      buf.append(s, pre, s.length());
      s = buf.toString();

      //
      TextArea ta = new TextArea(s);
      Matcher matcher = CALLOUT_ANCHOR_PATTERN.matcher(s);
      int prev = 0;
      while (matcher.find())
      {
         String calloutId = matcher.group(1);

         //
         String id = updateId(calloutId);

         //
         ctx.writeContent(ta.clip(ta.position(prev), ta.position(matcher.start())));

         //
         ctx.writeCallout(id);

         // Determine if we have callout text associated
         String text = matcher.group(2);
         if (!text.matches("\\s*"))
         {
            ctx.setCallout(id, text.trim());
         }

         // Iterate to next
         prev = matcher.end();
      }
      ctx.writeContent(ta.clip(ta.position(prev)));
   }

   private void printJavaSource(
      SignedMemberSource methodSource,
      CodeContext ctx,
      Set<String> chunkIds)
   {
      String source = methodSource.getClip();

      // Find the method curly braces
      int from = source.indexOf('{');
      int to = source.lastIndexOf('}');
      source = source.substring(from + 1, to);

      // Split lines
      printJavaSource(source, ctx, chunkIds);
   }

   private void printJavaSource(
      String source,
      CodeContext ctx)
   {
      // Split lines
      for (Iterator<String> i = Utils.split(source).iterator(); i.hasNext();)
      {
         String line = i.next();
         Matcher matcher = BEGIN_CHUNK_PATTERN.matcher(line);
         if (matcher.matches())
         {
            // Remove line
         }
         else
         {
            if (i.hasNext())
            {
               printJavaLine(line + "\n", ctx);
            }
            else
            {
               printJavaLine(line, ctx);
            }
         }
      }
   }

   private void printJavaSource(
      String source,
      CodeContext ctx,
      Set<String> chunkIds)
   {
      // Split lines
      boolean matches = false;
      for (String line : Utils.split(source))
      {
         if (BLANK_LINE_PATTERN.matcher(line).matches())
         {
            matches = false;
         }
         else
         {
            Matcher matcher = BEGIN_CHUNK_PATTERN.matcher(line);
            if (matcher.matches())
            {
               String chunkId = matcher.group(1);
               matches = chunkIds == null || chunkIds.contains(chunkId);
            }
            else
            {
               if (matches)
               {
                  printJavaLine(line + "\n", ctx);
               }
            }
         }
      }
   }

   public void parse(String s, final CodeContext ctx)
   {
      int prev = 0;
      Matcher matcher = JAVA_INCLUDE_PATTERN.matcher(s);
      while (matcher.find())
      {
         JavaCodeLink l = JavaCodeLink.parse(matcher.group(2));
         CodeSourceBuilder builder = new CodeSourceBuilder(new CodeSourceBuilderContext()
         {
            public InputStream getResource(String path)
            {
               try
               {
                  return ctx.resolveResources(path);
               }
               catch (IOException e)
               {
                  e.printStackTrace();
               }
               return null;
            }
         });

         //
         printJavaSource(s.substring(prev, matcher.start()), ctx);

         TypeSource typeSource = builder.buildClass(l.getFQN());
         CodeSource source;
         if (l.getMember() != null)
         {
            source = typeSource.findMember(l.getMember());
         }
         else
         {
            source = typeSource;
         }

         //
         if (source != null)
         {
            if ("include".equals(matcher.group(1)))
            {
               if (matcher.group(3) != null)
               {
                  String subset = matcher.group(3);
                  String a = subset.substring(1, subset.length() - 1);
                  String[] ids = a.split(",");
                  printJavaSource((SignedMemberSource)source, ctx, new HashSet<String>(Arrays.asList(ids)));
               }
               else
               {
                  printJavaSource(source.getClip(), ctx);
               }
            }
            else if ("javadoc".equals(matcher.group(1)) && source.getJavaDoc() != null)
            {
               String javadoc = source.getJavaDoc();
               ctx.writeContent(javadoc);
            }
         }
         else
         {
            ctx.writeContent("Could not locate the " + l + " source");
         }

         //
         prev = matcher.end();
      }

      //
      printJavaSource(s.substring(prev), ctx);
   }
}
