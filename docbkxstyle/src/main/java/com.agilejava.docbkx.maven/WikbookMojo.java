package com.agilejava.docbkx.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.selectors.FilenameSelector;

import java.io.File;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public abstract class WikbookMojo extends com.agilejava.docbkx.maven.AbstractHtmlMojo
{

   /**
    * @parameter default-value="${pom.basedir}/src/main/wikbook"
    */
   private File wikbookSource;

   @Override
   public void postProcess() throws MojoExecutionException
   {

      //
      Target target = new Target();
      target.setName("postProcess");
      Project project = new Project();
      project.addTarget(target);

      // Copy wikbook images
      if (wikbookSource != null && wikbookSource.exists() && wikbookSource.isDirectory())
      {
         File images = new File(wikbookSource, "images");
         if (images.exists() && images.isDirectory())
         {
            File output = getTargetDirectory();
            Copy copy = new Copy();
            copy.setProject(project);
            copy.setTodir(new File(output, "images"));
            FileSet fileSet = new FileSet();
            fileSet.setDir(images);
            FilenameSelector selector = new FilenameSelector();
            selector.setName("**");
            fileSet.add(selector);
            copy.addFileset(fileSet);
            target.addTask(copy);
         }
      }

      //
      File f;
      try
      {
         f = new File(WikbookMojo.class.getProtectionDomain().getCodeSource().getLocation().toURI());
      }
      catch (Exception e)
      {
         throw new MojoExecutionException("Could not get jar url", e);
      }

      // Copy the resources
      Expand unjar = new Expand();
      unjar.setProject(project);
      unjar.setSrc(f);
      unjar.setDest(getTargetDirectory());
      PatternSet patterns = new PatternSet();
      patterns.createInclude().setName("css/**");
      patterns.createInclude().setName("js/**");
      patterns.createInclude().setName("images/**");
      unjar.addPatternset(patterns);
      target.addTask(unjar);

      //
      executeTasks(target, getMavenProject());

      //
      super.postProcess();
   }
}
