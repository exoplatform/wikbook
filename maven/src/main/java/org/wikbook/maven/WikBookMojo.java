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
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Developer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.w3c.dom.Document;
import org.wikbook.Person;
import org.wikbook.ResourceType;
import org.wikbook.WikletContext;
import org.wikbook.WikletConverter;
import org.wikbook.xml.OutputFormat;
import org.wikbook.xml.XML;

import javax.xml.transform.dom.DOMResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 * @goal transform
 * @phase compile
 * @requiresDependencyResolution
 */
public class WikBookMojo extends AbstractMojo implements WikletContext
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
    * INTERNAL : The representation of the maven execution.
    *
    * @parameter expression="${session}"
    * @required
    * @readonly
    */
   protected MavenSession session;

   /**
    * @parameter expression="${plugin.artifacts}"
    * @required
    * @readonly
    */
   protected List pluginArtifacts;

   public File getSourceDirectory()
   {
      return sourceDirectory;
   }

   public void setSourceDirectory(File sourceDirectory)
   {
      this.sourceDirectory = sourceDirectory;
   }

   public String getSourceFileName()
   {
      return sourceFileName;
   }

   public void setSourceFileName(String sourceFileName)
   {
      this.sourceFileName = sourceFileName;
   }

   public File getDestinationDirectory()
   {
      return destinationDirectory;
   }

   public void setDestinationDirectory(File destinationDirectory)
   {
      this.destinationDirectory = destinationDirectory;
   }

   public String getDestinationFileName()
   {
      return destinationFileName;
   }

   public void setDestinationFileName(String destinationFileName)
   {
      this.destinationFileName = destinationFileName;
   }

   public MavenSession getSession()
   {
      return session;
   }

   public void setSession(MavenSession session)
   {
      this.session = session;
   }

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
         converter = new WikletConverter(this);
      }
      catch (Exception e)
      {
         throw new MojoFailureException("", e);
      }

      //
      converter.setEmitDTD(true);
      converter.setSyntaxId(syntaxId);

      //
      File destination = new File(destinationDirectory, destinationFileName);

      //
      DOMResult dom = new DOMResult();

      //
      converter.convert(sourceFileName, dom);

      //
      Document document = (Document)dom.getNode();

      //
      FileWriter out = null;
      try
      {
         String expectedXML = XML.serialize(document, new OutputFormat(null, false,
            "-//OASIS//DTD DocBook XML V4.5//EN",
            "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd"
            ));
         out = new FileWriter(destination);
         out.write(expectedXML);
         out.close();
      }
      catch (Exception e)
      {
         MojoFailureException mfe = new MojoFailureException("Could not create destination file");
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

   public List<Person> findPersonsByRole(String roleName)
   {
      ArrayList<Person> ps = new ArrayList<Person>();
      MavenProject project = session.getCurrentProject();
      for (Iterator<?> i = project.getDevelopers().iterator();i.hasNext();)
      {
         Developer developer = (Developer)i.next();
         if (developer.getRoles().contains(roleName))
         {
            String firstName = null;
            String lastName = null;
            if (developer.getName() != null)
            {
               String[] names = developer.getName().trim().split("\\s+");
               if (names.length > 0)
               {
                  firstName = names[0];
               }
               if (names.length > 1)
               {
                  firstName = names[1];
               }
            }
            Person p = new Person(firstName, lastName, developer.getEmail(), developer.getOrganization(), new HashSet<String>(developer.getRoles()));
            ps.add(p);
         }
      }
      return ps;
   }

   public List<URL> resolveResources(ResourceType type, String id) throws IOException
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
         case JAVA:
            List<URL> urls = new ArrayList<URL>();
            for (Artifact artifact : (List<Artifact>)pluginArtifacts){
               urls.add(artifact.getFile().toURI().toURL());
            }

            //
            try
            {
               List<?> dirs = new ArrayList();
               dirs.addAll(session.getCurrentProject().getCompileSourceRoots());
               dirs.addAll(session.getCurrentProject().getTestCompileSourceRoots());
               dirs.addAll(session.getCurrentProject().getCompileClasspathElements());

               //
               for (String elt : (List<String>)dirs)
               {
                  File eltFile = new File(elt);
                  if (eltFile.exists() && eltFile.isDirectory())
                  {
                     urls.add(eltFile.toURI().toURL());
                  }
               }
            }
            catch (DependencyResolutionRequiredException e)
            {
               e.printStackTrace();
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
      return null;
   }

   public URL resolveResource(ResourceType type, String id) throws IOException
   {
      List<URL> found = resolveResources(type, id);
      return found.isEmpty() ? null : found.get(0);
   }

   public String getProperty(String propertyName)
   {
      Properties properties = session.getCurrentProject().getProperties();
      return properties.getProperty(propertyName);
   }
}
