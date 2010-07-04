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

package org.wikbook.maven;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.wikbook.core.BookBuilderContext;
import org.wikbook.core.ResourceType;
import org.wikbook.core.ValidationMode;
import org.wikbook.core.xml.XML;
import org.wikbook.xwiki.WikletConverter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 * @goal transform
 * @phase compile
 * @requiresDependencyResolution
 */
public class WikBookMojo extends AbstractMojo
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
    * The syntax id.
    *
    * @parameter
    */
   private String syntaxId;

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

   /**
    * Turn on/off code highlighting
    *
    * @parameter default-value="true"
    */
   private boolean highlightCode;

   /**
    * Turn on/off code doctype generation of the DocBook document.
    *
    * @parameter default-value="true"
    */
   private boolean emitDoctype;

   /**
    * The wikbook validation mode, can either be <code>lax</code> or <code>strict</code>.
    *
    * @parameter default-value="lax"
    */
   private String validationMode;

   /**
    * .
    *
    * @parameter default-value=""
    */
   private String beforeBookBodyXML;

   /**
    * .
    *
    * @parameter default-value=""
    */
   private String afterBookBodyXML;

   /**
    * INTERNAL : The representation of the maven execution.
    *
    * @parameter expression="${session}"
    * @required
    * @readonly
    */
   private MavenSession session;

   /**
    * @parameter expression="${plugin.artifacts}"
    * @required
    * @readonly
    */
   private List pluginArtifacts;

   /**
    * The source directories containing the sources to be compiled.
    *
    * @parameter default-value="${project.compileSourceRoots}"
    * @required
    * @readonly
    */
   private List<String> compileSourceRoots;

   /**
    * Project classpath.
    *
    * @parameter default-value="${project.compileClasspathElements}"
    * @required
    * @readonly
    */
   private List<String> compileClasspathElements;

   /**
    * The source directories containing the sources to be compiled.
    *
    * @parameter default-value="${project.testCompileSourceRoots}"
    * @required
    * @readonly
    */
   private List<String> testCompileSourceRoots;

   /**
    * Project classpath.
    *
    * @parameter default-value="${project.testClasspathElements}"
    * @required
    * @readonly
    */
   private List<String> testClasspathElements;

   public void execute() throws MojoExecutionException, MojoFailureException
   {

      File src = new File(sourceDirectory, sourceFileName);
      if (!src.exists())
      {
         throw new MojoFailureException("Source file " + src.getAbsolutePath() + " does not exist");
      }
      if (!src.isFile())
      {
         throw new MojoFailureException("Source file " + src.getAbsolutePath() + " is not a file");
      }

      //
      if (destinationDirectory.exists())
      {
         if (!destinationDirectory.isDirectory())
         {
            throw new MojoFailureException("Destination directory " + destinationDirectory.getAbsolutePath() + " is not a directory");
         }
      }
      else
      {
         if (!destinationDirectory.mkdirs())
         {
            throw new MojoFailureException("Could not create destination directory " + destinationDirectory.getAbsolutePath());
         }
      }

      //
      WikletConverter converter;
      try
      {
         converter = new WikletConverter(context);
      }
      catch (Exception e)
      {
         throw new MojoFailureException("", e);
      }

      //
      converter.setEmitDoctype(emitDoctype);
      converter.setSyntaxId(syntaxId);

      //
      File destination = new File(destinationDirectory, destinationFileName);

      //
      FileWriter out = null;
      try
      {
         if (hasTrimmedContent(beforeBookBodyXML))
         {
            DocumentFragment elt = load(beforeBookBodyXML);
            converter.setBeforeBookBodyXML(elt);
         }

         //
         if (hasTrimmedContent(afterBookBodyXML))
         {
            DocumentFragment elt = load(afterBookBodyXML);
            converter.setAfterBookBodyXML(elt);
         }

         //
         out = new FileWriter(destination);
         StreamResult result = new StreamResult(out);
         converter.convert(sourceFileName, result);
      }
      catch (Exception e)
      {
         MojoFailureException mfe = new MojoFailureException("Could not create destination file " + e.getMessage());
         mfe.initCause(e);
         throw mfe;
      }
      finally
      {
         if (out != null)
         {
            try
            {
               out.close();
            }
            catch (IOException e)
            {
               e.printStackTrace();
            }
         }
      }
   }

   private static boolean hasTrimmedContent(String xml)
   {
      return xml != null && xml.trim().length() > 0;
   }

   private static DocumentFragment load(String xml) throws ParserConfigurationException, IOException, SAXException
   {
      xml = "<root>" + xml + "</root>";
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      dbf.setXIncludeAware(false);
      dbf.setValidating(false);
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new InputSource(new StringReader(xml)));
      DocumentFragment fragment = doc.createDocumentFragment();
      XML.copyStandaloneNodes(doc.getDocumentElement(), fragment);
      return fragment;
   }

   private final BookBuilderContext context = new BookBuilderContext()
   {
      public void log(String msg)
      {
         getLog().info(msg);
      }

      public boolean getHighlightCode()
      {
         return highlightCode;
      }

      public ValidationMode getValidationMode()
      {
         if ("strict".equalsIgnoreCase(validationMode))
         {
            return ValidationMode.STRICT;
         }
         else
         {
            return ValidationMode.LAX;
         }
      }

      public List<URL> resolveResources(ResourceType type, String id) throws IOException
      {
         if (id.length() > 0)
         {
            switch (type)
            {
               case WIKI:
                  File f = new File(sourceDirectory, id);
                  if (f.exists() && f.isFile())
                  {
                     return Arrays.asList(f.toURI().toURL());
                  }
                  break;
               case XML:
                  if (id.startsWith("/"))
                  {
                     id = id.substring(1);
                  }
               case JAVA:
                  LinkedHashSet<URL> urls = new LinkedHashSet<URL>();

                  //
                  List<String> dirs = new ArrayList<String>();
                  dirs.addAll(compileSourceRoots);
                  dirs.addAll(compileClasspathElements);
                  dirs.addAll(testCompileSourceRoots);
                  dirs.addAll(testClasspathElements);

                  //
                  for (String elt : dirs)
                  {
                     File eltFile = new File(elt);
                     if (eltFile.exists())
                     {
                        urls.add(eltFile.toURI().toURL());
                     }
                  }

                  //
                  for (Artifact artifact : (Set<Artifact>)session.getCurrentProject().getDependencyArtifacts())
                  {
                     urls.add(artifact.getFile().toURI().toURL());
                  }

                  //
                  ClassLoader cl = new URLClassLoader(urls.toArray(new URL[urls.size()]), ClassLoader.getSystemClassLoader());
                  Enumeration<URL> found = cl.getResources(id);
                  ArrayList<URL> bilto = new ArrayList<URL>();
                  while (found.hasMoreElements())
                  {
                     bilto.add(found.nextElement());
                  }

                  //
                  return bilto;
            }
         }
         return Collections.emptyList();
      }

      public String getProperty(String propertyName)
      {
         Properties properties = session.getCurrentProject().getProperties();
         return properties.getProperty(propertyName);
      }
   };
}
