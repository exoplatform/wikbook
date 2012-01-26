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
import org.wikbook.core.ResourceType;
import org.wikbook.core.ValidationMode;
import org.wikbook.core.xml.XML;
import org.wikbook.xwiki.AbstractXDOMDocbookBuilderContext;
import org.wikbook.xwiki.WikbookConverter;
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
import java.util.Collections;
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
    * The extra source directory.
    *
    * @parameter
    */
   private File extraSourceDirectory;

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
    * Any XML that should be inserted before the book body.
    *
    * @parameter default-value=""
    */
   private String beforeBookBodyXML;

   /**
    * Any XML that should be inserted after the book body.
    *
    * @parameter default-value=""
    */
   private String afterBookBodyXML;

   /**
    * The charset name when reading wiki files, the default value is the UTF-8 charset.
    *
    * @parameter default-value="UTF-8"
    */
   private String charset;

   /**
    * The book id, when specified it modifies the XML element <code>book</code> produced to add an <code<id</code> attribute
    * with the specified value.
    *
    * @parameter default-value="UTF-8"
    */
   private String bookId;

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

   /** The source roots. */
   private List<File> roots = null;

   private Iterable<File> getRoots()
   {
      if (roots == null)
      {
         List<File> roots = new ArrayList<File>();
         if (sourceDirectory != null)
         {
            roots.add(sourceDirectory);
         }
         if (extraSourceDirectory != null)
         {
            roots.add(extraSourceDirectory);
         }
         this.roots = roots;
      }
      return roots;
   }

   public void execute() throws MojoExecutionException, MojoFailureException
   {

      // Determine initial file
      File src = null;
      for (File root : getRoots())
      {
         File f = new File(root, sourceFileName);
         if (f.exists() && f.isFile())
         {
            src = f;
            break;
         }
      }

      //
      if (src == null)
      {
         throw new MojoFailureException("Source file " + sourceFileName + " is not valid");
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
      WikbookConverter converter;
      try
      {
         converter = new WikbookConverter(context);
      }
      catch (Exception e)
      {
         throw new MojoFailureException("", e);
      }

      //
      converter.setEmitDoctype(emitDoctype);
      converter.setSyntaxId(syntaxId);
      converter.setBookId(bookId);

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

   private final AbstractXDOMDocbookBuilderContext context = new AbstractXDOMDocbookBuilderContext()
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

      @Override
      public List<URL> resolveResources(ResourceType type, Iterable<String> path, String id) throws IOException
      {
         List<URL> urls = Collections.emptyList();
         if (id.length() > 0)
         {
            switch (type)
            {
               case WIKI:
                  // Explanations (or not to self since I was not able to remember what it did and wasted some time)
                  // The Iterable<String> path when it is not empty is the list of included documents, for instance
                  // if "book.wiki" includes "a/b.wiki" which includes "c/d.wiki" then the path should be the
                  // list {"a/b.wiki","c/d.wiki"}.
                  // The path is therefore a path of included documents used to find a resource, so the code below
                  // if "d.wiki" includes "e.wiki" in the same directory should follow:
                  // 1/ current = "/" (the root)
                  // 2/ current = "/a"
                  // 3/ current = "/a/c"
                  // 4/ resolved = "/a/c/e.wiki"
                  for (File root : getRoots())
                  {
                     File current = root;
                     for (String segment : path)
                     {
                        File relative = new File(current, segment);
                        current = relative.getParentFile();
                     }
                     File resolved = new File(current, id);
                     if (resolved.exists() && resolved.isFile())
                     {
                        if (urls.isEmpty())
                        {
                           urls = new ArrayList<URL>();
                        }
                        urls.add(resolved.toURI().toURL());
                     }
                  }
                  break;
               case XML:
               case JAVA:
               case DEFAULT:

                  //
                  List<String> dirs = new ArrayList<String>();
                  dirs.addAll(compileSourceRoots);
                  dirs.addAll(compileClasspathElements);
                  dirs.addAll(testCompileSourceRoots);
                  dirs.addAll(testClasspathElements);

                  //
                  LinkedHashSet<URL> urlSet = new LinkedHashSet<URL>();

                  //
                  for (String elt : dirs)
                  {
                     File eltFile = new File(elt);
                     if (eltFile.exists())
                     {
                        urlSet.add(eltFile.toURI().toURL());
                     }
                  }

                  //
                  for (Artifact artifact : (Set<Artifact>)session.getCurrentProject().getDependencyArtifacts())
                  {
                     urlSet.add(artifact.getFile().toURI().toURL());
                  }

                  // Remove trailing '/' if any
                  if (id.startsWith("/"))
                  {
                     id = id.substring(1);
                  }

                  //
                  ClassLoader cl = new URLClassLoader(urlSet.toArray(new URL[urlSet.size()]), ClassLoader.getSystemClassLoader());
                  urls =Collections.list(cl.getResources(id));
            }
         }

         //
         return urls;
      }

      public String getProperty(String propertyName)
      {
         Properties properties = session.getCurrentProject().getProperties();
         return properties.getProperty(propertyName);
      }

      @Override
      protected String getCharsetName()
      {
         return charset;
      }
   };
}
