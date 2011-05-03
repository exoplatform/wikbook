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

import org.wikbook.core.model.ElementContainer;
import org.wikbook.core.model.DocbookElement;
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
import org.wikbook.core.model.content.inline.InlineImageElement;
import org.wikbook.core.model.content.inline.LinkElement;
import org.wikbook.core.model.content.inline.TextElement;
import org.wikbook.core.model.structural.BookElement;
import org.wikbook.core.model.structural.ComponentElement;
import org.wikbook.core.model.structural.PrefaceElement;
import org.wikbook.core.render.docbook.content.block.AdmonitionTransformer;
import org.wikbook.core.render.docbook.content.block.BlockQuotationTransformer;
import org.wikbook.core.render.docbook.content.block.CalloutTransformer;
import org.wikbook.core.render.docbook.content.block.DOMTransformer;
import org.wikbook.core.render.docbook.content.block.ExampleTransformer;
import org.wikbook.core.render.docbook.content.block.GroupTransformer;
import org.wikbook.core.render.docbook.content.block.ImageTransformer;
import org.wikbook.core.render.docbook.content.block.ListItemTransformer;
import org.wikbook.core.render.docbook.content.block.ListTransformer;
import org.wikbook.core.render.docbook.content.block.ParagraphTransformer;
import org.wikbook.core.render.docbook.content.block.ProgramListingTransformer;
import org.wikbook.core.render.docbook.content.block.ScreenTransformer;
import org.wikbook.core.render.docbook.content.block.list.TermTransformer;
import org.wikbook.core.render.docbook.content.block.list.VariableListTransformer;
import org.wikbook.core.render.docbook.content.block.table.TableCellTransformer;
import org.wikbook.core.render.docbook.content.block.table.TableRowTransformer;
import org.wikbook.core.render.docbook.content.block.table.TableTransformer;
import org.wikbook.core.render.docbook.content.inline.AnchorTransformer;
import org.wikbook.core.render.docbook.content.inline.FormatTransformer;
import org.wikbook.core.render.docbook.content.inline.InlineImageTransformer;
import org.wikbook.core.render.docbook.content.inline.LinkTransformer;
import org.wikbook.core.render.docbook.content.inline.TextTransformer;
import org.wikbook.core.render.docbook.structural.BookTransformer;
import org.wikbook.core.render.docbook.structural.ComponentTransformer;
import org.wikbook.core.render.docbook.structural.PrefaceTransformer;
import org.wikbook.core.xml.ElementEmitter;
import org.wikbook.core.xml.XMLEmitter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class ElementTransformer<E extends DocbookElement>
{

   /** . */
   private static final Map<Class<? extends DocbookElement>, ElementTransformer<?>> writers = new HashMap<Class<? extends DocbookElement>, ElementTransformer<?>>();

   static
   {
      writers.put(BookElement.class, new BookTransformer());
      writers.put(ComponentElement.class, new ComponentTransformer());
      writers.put(PrefaceElement.class, new PrefaceTransformer());

      //
      writers.put(AnchorElement.class, new AnchorTransformer());
      writers.put(FormatElement.class, new FormatTransformer());
      writers.put(LinkElement.class, new LinkTransformer());
      writers.put(TextElement.class, new TextTransformer());
      writers.put(InlineImageElement.class, new InlineImageTransformer());

      //
      writers.put(AdmonitionElement.class, new AdmonitionTransformer());
      writers.put(BlockQuotationElement.class, new BlockQuotationTransformer());
      writers.put(CalloutElement.class, new CalloutTransformer());
      writers.put(DOMElement.class, new DOMTransformer());
      writers.put(ExampleElement.class, new ExampleTransformer());
      writers.put(GroupElement.class, new GroupTransformer());
      writers.put(ImageElement.class, new ImageTransformer());
      writers.put(ListItemElement.class, new ListItemTransformer());
      writers.put(ListElement.class, new ListTransformer());
      writers.put(ParagraphElement.class, new ParagraphTransformer());
      writers.put(ProgramListingElement.class, new ProgramListingTransformer());
      writers.put(ScreenElement.class, new ScreenTransformer());

      //
      writers.put(TermElement.class, new TermTransformer());
      writers.put(VariableListElement.class, new VariableListTransformer());

      //
      writers.put(TableElement.class, new TableTransformer());
      writers.put(TableRowElement.class, new TableRowTransformer());
      writers.put(TableCellElement.class, new TableCellTransformer());
   }

   public static <E extends DocbookElement> ElementTransformer<E> getWriter(Class<E> elementType)
   {
      return (ElementTransformer<E>)writers.get(elementType);
   }

   public static <E extends DocbookElement> ElementTransformer<E> getWriter(E element)
   {
      ElementTransformer<E> writer = (ElementTransformer<E>)writers.get(element.getType());
      if (writer == null)
      {
         throw new IllegalArgumentException("No writer found for element " + element + " of type " + element.getType().getName());
      }
      return writer;
   }

   /**
    * <p>Write an element container to the specified emitter.</p>
    *
    * <p>When the argument {@code blockContainer} value is false, the writing process iterates over the container elements
    * and render them sequentially.</p>

    * <p>When the argument {@code blockContainer} value is true, the container is considered as a block container,
    * the writing process takes care of wrapping the elements that are not block elements by a paragraph element (which
    * is a block itself).</p>
    *
    * @param container the container
    * @param blockContainer denotes a container acting as a block container when set to true
    * @param emitter the emmitter
    * @param <E> the container element parameter type
    */
   protected <E extends DocbookElement> void write(
      ElementContainer<E> container,
      boolean blockContainer,
      XMLEmitter emitter)
   {
      if (blockContainer)
      {
         ElementEmitter paraXML = null;
         for (DocbookElement e : container)
         {
            ElementTransformer<DocbookElement> writer = getWriter(e);
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
      else
      {
         for (E elt : container)
         {
            ElementTransformer<E> writer = getWriter(elt);
            writer.write(elt, emitter);
         }
      }
   }

   /**
    * Writes an element to the emitter.
    *
    * @param element the element to write
    * @param emitter the emitter
    */
   public abstract void write(E element, XMLEmitter emitter);

}
