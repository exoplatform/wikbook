package org.wikbook.template.processing;

import org.wikbook.template.freemarker.FreemarkerRenderer;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.ModelContext;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public abstract class AbstractTemplateProcessor extends AbstractProcessor {

  private Class[] annotations;
  private Filer filer;
  private Elements utils;
  private String ext;

  @Override
  public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

    //
    annotations = annotations();
    if (annotations == null) {
      throw new NullPointerException();
    }

    //
    ext = ext();
    if (ext == null || ext.length() == 0) {
      throw new NullPointerException();
    }
    else {
      ext = "." + ext;
    }

    //
    if (annotations.length == 0) return false;

    //
    List<Element> done = new ArrayList<Element>();
    this.filer = processingEnv.getFiler();
    this.utils = processingEnv.getElementUtils();

    //
    ModelContext ctx = new ModelContext();
    ctx.setClasses(annotations);
    ctx.setUtils(utils);

    //
    for (Class clazz : annotations) {
      Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(clazz);
      for (Element element : elements) {

        if (done.contains(element))
          continue;

        if (element.getKind() == ElementKind.CLASS) {
          MetaModel metamodel = buildMetaModel((TypeElement) element, ctx);

          writeState(metamodel);

          try {
            String templateName = clazz.getSimpleName();
            FileObject file = filer.createResource(StandardLocation.SOURCE_OUTPUT, "target", "generated/" + ((TypeElement) element).getQualifiedName() + ext, null);
            OutputStream os = file.openOutputStream();

            new FreemarkerRenderer().render(templateName, metamodel, os, filer);

          } catch (IOException e) {
            e.printStackTrace();
          }

        }
        done.add(element);
      }
    }

    return false;
    
  }

  private MetaModel buildMetaModel(TypeElement el, ModelContext ctx) {

    TemplateElementVisitor visitor = new TemplateElementVisitor();
    MetaModel model = el.accept(visitor, ctx);
    return model;
    
  }

  private void writeState(MetaModel metaModel) {

    try {
      FileObject servicesfile = filer.createResource(StandardLocation.SOURCE_OUTPUT, "target", "metaModel", null);
      ObjectOutputStream oos = new ObjectOutputStream(servicesfile.openOutputStream());
      oos.writeObject(metaModel);
      oos.flush();
      oos.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

  }

  protected abstract Class[] annotations();
  protected abstract String ext();

}
