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

import org.wikbook.codesource.Anchor;
import org.wikbook.codesource.BodySource;
import org.wikbook.codesource.CodeSourceBuilder;
import org.wikbook.codesource.TypeSource;
import org.wikbook.text.Position;
import org.wikbook.text.TextArea;
import org.wikbook.xml.ElementEmitter;
import org.wikbook.xml.OutputFormat;
import org.wikbook.xml.XML;
import org.wikbook.xml.XMLEmitter;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ProgramListingElement extends BlockElement
{

   private static final Pattern JAVA_INCLUDE_PATTERN = Pattern.compile(
      "\\{" + "@(include|javadoc)\\s+([^\\s]+)" + "\\s*\\}"
   );

   /** . */
   private final String language;

   /** . */
   private final Integer indent;

   /** . */
   private final String content;

   public ProgramListingElement(String language, Integer indent, String content)
   {
      this.language = language;
      this.indent = indent;
      this.content = content;
   }

   @Override
   public void writeTo(XMLEmitter xml)
   {
      ElementEmitter programListingXML = xml.element("programlisting");
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

            //
            parse(bilto, programListingXML);

            //
            break;
      }
   }

   /** . */
   private static final String WHITE_NON_CR = "[ \t\\x0B\f\r]";

   /** . */
   private static final Pattern LINE_COMMENT = Pattern.compile("//\\s*<([^>]+)>" + WHITE_NON_CR + "*");

   private static void parse2(String s, XMLEmitter elt)
   {
      TextArea ta = new TextArea(s);

      Matcher matcher = LINE_COMMENT.matcher(s);
      int prev = 0;
      while (matcher.find())
      {
         elt.content(ta.clip(ta.position(prev), ta.position(matcher.start())), true);
         elt.element("co").withAttribute("id", matcher.group(1)).withAttribute("linkends", matcher.group(1));
         prev = matcher.end();
      }
      elt.content(ta.clip(ta.position(prev)), true);
   }

   public static void parse(String s, XMLEmitter elt)
   {
      int prev = 0;
      Matcher matcher = JAVA_INCLUDE_PATTERN.matcher(s);
      while (matcher.find())
      {
         JavaCodeLink l = JavaCodeLink.parse(matcher.group(2));
         CodeSourceBuilder builder = new CodeSourceBuilder();
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
         parse2(s.substring(prev, matcher.start()), elt);

         //
         if ("include".equals(matcher.group(1)))
         {
            Position previous = Position.get(0, 0);
            TextArea ta = new TextArea(source.getClip());
            for (Anchor anchor : source.getAnchors())
            {
               elt.content(ta.clip(previous, anchor.getPosition()));
               elt.element("co").withAttribute("id", anchor.getId()).withAttribute("linkends", anchor.getId());
               previous = anchor.getPosition();
            }
            elt.content(ta.clip(previous));
         }
         else if ("javadoc".equals(matcher.group(1)) && source.getJavaDoc() != null)
         {
            String javadoc = source.getJavaDoc();
            elt.content(javadoc, true);
         }

         //
         prev = matcher.end();
      }

      //
      parse2(s.substring(prev), elt);
   }
}
