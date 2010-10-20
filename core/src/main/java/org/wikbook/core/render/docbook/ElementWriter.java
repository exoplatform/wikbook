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

package org.wikbook.core.render.docbook;

import org.wikbook.core.model.AnyElementContainer;
import org.wikbook.core.model.DocbookElement;
import org.wikbook.core.model.ElementContainer;
import org.wikbook.core.model.content.ContentElement;
import org.wikbook.core.model.content.ContentElementContainer;
import org.wikbook.core.model.content.block.AdmonitionElement;
import org.wikbook.core.model.content.block.BlockElement;
import org.wikbook.core.model.content.block.BlockQuotationElement;
import org.wikbook.core.model.content.block.CalloutElement;
import org.wikbook.core.model.content.block.DOMElement;
import org.wikbook.core.model.content.block.ExampleElement;
import org.wikbook.core.model.content.block.GroupElement;
import org.wikbook.core.model.content.block.ImageElement;
import org.wikbook.core.model.content.block.ListElement;
import org.wikbook.core.model.content.block.ListItemElement;
import org.wikbook.core.model.content.block.ParagraphElement;
import org.wikbook.core.model.content.block.ProgramListingElement;
import org.wikbook.core.model.content.block.ScreenElement;
import org.wikbook.core.model.content.block.list.TermElement;
import org.wikbook.core.model.content.block.list.VariableListElement;
import org.wikbook.core.model.content.block.table.TableCellElement;
import org.wikbook.core.model.content.block.table.TableElement;
import org.wikbook.core.model.content.block.table.TableRowElement;
import org.wikbook.core.model.content.inline.AnchorElement;
import org.wikbook.core.model.content.inline.FormatElement;
import org.wikbook.core.model.content.inline.LinkElement;
import org.wikbook.core.model.content.inline.TextElement;
import org.wikbook.core.model.structural.BookElement;
import org.wikbook.core.model.structural.ComponentElement;
import org.wikbook.core.render.docbook.content.block.AdmonitionWriter;
import org.wikbook.core.render.docbook.content.block.BlockQuotationWriter;
import org.wikbook.core.render.docbook.content.block.CalloutWriter;
import org.wikbook.core.render.docbook.content.block.DOMWriter;
import org.wikbook.core.render.docbook.content.block.ExampleWriter;
import org.wikbook.core.render.docbook.content.block.GroupWriter;
import org.wikbook.core.render.docbook.content.block.ImageWriter;
import org.wikbook.core.render.docbook.content.block.ListItemWriter;
import org.wikbook.core.render.docbook.content.block.ListWriter;
import org.wikbook.core.render.docbook.content.block.ParagraphWriter;
import org.wikbook.core.render.docbook.content.block.ProgramListingWriter;
import org.wikbook.core.render.docbook.content.block.ScreenWriter;
import org.wikbook.core.render.docbook.content.block.list.TermWriter;
import org.wikbook.core.render.docbook.content.block.list.VariableListWriter;
import org.wikbook.core.render.docbook.content.block.table.TableCellWriter;
import org.wikbook.core.render.docbook.content.block.table.TableRowWriter;
import org.wikbook.core.render.docbook.content.block.table.TableWriter;
import org.wikbook.core.render.docbook.content.inline.AnchorWriter;
import org.wikbook.core.render.docbook.content.inline.FormatWriter;
import org.wikbook.core.render.docbook.content.inline.LinkWriter;
import org.wikbook.core.render.docbook.content.inline.TextWriter;
import org.wikbook.core.render.docbook.structural.BookWriter;
import org.wikbook.core.render.docbook.structural.ComponentWriter;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class ElementWriter<E extends DocbookElement>
{

   /** . */
   private static final Map<Class<? extends DocbookElement>, ElementWriter<?>> writers = new HashMap<Class<? extends DocbookElement>, ElementWriter<?>>();

   static
   {
      writers.put(BookElement.class, new BookWriter());
      writers.put(ComponentElement.class, new ComponentWriter());

      //
      writers.put(AnchorElement.class, new AnchorWriter());
      writers.put(FormatElement.class, new FormatWriter());
      writers.put(LinkElement.class, new LinkWriter());
      writers.put(TextElement.class, new TextWriter());

      //
      writers.put(AdmonitionElement.class, new AdmonitionWriter());
      writers.put(BlockQuotationElement.class, new BlockQuotationWriter());
      writers.put(CalloutElement.class, new CalloutWriter());
      writers.put(DOMElement.class, new DOMWriter());
      writers.put(ExampleElement.class, new ExampleWriter());
      writers.put(GroupElement.class, new GroupWriter());
      writers.put(ImageElement.class, new ImageWriter());
      writers.put(ListItemElement.class, new ListItemWriter());
      writers.put(ListElement.class, new ListWriter());
      writers.put(ParagraphElement.class, new ParagraphWriter());
      writers.put(ProgramListingElement.class, new ProgramListingWriter());
      writers.put(ScreenElement.class, new ScreenWriter());

      //
      writers.put(TermElement.class, new TermWriter());
      writers.put(VariableListElement.class, new VariableListWriter());

      //
      writers.put(TableElement.class, new TableWriter());
      writers.put(TableRowElement.class, new TableRowWriter());
      writers.put(TableCellElement.class, new TableCellWriter());
   }

   public static <E extends DocbookElement> ElementWriter<E> getWriter(Class<E> elementType)
   {
      return (ElementWriter<E>)writers.get(elementType);
   }

   public static <E extends DocbookElement> ElementWriter<E> getWriter(E element)
   {
      ElementWriter<E> writer = (ElementWriter<E>)writers.get(element.getType());
      if (writer == null)
      {
         throw new IllegalArgumentException("No writer found for element " + element + " of type " + element.getType().getName());
      }
      return writer;
   }

   public <E extends DocbookElement> void write(ElementContainer<E> elements, XMLEmitter emitter)
   {
      if (elements instanceof AnyElementContainer)
      {
         write((AnyElementContainer<E>)elements, emitter);
      }
      else
      {
         write((ContentElementContainer)elements, emitter);
      }
   }

   public <E extends DocbookElement> void write(AnyElementContainer<E> elements, XMLEmitter emitter)
   {
      for (E elt : elements)
      {
         ElementWriter<E> writer = getWriter(elt);
         writer.write(elt, emitter);
      }
   }

   public void write(ContentElementContainer elements, XMLEmitter emitter)
   {

      //
      ElementEmitter paraXML = null;
      for (ContentElement e : elements)
      {
         ElementWriter<ContentElement> writer = getWriter(e);
         if (e instanceof BlockElement)
         {
            if (paraXML != null)
            {
               paraXML = null;
            }
            writer.write(e, emitter);
         }
         else
         {
            if (paraXML == null)
            {
               paraXML = emitter.element("para");
            }
            writer.write(e, paraXML);
         }
      }
   }

   public abstract void write(E element, XMLEmitter emitter);

}
