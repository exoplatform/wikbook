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
import org.wikbook.core.BookBuilder;
import org.wikbook.core.BookBuilderContext;
import org.wikbook.core.WikbookException;
import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.content.block.AdmonitionElement;
import org.wikbook.core.model.content.block.LanguageSyntax;
import org.wikbook.core.model.content.block.ListKind;
import org.wikbook.core.model.content.inline.TextFormat;
import org.wikbook.core.xml.OutputFormat;
import org.wikbook.core.xml.XML;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.listener.HeaderLevel;
import org.xwiki.rendering.listener.Image;
import org.xwiki.rendering.listener.Link;
import org.xwiki.rendering.listener.ListType;
import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.syntax.Syntax;

import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class XDOMTransformer implements Listener
{

   /** . */
   private static final Set<String> admonitions = new HashSet<String>();

   /** . */
   private static final EnumMap<Format, TextFormat> formatMapping = new EnumMap<Format, TextFormat>(Format.class);

   /** . */
   private static final EnumMap<ListType, ListKind> mapping = new EnumMap<ListType, ListKind>(ListType.class);

   static
   {
      mapping.put(ListType.BULLETED, ListKind.BULLETED);
      mapping.put(ListType.NUMBERED, ListKind.NUMBERED);
   }

   static
   {
      admonitions.add("note");
      admonitions.add("important");
      admonitions.add("tip");
      admonitions.add("caution");
      admonitions.add("warning");
   }

   static
   {
      formatMapping.put(Format.BOLD, TextFormat.BOLD);
      formatMapping.put(Format.ITALIC, TextFormat.ITALIC);
      formatMapping.put(Format.MONOSPACE, TextFormat.MONOSPACE);
      formatMapping.put(Format.SUPERSCRIPT, TextFormat.SUPERSCRIPT);
      formatMapping.put(Format.SUBSCRIPT, TextFormat.SUBSCRIPT);
      formatMapping.put(Format.UNDERLINED, TextFormat.UNDERLINE);
      formatMapping.put(Format.STRIKEDOUT, TextFormat.STRIKE);
   }

   /** . */
   final LinkedList<String> syntaxStack;

   /** . */
   final BookBuilder builder;

   /** . */
   final BookBuilderContext context;

   public XDOMTransformer(final BookBuilderContext context) throws IOException, ClassNotFoundException
   {


      //
      this.context = context;
      this.syntaxStack = new LinkedList<String>();
      this.builder = new BookBuilder(context)
      {
         @Override
         protected void load(Reader reader)
         {
            WikiLoader loader = new WikiLoader(context);
            Block block = loader.load(reader, syntaxStack.getLast());
            block.traverse(XDOMTransformer.this);
         }
      };
   }

   public DocbookElement transform(Block block)
   {
      builder.beginBook();
      block.traverse(this);
      builder.endBook();
      return builder.getBook();
   }

   public void beginDocument(Map<String, String> parameters)
   {
   }

   public void endDocument(Map<String, String> parameters)
   {
   }

   public void beginParagraph(Map<String, String> parameters)
   {
      builder.beginParagraph();
   }

   public void endParagraph(Map<String, String> parameters)
   {
      builder.endParagraph();
   }

   //

   public void beginSection(Map<String, String> parameters)
   {
      builder.beginSection();
   }

   public void endSection(Map<String, String> parameters)
   {
      builder.endSection();
   }

   public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
   {
      builder.beginHeader();
   }

   public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
   {
      builder.endHeader();
   }

   //

   public void onWord(String word)
   {
      builder.onText(word);
   }

   public void onSpace()
   {
      builder.onText(" ");
   }

   public void onNewLine()
   {
      // For now we dont take in account line breaks
      builder.onText(" ");
   }

   public void onSpecialSymbol(char symbol)
   {
      // For now it will be ok
      builder.onText(Character.toString(symbol));
   }

   public void beginFormat(Format format, Map<String, String> parameters)
   {
      TextFormat textFormat = formatMapping.get(format);
      if (textFormat == null)
      {
         context.onValidationError("Format " + format + " is not yet supported");
      }
      else
      {
         builder.beginFormat(textFormat);
      }
   }

   public void endFormat(Format format, Map<String, String> parameters)
   {
      TextFormat textFormat = formatMapping.get(format);
      if (textFormat == null)
      {
         context.onValidationError("Format " + format + " is not yet supported");
      }
      else
      {
         builder.endFormat(textFormat);
      }
   }

   //

   public void beginList(ListType listType, Map<String, String> parameters)
   {
      String style = parameters.get("style");
      ListKind lk = mapping.get(listType);
      builder.beginList(lk, style);
   }

   public void endList(ListType listType, Map<String, String> parameters)
   {
      String style = parameters.get("style");
      ListKind lk = mapping.get(listType);
      builder.endList(lk, style);
   }

   public void beginListItem()
   {
      builder.beginListItem();
   }

   public void endListItem()
   {
      builder.endListItem();
   }

   //

   public void onMacro(String id, Map<String, String> macroParameters, String content, boolean isInline)
   {
      _onMacro(id, macroParameters, content, builder.isInline());
   }

   private void _onMacro(String id, Map<String, String> macroParameters, String content, boolean isInline)
   {
      if (admonitions.contains(id))
      {
         WikiLoader loader = new WikiLoader(context);
         Block block = loader.load(new StringReader(content), null);

         //
         builder.beginAdmonition(id);
         block.traverse(this);
         builder.endAdmonition(id);
      }
      else if ("screen".equals(id))
      {
         builder.beginScreen();
         builder.onText(content);
         builder.endScreen();
      }
      else if ("anchor".equals(id))
      {
         String anchor = macroParameters.get("id");
         builder.onAnchor(anchor);
      }
      else if ("docbook".equals(id))
      {
         try
         {
            Transformer transformer = XML.createTransformer(new OutputFormat(2, true));

            // Perform identity transformation
            DOMResult result = new DOMResult();

            //
            transformer.transform(new StreamSource(new StringReader(content)), result);

            //
            builder.onDocbook(((Document)result.getNode()).getDocumentElement());
         }
         catch (Exception e)
         {
            throw new WikbookException(e);
         }
      }
      else if ("code".equals(id) || "java".equals(id) || "xml".equals(id))
      {
         if (isInline)
         {
            builder.beginFormat(TextFormat.CODE);
            builder.onText(content);
            builder.endFormat(TextFormat.CODE);
         }
         else
         {
            LanguageSyntax languageSyntax = LanguageSyntax.UNKNOWN;
            if ("java".equals(id))
            {
               languageSyntax = LanguageSyntax.JAVA;
            }
            else if ("xml".equals(id))
            {
               languageSyntax = LanguageSyntax.XML;
            }
            else
            {
               String language = macroParameters.get("language");
               if ("java".equalsIgnoreCase(language))
               {
                  languageSyntax = LanguageSyntax.JAVA;
               }
               else if ("xml".equalsIgnoreCase(language))
               {
                  languageSyntax = LanguageSyntax.XML;
               }
            }

            //
            Integer indent = null;
            if (macroParameters.get("indent") != null)
            {
               try
               {
                  indent = Integer.parseInt(macroParameters.get("indent"));
               }
               catch (NumberFormatException e)
               {
                  e.printStackTrace();
               }
            }

            //
            builder.onCode(
               languageSyntax,
               indent,
               content
            );
         }
      }
      else if ("example".equals(id))
      {
         if (isInline)
         {
            throw new UnsupportedOperationException();
         }
         WikiLoader loader = new WikiLoader(context);
         Block block = loader.load(new StringReader(content), syntaxStack.getLast());
         builder.beginExample(macroParameters.get("title"));
         block.traverse(this);
         builder.endExample(macroParameters.get("title"));
      }
      else
      {
         context.onValidationError("Unsupported macro " + id);
      }
   }

   //

   public void beginLink(Link link, boolean isFreeStandingURI, Map<String, String> parameters)
   {
      switch (link.getType())
      {
         case URI:
            String ref = link.getReference();
            org.wikbook.core.model.content.inline.LinkType type;
            if (ref.startsWith("#"))
            {
               ref = ref.substring(1);
               type = org.wikbook.core.model.content.inline.LinkType.ANCHOR;
            }
            else
            {
               type = org.wikbook.core.model.content.inline.LinkType.URL;
            }
            builder.beginLink(type, ref);
            break;
         default:
            context.onValidationError("Unsupported link type " + link.getType());
      }
   }

   public void endLink(Link link, boolean isFreeStandingURI, Map<String, String> parameters)
   {
      switch (link.getType())
      {
         case URI:
            String ref = link.getReference();
            org.wikbook.core.model.content.inline.LinkType type;
            if (ref.startsWith("#"))
            {
               ref = ref.substring(1);
               type = org.wikbook.core.model.content.inline.LinkType.ANCHOR;
            }
            else
            {
               type = org.wikbook.core.model.content.inline.LinkType.URL;
            }
            builder.endLink(type, ref);
            break;
         default:
            context.onValidationError("Unsupported link type " + link.getType());
      }
   }

   //

   public void beginTable(Map<String, String> parameters)
   {
      String title = parameters.get("title");
      builder.beginTable(title);
   }

   public void beginTableRow(Map<String, String> parameters)
   {
      builder.beginTableRow(parameters);
   }

   public void beginTableCell(Map<String, String> parameters)
   {
      builder.beginTableCell(parameters);
   }

   public void beginTableHeadCell(Map<String, String> parameters)
   {
      builder.beginTableHeadCell(parameters);
   }

   public void endTable(Map<String, String> parameters)
   {
      String title = parameters.get("title");
      builder.endTable(title);
   }

   public void endTableRow(Map<String, String> parameters)
   {
      builder.endTableRow(parameters);
   }

   public void endTableCell(Map<String, String> parameters)
   {
      builder.endTableCell(parameters);
   }

   public void endTableHeadCell(Map<String, String> parameters)
   {
      builder.endTableHeadCell(parameters);
   }

   //

   public void beginGroup(Map<String, String> parameters)
   {
      builder.beginGroup();
   }

   public void endGroup(Map<String, String> parameters)
   {
      builder.endGroup();
   }

   //

   public void beginDefinitionList(Map<String, String> parameters)
   {
      String title = parameters.get("title");
      builder.beginDefinitionList(title);
   }

   public void beginDefinitionTerm()
   {
      builder.beginDefinitionTerm();
   }

   public void endDefinitionTerm()
   {
      builder.endDefinitionTerm();
   }

   public void beginDefinitionDescription()
   {
      builder.beginDefinitionDescription();
   }

   public void endDefinitionDescription()
   {
      builder.endDefinitionDescription();
   }

   public void endDefinitionList(Map<String, String> parameters)
   {
      String title = parameters.get("title");
      builder.endDefinitionList(title);
   }

   //

   public void beginMacroMarker(String name, Map<String, String> macroParameters, String content, boolean isInline)
   {
      context.onValidationError("Not supported");
   }

   public void endMacroMarker(String name, Map<String, String> macroParameters, String content, boolean isInline)
   {
      context.onValidationError("Not supported");
   }

   public void beginQuotation(Map<String, String> parameters)
   {
      builder.beginQuotation();
   }

   public void endQuotation(Map<String, String> parameters)
   {
      builder.endQuotation();
   }

   public void beginQuotationLine()
   {
   }

   public void endQuotationLine()
   {
   }

   public void onId(String name)
   {
      context.onValidationError("Not supported");
   }

   public void onHorizontalLine(Map<String, String> parameters)
   {
   }

   public void onEmptyLines(int count)
   {
      // Nothing to do really
   }

   public void onVerbatim(String protectedString, boolean isInline, Map<String, String> parameters)
   {
      builder.onVerbatim(protectedString);
   }

   public void onRawText(String rawContent, Syntax syntax)
   {
      context.onValidationError("Not supported");
   }

   public void onImage(Image image, boolean isFreeStandingURI, Map<String, String> parameters)
   {
      builder.onImage(image.getName(), parameters);
   }
}