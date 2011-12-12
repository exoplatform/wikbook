/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.wikbook.template.test;

import junit.framework.TestCase;
import org.wikbook.template.processing.metamodel.MetaModel;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public abstract class AbstractProcessorTestCase extends TestCase {
  
  protected MetaModel buildClass(String name) throws IOException, ClassNotFoundException {

    //
    new File("target/metaModel").delete();


    List<File> files = new ArrayList<File>();
    URL url = Thread.currentThread().getContextClassLoader().getResource(name + ".java");
    try {
      files.add(new File(url.toURI()));
    }
    catch (URISyntaxException e) {
      e.printStackTrace();
    }

    //
    List<String> options = new ArrayList<String>();
    options.add("-sourcepath");
    options.add("src/test/resources");

    //
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
    Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);

    compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits).call();

    try {
      fileManager.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    for(Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
      System.out.println(diagnostic.getMessage(Locale.ENGLISH));
    }

    return readMetaModel();

  }

  private MetaModel readMetaModel() throws ClassNotFoundException, IOException {

    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("target/metaModel"));
    return (MetaModel) ois.readObject();

  }

}
