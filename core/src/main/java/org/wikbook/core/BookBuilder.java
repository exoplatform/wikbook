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

import org.w3c.dom.Element;
import org.wikbook.core.model.DocbookContext;
import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.Loader;
import org.wikbook.core.model.content.block.AdmonitionElement;
import org.wikbook.core.model.content.block.AdmonitionKind;
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
import org.wikbook.core.model.content.inline.LinkType;
import org.wikbook.core.model.content.inline.TextElement;
import org.wikbook.core.model.content.inline.TextFormat;
import org.wikbook.core.model.structural.BookElement;
import org.wikbook.core.model.structural.ComponentElement;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class BookBuilder
{

   /** . */
   private final BookBuilderContext context;

   /** . */
   private DocbookContext bookContext;

   /** . */
   private BookElement book;

   /** . */
   private final Loader loader = new Loader()
   {
      public void load(Reader reader)
      {
         BookBuilder.this.load(reader);
      }
   };

   public BookBuilder(BookBuilderContext context) throws IOException, ClassNotFoundException
   {
      this.context = context;
   }

   public boolean isInline()
   {
      // Determine inline according to the docbook context and not according to what the parser tell us
      DocbookElement elt = book.peek();
      return elt instanceof InlineElement;
   }

   public BookElement getBook()
   {
      return book;
   }

   public void beginBook()
   {
      book = new BookElement();
      bookContext = new DocbookContext(book);
   }

   public void endBook()
   {
      bookContext = null;
      book.close();
   }

   public void beginParagraph()
   {
      book.push(new ParagraphElement());
   }

   public void endParagraph()
   {
      book.merge();
   }

   public void beginSection()
   {
      book.push(new ComponentElement());
   }

   public void endSection()
   {
      book.merge();
   }

   public void beginHeader()
   {
      ((ComponentElement)book.peek()).beginTitle();
   }

   public void endHeader()
   {
      ((ComponentElement)book.peek()).endTitle();
   }

   //

   public void onText(String text)
   {
      book.merge(new TextElement(text));
   }

   public void beginFormat(TextFormat format)
   {
      book.push(new FormatElement(format));
   }

   public void endFormat(TextFormat format)
   {
      book.merge();
   }

   public void beginList(ListKind listKind, String style)
   {
      book.push(new ListElement(listKind, style));
   }

   public void endList(ListKind listKind, String style)
   {
      book.merge();
   }

   public void beginListItem()
   {
      book.push(new ListItemElement());
   }

   public void endListItem()
   {
      book.merge();
   }

   public void beginAdmonition(AdmonitionKind admonition)
   {
      if (isInline())
      {
         context.onValidationError("No admonition " + admonition + " allowed inside");
      }
      else
      {
         AdmonitionElement admonitionElt = new AdmonitionElement(admonition);
         book.push(admonitionElt);
      }
   }

   public void endAdmonition(AdmonitionKind admonition)
   {
      if (isInline())
      {
         context.onValidationError("No admonition " + admonition + " allowed inside");
      }
      else
      {
         book.merge();
      }
   }

   public void beginExample(String title)
   {
      book.push(new ExampleElement(title));
   }

   public void endExample(String title)
   {
      book.merge();
   }

   public void beginLink(LinkType linkType, String ref)
   {
      book.push(new LinkElement(linkType, ref));
   }

   public void endLink(LinkType linkType, String ref)
   {
      book.merge();
   }

   public void beginScreen()
   {
      ScreenElement screenElt = new ScreenElement();
      book.push(screenElt);
   }

   public void endScreen()
   {
      book.merge();
   }

   public void beginTable(String title)
   {
      book.push(new TableElement(title));
   }

   public void endTable(String title)
   {
      book.merge();
   }

   public void beginTableRow(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doBeginTableRow(parameters);
   }

   public void endTableRow(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doEndTableRow(parameters);
   }

   public void beginTableCell(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doBeginTableCell(parameters);
   }

   public void endTableCell(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doEndTableCell(parameters);
   }

   public void beginTableHeadCell(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doBeginTableHeadCell(parameters);
   }

   public void endTableHeadCell(Map<String, String> parameters)
   {
      ((TableElement)book.peek()).doEndTableHeadCell(parameters);
   }

   public void beginDefinitionList(String title)
   {
      book.push(new VariableListElement(title));
   }

   public void endDefinitionList(String title)
   {
      book.merge();
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

   public void beginQuotation()
   {
      book.push(new BlockQuotationElement());
   }

   public void endQuotation()
   {
      book.merge();
   }

   public void beginGroup()
   {
      book.push(new GroupElement());
   }

   public void endGroup()
   {
      book.merge();
   }

   public void onVerbatim(String text)
   {
      book.merge(new TextElement(text));
   }

   public void onImage(String imageName, Map<String, String> parameters)
   {
      book.merge(new ImageElement(imageName, parameters));
   }

   public void onCode(LanguageSyntax language, Integer indent, String content)
   {
      ProgramListingElement programListingElt = book.push(new ProgramListingElement(
         context,
         language,
         indent,
         content,
         context.getHighlightCode()));

      //
      programListingElt.process(loader);

      //
      book.merge();
   }

   public void onAnchor(String anchor)
   {
      book.merge(new AnchorElement(anchor));
   }

   public void onDocbook(Element docbookElt)
   {
      book.merge(new DOMElement(docbookElt));
   }

   protected abstract void load(Reader reader);


}
