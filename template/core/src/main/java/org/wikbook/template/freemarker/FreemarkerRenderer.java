package org.wikbook.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.TemplateElement;

import javax.annotation.processing.Filer;
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

  public void render(MetaModel model, String templateName, TemplateElement element, OutputStream os, Filer filer) throws IOException {

    Map<String, ? extends Object> data = builder.build(model, element);
    render(data, templateName, os, filer);

  }

  public void render(MetaModel model, String templateName, OutputStream os, Filer filer) throws IOException {

    Map<String, ? extends Object> data = builder.build(model);
    render(data, templateName, os, filer);

  }

  private void render(Map<String, ? extends Object> data, String templateName, OutputStream os, Filer filer) throws IOException {

    Configuration cfg = new Configuration();

    try {

      cfg.setTemplateLoader(new FilerTemplateLoader(filer));
      cfg.setObjectWrapper(new DefaultObjectWrapper());

      Template temp = cfg.getTemplate(templateName);
      OutputStreamWriter osw = new OutputStreamWriter(os);

      temp.process(data, osw);

      osw.flush();
      osw.close();

    } catch (TemplateException e) {
      throw new RuntimeException(e);
    }

  }

}