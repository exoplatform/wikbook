package org.wikbook.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.wikbook.template.processing.metamodel.MetaModel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class FreemarkerRenderer {

  private FreemarkerModelBuilder builder;

  public FreemarkerRenderer() {
    builder = new FreemarkerModelBuilder();
  }

  public void render(MetaModel model, OutputStream os) throws IOException {

    Map<String, Object> data = builder.build(model);
    Configuration cfg = new Configuration();

    try {

      cfg.setDirectoryForTemplateLoading(new File("templates"));
      cfg.setObjectWrapper(new DefaultObjectWrapper());

      Template temp = cfg.getTemplate("templateTmp.txt");
      OutputStreamWriter osw = new OutputStreamWriter(os);
      
      temp.process(data, osw);

      osw.flush();
      osw.close();

    } catch (TemplateException e) {
      e.printStackTrace();
    }


  }

}
