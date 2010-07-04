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

package org.wikbook.core.model;

import org.w3c.dom.Element;
import org.wikbook.core.ResourceType;
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
import org.wikbook.core.model.structural.ComponentElement;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class DocbookBuilder
{

   /** . */
   private final DocbookBuilderContext context;

   /** . */
   private DocbookElement root;

   /** . */
   private final DocbookElementContext rootContext = new DocbookElementContext()
   {
      @Override
      public URL resolveResource(ResourceType type, String id) throws IOException
      {
         return context.resolveResource(type, id);
      }
      @Override
      public void build(Reader reader, DocbookElement element)
      {
         new DocbookBuilder(context, element).build(reader, null);
      }
   };

   public DocbookBuilder(DocbookBuilderContext context, DocbookElement root)
   {
      if (context == null)
      {
         throw new NullPointerException("No null context accepted");
      }

      //
      this.context = context;
      this.root = root;
   }

   public void build(Reader reader, String syntaxId)
   {
      // Wire
      root.context = rootContext;

      // perform build
      context.build(reader, syntaxId, this);

      // Clear context
      root.context = null;
   }

   public boolean isInline()
   {
      // Determine inline according to the docbook context and not according to what the parser tell us
      DocbookElement elt = root.peek();
      return elt instanceof InlineElement;
   }

   public DocbookElement getRoot()
   {
      return root;
   }

   public void beginParagraph()
   {
      root.push(new ParagraphElement());
   }

   public void endParagraph()
   {
      root.merge();
   }

   public void beginSection()
   {
      root.push(new ComponentElement());
   }

   public void endSection()
   {
      root.merge();
   }

   public void beginHeader()
   {
      ((ComponentElement)root.peek()).beginTitle();
   }

   public void endHeader()
   {
      ((ComponentElement)root.peek()).endTitle();
   }

   //

   public void onText(String text)
   {
      root.merge(new TextElement(text));
   }

   public void beginFormat(TextFormat format)
   {
      root.push(new FormatElement(format));
   }

   public void endFormat(TextFormat format)
   {
      root.merge();
   }

   public void beginList(ListKind listKind, String style)
   {
      root.push(new ListElement(listKind, style));
   }

   public void endList(ListKind listKind, String style)
   {
      root.merge();
   }

   public void beginListItem()
   {
      root.push(new ListItemElement());
   }

   public void endListItem()
   {
      root.merge();
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
         root.push(admonitionElt);
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
         root.merge();
      }
   }

   public void beginExample(String title)
   {
      root.push(new ExampleElement(title));
   }

   public void endExample(String title)
   {
      root.merge();
   }

   public void beginLink(LinkType linkType, String ref)
   {
      root.push(new LinkElement(linkType, ref));
   }

   public void endLink(LinkType linkType, String ref)
   {
      root.merge();
   }

   public void beginScreen()
   {
      ScreenElement screenElt = new ScreenElement();
      root.push(screenElt);
   }

   public void endScreen()
   {
      root.merge();
   }

   public void beginTable(String title)
   {
      root.push(new TableElement(title));
   }

   public void endTable(String title)
   {
      root.merge();
   }

   public void beginTableRow(Map<String, String> parameters)
   {
      ((TableElement)root.peek()).doBeginTableRow(parameters);
   }

   public void endTableRow(Map<String, String> parameters)
   {
      ((TableElement)root.peek()).doEndTableRow(parameters);
   }

   public void beginTableCell(Map<String, String> parameters)
   {
      ((TableElement)root.peek()).doBeginTableCell(parameters);
   }

   public void endTableCell(Map<String, String> parameters)
   {
      ((TableElement)root.peek()).doEndTableCell(parameters);
   }

   public void beginTableHeadCell(Map<String, String> parameters)
   {
      ((TableElement)root.peek()).doBeginTableHeadCell(parameters);
   }

   public void endTableHeadCell(Map<String, String> parameters)
   {
      ((TableElement)root.peek()).doEndTableHeadCell(parameters);
   }

   public void beginDefinitionList(String title)
   {
      root.push(new VariableListElement(title));
   }

   public void endDefinitionList(String title)
   {
      root.merge();
   }

   public void beginDefinitionTerm()
   {
      root.push(new TermElement());
   }

   public void endDefinitionTerm()
   {
      root.merge();
   }

   public void beginDefinitionDescription()
   {
      root.push(new ListItemElement());
   }

   public void endDefinitionDescription()
   {
      root.merge();
   }

   public void beginQuotation()
   {
      root.push(new BlockQuotationElement());
   }

   public void endQuotation()
   {
      root.merge();
   }

   public void beginGroup()
   {
      root.push(new GroupElement());
   }

   public void endGroup()
   {
      root.merge();
   }

   public void onVerbatim(String text)
   {
      root.merge(new TextElement(text));
   }

   public void onImage(String imageName, Map<String, String> parameters)
   {
      root.merge(new ImageElement(imageName, parameters));
   }

   public void onCode(LanguageSyntax language, Integer indent, String content)
   {
      ProgramListingElement programListingElt = root.push(new ProgramListingElement(
         context,
         language,
         indent,
         content,
         context.getHighlightCode()));

      //
      programListingElt.process();

      //
      root.merge();
   }

   public void onAnchor(String anchor)
   {
      root.merge(new AnchorElement(anchor));
   }

   public void onDocbook(Element docbookElt)
   {
      root.merge(new DOMElement(docbookElt));
   }
}
