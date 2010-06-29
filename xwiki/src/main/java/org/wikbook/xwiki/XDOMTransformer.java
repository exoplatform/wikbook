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
import org.wikbook.core.WikletContext;
import org.wikbook.core.model.DocbookContext;
import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.Loader;
import org.wikbook.core.model.content.block.AdmonitionElement;
import org.wikbook.core.model.content.block.BlockQuotationElement;
import org.wikbook.core.model.content.block.DOMElement;
import org.wikbook.core.model.content.block.ExampleElement;
import org.wikbook.core.model.content.block.GroupElement;
import org.wikbook.core.model.content.block.ImageElement;
import org.wikbook.core.model.content.block.LanguageSyntax;
import org.wikbook.core.model.content.block.ListElement;
import org.wikbook.core.model.content.block.ListItemElement;
import org.wikbook.core.model.content.block.ListKind;
import org.wikbook.core.model.content.block.ParagraphElement;
import org.wikbook.core.model.content.block.ProgramListingElement;
import org.wikbook.core.model.content.block.ScreenElement;
import org.wikbook.core.model.content.block.TableElement;
import org.wikbook.core.model.content.block.list.TermElement;
import org.wikbook.core.model.content.block.list.VariableListElement;
import org.wikbook.core.model.content.inline.AnchorElement;
import org.wikbook.core.model.content.inline.FormatElement;
import org.wikbook.core.model.content.inline.InlineElement;
import org.wikbook.core.model.content.inline.LinkElement;
import org.wikbook.core.model.content.inline.TextElement;
import org.wikbook.core.model.content.inline.TextFormat;
import org.wikbook.core.model.structural.BookElement;
import org.wikbook.core.model.structural.ComponentElement;
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
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
   private final WikletContext context;

   /** . */
   private DocbookContext bookContext;

   /** . */
   private BookElement book;

   /** . */
   final LinkedList<String> syntaxStack;

   /** . */
   private final Loader loader = new Loader()
   {
      public void load(Reader reader)
      {
         WikiLoader loader = new WikiLoader(context);
         Block block = loader.load(reader, syntaxStack.getLast());
         block.traverse(XDOMTransformer.this);
      }
   };

   public XDOMTransformer(WikletContext context) throws IOException, ClassNotFoundException
   {
      this.context = context;
      this.syntaxStack = new LinkedList<String>();
   }

   public DocbookElement transform(Block block)
   {
      book = new BookElement();
      bookContext = new DocbookContext(book);
      block.traverse(this);
      book.close();
      return book;
   }

   public void beginDocument(Map<String, String> parameters)
   {
   }

   public void endDocument(Map<String, String> parameters)
   {
   }

   public void beginParagraph(Map<String, String> parameters)
   {
      book.push(new ParagraphElement());
   }

   public void endParagraph(Map<String, String> parameters)
   {
      book.merge();
   }

   //

   public void beginSection(Map<String, String> parameters)
   {
      book.push(new ComponentElement());
   }

   public void endSection(Map<String, String> parameters)
   {
      book.merge();
   }

   public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
   {
      ((ComponentElement)book.peek()).beginTitle();
   }

   public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
   {
      ((ComponentElement)book.peek()).endTitle();
   }

   //

   public void onWord(String word)
   {
      book.merge(new TextElement(word));
   }

   public void onSpace()
   {
      book.merge(new TextElement(" "));
   }

   public void onNewLine()
   {
      // For now we dont take in account line breaks
      book.merge(new TextElement(" "));
   }

   public void onSpecialSymbol(char symbol)
   {
      // For now it will be ok
      book.merge(new TextElement(Character.toString(symbol)));
   }

   public void beginFormat(Format format, Map<String, String> parameters)
   {
      TextFormat textFormat = formatMapping.get(format);
      if (textFormat == null)
      {
         throw new UnsupportedOperationException("Format " + format + " is not yet handled");
      }
      book.push(new FormatElement(textFormat));
   }

   public void endFormat(Format format, Map<String, String> parameters)
   {
      book.merge();
   }

   //

   /** Trivial mapping. */
   private static final EnumMap<ListType, ListKind> mapping = new EnumMap<ListType, ListKind>(ListType.class);

   static
   {
      mapping.put(ListType.BULLETED, ListKind.BULLETED);
      mapping.put(ListType.NUMBERED, ListKind.NUMBERED);
   }

   public void beginList(ListType listType, Map<String, String> parameters)
   {
      String style = parameters.get("style");
      ListKind lk = mapping.get(listType);
      book.push(new ListElement(lk, style));
   }

   public void endList(ListType listType, Map<String, String> parameters)
   {
      book.merge();
   }

   public void beginListItem()
   {
      ((ListElement)book.peek()).beginItem();
   }

   public void endListItem()
   {
      ((ListElement)book.peek()).endItem();
   }

   //

   public void onMacro(String id, Map<String, String> macroParameters, String content, boolean isInline)
   {
      // Determine inline according to the docbook context and not according to what the parser tell us
      DocbookElement elt = book.peek();
      boolean inline = elt instanceof InlineElement;

      //
      _onMacro(id, macroParameters, content, inline);
   }

   private void _onMacro(String id, Map<String, String> macroParameters, String content, boolean isInline)
   {
      if (admonitions.contains(id))
      {
/*
         if (isInline)
         {
            throw new UnsupportedOperationException("todo");
         }
         else
         {
         }
*/
         AdmonitionElement admonitionElt = new AdmonitionElement(id);

         //
         WikiLoader loader = new WikiLoader(context);
         Block block = loader.load(new StringReader(content), null);

         //
         book.push(admonitionElt);
         block.traverse(this);
         book.merge();
      }
      else if ("screen".equals(id))
      {
         ScreenElement screenElt = new ScreenElement();
         screenElt.append(new TextElement(content));
         book.merge(screenElt);
      }
      else if ("anchor".equals(id))
      {
         String anchor = macroParameters.get("id");
         book.merge(new AnchorElement(anchor));
      }
      else if ("docbook".equals(id))
      {
         try
         {
            Transformer transformer = XML.createTransformer(new OutputFormat(2, true));

            // Output buffer
            StringWriter writer = new StringWriter();

            // Perform identity transformation
            DOMResult result = new DOMResult();

            //
            transformer.transform(new StreamSource(new StringReader(content)), result);

            //
            book.merge(new DOMElement(((Document)result.getNode()).getDocumentElement()));
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
      else if ("code".equals(id) || "java".equals(id) || "xml".equals(id))
      {
         if (isInline)
         {
            book.push(new FormatElement(TextFormat.CODE));
            book.merge(new TextElement(content));
            book.merge();
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
            try
            {
               ProgramListingElement programListingElt = book.push(new ProgramListingElement(
                  context,
                  languageSyntax,
                  indent,
                  content,
                  context.getHighlightCode()));

               //
               programListingElt.process(loader);

               //
               book.merge();
            }
            catch (ParserConfigurationException e)
            {
               e.printStackTrace();
            }
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
         book.push(new ExampleElement(macroParameters.get("title")));
         block.traverse(this);
         book.merge();
      }
      else
      {
         throw new UnsupportedOperationException("Unsupported macro " + id);
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
            book.push(new LinkElement(type, ref));
            break;
         default:
            throw new UnsupportedOperationException();
      }
   }

   public void endLink(Link link, boolean isFreeStandingURI, Map<String, String> parameters)
   {
      switch (link.getType())
      {
         case URI:
            book.merge();
            break;
         default:
            throw new UnsupportedOperationException();
      }
   }

   //

   public void beginTable(Map<String, String> parameters)
   {
      String title = parameters.get("title");
      book.push(new TableElement(title));
   }

   public void beginTableRow(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doBeginTableRow(parameters);
   }

   public void beginTableCell(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doBeginTableCell(parameters);
   }

   public void beginTableHeadCell(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doBeginTableHeadCell(parameters);
   }

   public void endTable(Map<String, String> parameters)
   {
      book.merge();
   }

   public void endTableRow(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doEndTableRow(parameters);
   }

   public void endTableCell(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doEndTableCell(parameters);
   }

   public void endTableHeadCell(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doEndTableHeadCell(parameters);
   }

   //

   public void beginGroup(Map<String, String> parameters)
   {
      book.push(new GroupElement());
   }

   public void endGroup(Map<String, String> parameters)
   {
      book.merge();
   }

   //

   public void beginDefinitionList(Map<String, String> parameters)
   {
      String title = parameters.get("title");
      book.push(new VariableListElement(title));
   }

   public void beginDefinitionTerm()
   {
      book.push(new TermElement());
   }

   public void endDefinitionTerm()
   {
      book.merge();
   }

   public void beginDefinitionDescription()
   {
      book.push(new ListItemElement());
   }

   public void endDefinitionDescription()
   {
      book.merge();
   }

   public void endDefinitionList(Map<String, String> parameters)
   {
      book.merge();
   }

   //

   public void beginMacroMarker(String name, Map<String, String> macroParameters, String content, boolean isInline)
   {
      throw new UnsupportedOperationException();
   }

   public void endMacroMarker(String name, Map<String, String> macroParameters, String content, boolean isInline)
   {
      throw new UnsupportedOperationException();
   }

   public void beginQuotation(Map<String, String> parameters)
   {
      book.push(new BlockQuotationElement());
   }

   public void endQuotation(Map<String, String> parameters)
   {
      book.merge();
   }

   public void beginQuotationLine()
   {
   }

   public void endQuotationLine()
   {
   }

   public void onId(String name)
   {
      throw new UnsupportedOperationException();
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
      book.merge(new TextElement(protectedString));
   }

   public void onRawText(String rawContent, Syntax syntax)
   {
      throw new UnsupportedOperationException();
   }

   public void onImage(Image image, boolean isFreeStandingURI, Map<String, String> parameters)
   {
      book.merge(new ImageElement(image.getName(), parameters));
   }
}