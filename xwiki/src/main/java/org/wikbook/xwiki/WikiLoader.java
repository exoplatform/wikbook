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

import org.wikbook.core.ResourceType;
import org.wikbook.core.Utils;
import org.wikbook.core.WikbookException;
import org.wikbook.core.WikletContext;
import org.xwiki.component.embed.EmbeddableComponentManager;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.HeaderBlock;
import org.xwiki.rendering.block.MacroBlock;
import org.xwiki.rendering.block.SectionBlock;
import org.xwiki.rendering.block.VerbatimBlock;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.listener.HeaderLevel;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.Parser;
import org.xwiki.rendering.renderer.BlockRenderer;
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter;
import org.xwiki.rendering.renderer.printer.WikiPrinter;
import org.xwiki.rendering.syntax.Syntax;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class WikiLoader
{

   /** . */
   private final WikletContext context;

   /** . */
   private final EmbeddableComponentManager ecm;

   public WikiLoader(WikletContext context) throws WikbookException
   {
      EmbeddableComponentManager ecm = new EmbeddableComponentManager();
      ecm.initialize(Thread.currentThread().getContextClassLoader());

      //
      this.context = context;
      this.ecm = ecm;
   }

   public Block load(String id, String syntaxId) throws WikbookException
   {
      return load(id, syntaxId, 0);
   }

   public Block load(Reader reader, String syntaxId) throws WikbookException
   {
      return load(reader, syntaxId, 0);
   }

   private Block load(String id, String syntaxId, int baseLevel) throws WikbookException
   {
      try
      {
         Reader reader = _load(id);
         return load(reader, syntaxId, baseLevel);
      }
      catch (IOException e)
      {
         throw new WikbookException(e);
      }
   }

   private Block load(Reader reader, String syntaxId, int baseLevel) throws WikbookException
   {
      try
      {
         if (syntaxId == null)
         {
            syntaxId = Syntax.XWIKI_2_0.toIdString();
         }

         //
         XDOM xdom;
         if ("verbatim".equals(syntaxId))
         {
            xdom = new XDOM(new ArrayList<Block>());
            xdom.addChild(new VerbatimBlock(Utils.read(reader), false));
         }
         else
         {
            // Get parser
            Parser parser = ecm.lookup(Parser.class, syntaxId);


            // Parse
            xdom = parser.parse(reader);

            // Replace all headers
            if (baseLevel > 0)
            {
               for (HeaderBlock header : xdom.getChildrenByType(HeaderBlock.class, true))
               {
                  int l = baseLevel + header.getLevel().getAsInt();
                  HeaderBlock nheader = new HeaderBlock(
                     header.getChildren(),
                     HeaderLevel.parseInt(l),
                     header.getParameters(),
                     header.getId()
                  );
                  header.getParent().replaceChild(nheader, header);
               }
            }

            //
            List<Substitution> substitutions = visit(xdom, syntaxId);

            //
            if (substitutions.size() > 0)
            {
               for (Substitution substitution : substitutions)
               {
                  substitution.src.getParent().replaceChild(substitution.dst, substitution.src);
               }
               WikiPrinter printer = new DefaultWikiPrinter();
               BlockRenderer substitutionRenderer = ecm.lookup(BlockRenderer.class, Syntax.XWIKI_2_0.toIdString());
               substitutionRenderer.render(xdom, printer);
               Parser substitutionParser = ecm.lookup(Parser.class, Syntax.XWIKI_2_0.toIdString());
               xdom = substitutionParser.parse(new StringReader(printer.toString()));
            }
         }

         // Returns include block instead of XDOM
         return new IncludeBlock(syntaxId, xdom.getChildren());
      }
      catch (IOException e)
      {
         throw new WikbookException(e);
      }
      catch (ComponentLookupException e)
      {
         throw new WikbookException(e);
      }
      catch (ParseException e)
      {
         throw new WikbookException(e);
      }
   }

   /**
    * Load the document with the specified id.
    *
    * @param id the document id
    * @return a reader for the document
    * @throws IOException any io exception
    */
   private Reader _load(String id) throws IOException
   {
      URL main = context.resolveResource(ResourceType.WIKI, id);
      if (main == null)
      {
         throw new IOException("Could not load wiki document: " + id);
      }
      return Utils.read(main);
   }

   /**
    * Visit a block and returns the list of substitutions that must be performed in the visited block. The returned list
    * can be safely modified.
    *
    * @param block the block to visit
    * @return the substitution list
    */
   private List<Substitution> visit(Block block, String currentSyntaxId)
   {
      return visit(block, 0, currentSyntaxId);
   }

   private List<Substitution> visit(Block block, int level, String currentSyntaxId)
   {
      if (block instanceof MacroBlock)
      {
         MacroBlock macro = (MacroBlock)block;
         String id = macro.getId();
         if ("include".equals(id))
         {
            String includedId = macro.getParameter("document");
            String syntaxId = macro.getParameter("syntax");
            if (syntaxId == null)
            {
               syntaxId = currentSyntaxId;
            }
            LinkedList<Substitution> list = new LinkedList<Substitution>();
            list.add(new Substitution(block, load(includedId, syntaxId, level)));
            return list;
         }
         else if (id.startsWith("property."))
         {
            String name = id.substring("property.".length());
            if (name != null)
            {
               String value = context.getProperty(name);
               if (value != null)
               {
                  LinkedList<Substitution> list = new LinkedList<Substitution>();
                  list.add(new Substitution(block, new WordBlock(value)));
                  return list;
               }
            }
         }
      }
      else if (block instanceof SectionBlock)
      {
         level++;
      }

      //
      List<Substitution> substitutions = new LinkedList<Substitution>();
      for (Block child : block.getChildren())
      {
         List<Substitution> childSubstitutions = visit(child, level, currentSyntaxId);
         if (childSubstitutions.size() > 0)
         {
            substitutions.addAll(childSubstitutions);
         }
      }

      //
      return substitutions;
   }
}
