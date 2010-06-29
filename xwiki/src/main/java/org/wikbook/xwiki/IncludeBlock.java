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

import org.xwiki.rendering.block.AbstractFatherBlock;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.listener.Listener;

import java.util.List;

/**
 * This synthetic block is used to change the syntax on the transformer that can change due to the inclusions.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class IncludeBlock extends AbstractFatherBlock
{

   /** . */
   private final String syntaxId;

   public IncludeBlock(String syntaxId, List<Block> childrenBlocks)
   {
      super(childrenBlocks);

      //
      this.syntaxId = syntaxId;
   }

   public void before(Listener listener)
   {
      if (listener instanceof XDOMTransformer)
      {
         ((XDOMTransformer)listener).syntaxStack.addLast(syntaxId);
      }
   }

   public void after(Listener listener)
   {
      if (listener instanceof XDOMTransformer)
      {
         ((XDOMTransformer)listener).syntaxStack.removeLast();
      }
   }
}
