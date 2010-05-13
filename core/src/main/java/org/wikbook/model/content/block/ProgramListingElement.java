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

package org.wikbook.model.content.block;

import org.wikbook.ResourceType;
import org.wikbook.WikletContext;
import org.wikbook.codesource.BodySource;
import org.wikbook.codesource.CodeSourceBuilder;
import org.wikbook.codesource.CodeSourceBuilderContext;
import org.wikbook.codesource.TypeSource;
import org.wikbook.text.TextArea;
import org.wikbook.xml.ElementEmitter;
import org.wikbook.xml.OutputFormat;
import org.wikbook.xml.XML;
import org.wikbook.xml.XMLEmitter;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ProgramListingElement extends BlockElement
{

   /** . */
   public static final String WHITE_NON_CR = "[ \t\\x0B\f\r]";

   /** . */
   public static final Pattern CALLOUT_ANCHOR_PATTERN = Pattern.compile(
      "//" + WHITE_NON_CR + "*" + "<([0-9]+)>" + "(.*)$", Pattern.MULTILINE);

   /** . */
   public static final Pattern CALLOUT_DEF_PATTERN = Pattern.compile(
      "^" + WHITE_NON_CR + "*//" + WHITE_NON_CR + "*" + "=([0-9]+)=" + WHITE_NON_CR + "(\\S.*)$", Pattern.MULTILINE);

   /** . */
   public static final Pattern SEPARATOR_PATTERN = Pattern.compile(
      "^" + WHITE_NON_CR + "*//" + WHITE_NON_CR + "*" + "-([0-9]+)-" + WHITE_NON_CR + "*$", Pattern.MULTILINE);

   /** . */
   public static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("^" + WHITE_NON_CR + "*$", Pattern.MULTILINE);

   /** . */
   public static final Pattern BILTO = Pattern.compile(
      "^(?:" +
         "(?:" + WHITE_NON_CR + "*//" + WHITE_NON_CR + "*" + "-([0-9]+)-" + WHITE_NON_CR + "*)" +
         "|" +
         "(?:" + WHITE_NON_CR +  "*)" +
      ")$", Pattern.MULTILINE);

   /** . */
   private static final Pattern JAVA_INCLUDE_PATTERN = Pattern.compile(
      "\\{" + "@(include|javadoc)" + "\\s+" + "([^\\s]+)" + "\\s*" + "(\\{[0-9]+(?:,[0-9]+)*\\})?" + "\\s*" + "\\}"
   );

   /** . */
   private final String language;

   /** . */
   private final Integer indent;

   /** . */
   private final String content;

   /** . */
   private final WikletContext context;

   public ProgramListingElement(
      WikletContext context,
      String language,
      Integer indent,
      String content)
   {
      this.context = context;
      this.language = language;
      this.indent = indent;
      this.content = content;
   }

   @Override
   public void writeTo(XMLEmitter xml)
   {
      ElementEmitter programListingCoXML = xml.element("programlistingco");
      ElementEmitter programListingXML = programListingCoXML.element("programlisting");
      CodeLanguage codeLanguage = CodeLanguage.UNKNOWN;
      if (language != null)
      {
         if ("java".equalsIgnoreCase(language))
         {
            codeLanguage = CodeLanguage.JAVA;
         }
         else if ("xml".equalsIgnoreCase(language))
         {
            codeLanguage = CodeLanguage.XML;
         }
      }

      String bilto = content;
      switch (codeLanguage)
      {
         case XML:
            if (indent != null)
            {
               try
               {
                  Transformer transformer = XML.createTransformer(new OutputFormat(indent, true));

                  // Output buffer
                  StringWriter writer = new StringWriter();

                  // Perform identity transformation
                  transformer.transform(new StreamSource(new StringReader(content)), new StreamResult(writer));

                  //
                  bilto = writer.toString();
               }
               catch (Exception e)
               {
                  e.printStackTrace();
                  bilto = "Exception occured, see logs";
               }
            }
            programListingXML.content(bilto, true);
            break;
         case UNKNOWN:
            programListingXML.content(bilto, true);
            break;
         case JAVA:

            // TreeMap will sort by callout id
            TreeMap<String, Callout> callouts = new TreeMap<String, Callout>();

            //
            parse(bilto, programListingXML, callouts);

            //
            if (callouts.size() > 0)
            {
               ElementEmitter calloutListXML = programListingCoXML.element("calloutlist");
               for (Map.Entry<String, Callout> callout : callouts.entrySet())
               {
                  if (callout.getValue().text != null)
                  {
                     StringBuffer sb = new StringBuffer();
                     for (String coId : callout.getValue().ids)
                     {
                        if (sb.length() > 0)
                        {
                           sb.append(" ");
                        }
                        sb.append(coId);
                     }
                     calloutListXML.element("callout").withAttribute("arearefs", sb.toString()).element("para").content(callout.getValue().text, true);
                  }
               }
            }

            //
            break;
      }
   }

   private void printJavaSource(
      String s,
      XMLEmitter programListingElt,
      Map<String, Callout> callouts,
      Set<String> chunkIds)
   {
      Matcher chunkMatcher = BILTO.matcher(s);

      //
      String chunkId = null;
      int pre = 0;
      while (true)
      {
         String chunk;
         String nextChunkId;
         boolean found = chunkMatcher.find();
         if (found)
         {
            chunk = s.substring(pre, chunkMatcher.start());
            nextChunkId = chunkMatcher.group(1);
         }
         else
         {
            chunk = s.substring(pre);
            nextChunkId = null;
         }

         //
         if (chunkId != null && chunkIds.contains(chunkId))
         {
            printJavaSource(chunk, programListingElt, callouts);
         }

         //
         if (!found)
         {
            break;
         }

         //
         pre = chunkMatcher.end();
         chunkId = nextChunkId;
      }

   }

   private void printJavaSource(String s, XMLEmitter programListingElt, Map<String, Callout> callouts)
   {
      // Process all callout definitions
      Matcher coDefMatcher = CALLOUT_DEF_PATTERN.matcher(s);
      int pre = 0;
      StringBuilder buf = new StringBuilder();
      while (coDefMatcher.find())
      {
         String id = coDefMatcher.group(1);
         String text = coDefMatcher.group(2).trim();

         //
         buf.append(s, pre, coDefMatcher.start());

         //
         Callout callout = callouts.get(id);
         if (callout == null)
         {
            callout = new Callout();
            callouts.put(id, callout);
         }

         // We override
         callout.text = text;

         //
         pre = coDefMatcher.end();
      }
      buf.append(s, pre, s.length());
      s = buf.toString();

      //
      TextArea ta = new TextArea(s);
      Random random = new Random();
      Matcher matcher = CALLOUT_ANCHOR_PATTERN.matcher(s);
      int prev = 0;
      while (matcher.find())
      {
         String id = matcher.group(1);

         //
         programListingElt.content(ta.clip(ta.position(prev), ta.position(matcher.start())), true);

         //
         Callout callout = callouts.get(id);
         if (callout == null)
         {
            callout = new Callout();
            callouts.put(id, callout);
         }

         //
         String coId = "" + Math.abs(random.nextLong());
         callout.ids.add(coId);
         programListingElt.element("co").withAttribute("id", coId);

         // Determine if we have callout text associated
         String text = matcher.group(2);
         if (!text.matches("\\s*"))
         {
            callout.text = text.trim();
         }

         // Iterate to next
         prev = matcher.end();
      }
      programListingElt.content(ta.clip(ta.position(prev)), true);
   }

   private void parse(String s, XMLEmitter programListingElt, Map<String, Callout> callouts)
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
                  List<URL> list = context.resolveResources(ResourceType.JAVA, path);
                  if (list.size() > 0)
                  {
                     return list.get(0).openStream();
                  }
               }
               catch (IOException e)
               {
                  e.printStackTrace();
               }
               return null;
            }
         });
         TypeSource typeSource = builder.buildClass(l.getFQN());
         BodySource source;
         if (l.getMember() != null)
         {
            source = typeSource.findMember(l.getMember());
         }
         else
         {
            source = typeSource;
         }

         //
         printJavaSource(s.substring(prev, matcher.start()), programListingElt, callouts);

         //
         if ("include".equals(matcher.group(1)))
         {
            if (matcher.group(3) != null)
            {
               String subset = matcher.group(3);
               String a = subset.substring(1, subset.length() - 1);
               String[] ids = a.split(",");
               printJavaSource(source.getClip(), programListingElt, callouts, new HashSet<String>(Arrays.asList(ids)));
            }
            else
            {
               printJavaSource(source.getClip(), programListingElt, callouts);
            }
         }
         else if ("javadoc".equals(matcher.group(1)) && source.getJavaDoc() != null)
         {
            String javadoc = source.getJavaDoc();
            programListingElt.content(javadoc, true);
         }

         //
         prev = matcher.end();
      }

      //
      printJavaSource(s.substring(prev), programListingElt, callouts);
   }

   private static class Callout
   {

      /** . */
      private String text;

      /** . */
      private final LinkedList<String> ids = new LinkedList<String>();

   }

   /**
    * A chunk of code.
    */
   private static class Chunk
   {

      /** . */
      private final int id;

      /** . */
      private final String text;

      private Chunk(int id, String text)
      {
         this.id = id;
         this.text = text;
      }
   }

}
