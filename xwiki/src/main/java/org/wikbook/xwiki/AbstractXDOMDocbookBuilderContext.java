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

import org.wikbook.core.DocbookBuilder;
import org.wikbook.core.DocbookBuilderContext;
import org.wikbook.core.ResourceType;
import org.wikbook.core.Utils;
import org.xwiki.rendering.block.Block;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.LinkedList;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class AbstractXDOMDocbookBuilderContext extends DocbookBuilderContext
{

   /** . */
   final LinkedList<String> syntaxStack;

   protected AbstractXDOMDocbookBuilderContext()
   {
      this.syntaxStack = new LinkedList<String>();
   }

   /**
    * Load the document with the specified id.
    *
    * @param id the document id
    * @return a reader for the document
    * @throws java.io.IOException any io exception
    */
   Reader _load(String id) throws IOException
   {
      URL main = resolveResource(ResourceType.WIKI, id);
      if (main == null)
      {
         throw new IOException("Could not load wiki document: " + id);
      }
      return Utils.read(main);
   }

   @Override
   public void build(Reader reader, DocbookBuilder builder)
   {
      WikiLoader loader = new WikiLoader(this);
      Block block = loader.load(reader, syntaxStack.getLast());
      XDOMTransformer transformer = new XDOMTransformer(this, builder);
      block.traverse(transformer);
   }

   public void build(Reader reader, DocbookBuilder builder, String syntaxId)
   {
      WikiLoader loader = new WikiLoader(this);
      Block block = loader.load(reader, syntaxId);
      XDOMTransformer transformer = new XDOMTransformer(this, builder);
      syntaxStack.addLast(syntaxId);
      block.traverse(transformer);
      syntaxStack.removeLast();
   }
}
