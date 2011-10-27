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

package org.wikbook.html2pdf;

import com.lowagie.text.DocumentException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 * @goal transform
 * @phase package
 * @requiresDependencyResolution
 */
public class Html2PdfMojo extends AbstractMojo
{

   /**
    * The source directory.
    *
    * @parameter
    */
   private File sourceDirectory;

   /**
    * The source file name.
    *
    * @parameter
    */
   private String sourceFileName;

   /**
    * The destination directory.
    *
    * @parameter
    */
   private File destinationDirectory;

   /**
    * The destination file name.
    *
    * @parameter
    */
   private String destinationFileName;

   public void execute() throws MojoExecutionException, MojoFailureException
   {

      File document = new File(sourceDirectory, sourceFileName);
      if (!document.exists())
      {
         throw new MojoFailureException("Source " + document.getAbsolutePath() + " does not exist");
      }
      if (!document.isFile())
      {
         throw new MojoFailureException("Source " + document.getAbsolutePath() + " is not a valid file");
      }

      //
      if (!destinationDirectory.exists())
      {
         destinationDirectory.mkdirs();
      }

      //
      ITextRenderer renderer = new ITextRenderer();
      try
      {
         renderer.setDocument(document);
      }
      catch (IOException e)
      {
         throw new MojoFailureException("Source " + document.getAbsolutePath() + " is not readable", e);
      }

      //
      renderer.layout();

      //
      File pdf = new File(destinationDirectory, destinationFileName);
      OutputStream out = null;
      try
      {
         out = new BufferedOutputStream(new FileOutputStream(pdf));
         renderer.createPDF(out);
      }
      catch (FileNotFoundException e)
      {
         throw new MojoFailureException("Cannot write PDF", e);
      }
      catch (DocumentException e)
      {
         throw new MojoFailureException("Cannot write PDF", e);
      }
      finally
      {
         if (out != null)
         {
            try
            {
               out.close();
            }
            catch (IOException ignore)
            {
            }
         }
      }
   }
}
