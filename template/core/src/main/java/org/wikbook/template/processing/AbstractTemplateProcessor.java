package org.wikbook.template.processing;

import org.wikbook.template.freemarker.FreemarkerRenderer;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.ModelContext;
import org.wikbook.template.processing.metamodel.TemplateElement;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
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
  private Elements elementsUtils;
  private Types typesUtils;
  private String templateName;
  private String generatedDirectory;
  private String ext;
  private MetaModel metaModel;

  protected AbstractTemplateProcessor() {
    this.metaModel = new MetaModel();
  }

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
    templateName = templateName();
    if (templateName == null) {
      throw new NullPointerException();
    }

    //
    generatedDirectory = generatedDirectory();
    if (generatedDirectory == null) {
      throw new NullPointerException();
    }

    //
    if (annotations.length == 0) return false;

    //
    List<Element> done = new ArrayList<Element>();
    this.filer = processingEnv.getFiler();
    this.elementsUtils = processingEnv.getElementUtils();
    this.typesUtils = processingEnv.getTypeUtils();

    //
    ModelContext ctx = new ModelContext();
    ctx.setClasses(annotations);
    ctx.setElementsUtils(elementsUtils);
    ctx.setTypesUtils(typesUtils);

    //
    for (Class clazz : annotations) {
      Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(clazz);
      for (Element element : elements) {

        if (done.contains(element))
          continue;

        if (element.getKind() == ElementKind.CLASS) {
          List<TemplateElement> modelElements = buildElements((TypeElement) element, ctx);
          metaModel.addAll(modelElements);

        }
        done.add(element);
      }
    }

    if (roundEnvironment.processingOver()) {

      writeState(metaModel);

      for (TemplateElement el : metaModel.getElements()) {

        try {
          FileObject file = filer.createResource(StandardLocation.SOURCE_OUTPUT, generatedDirectory, "" + el.getType().getFqn() + ext, null);
          OutputStream os = file.openOutputStream();

          new FreemarkerRenderer().render(metaModel, templateName, el, os, filer);

        } catch (IOException e) {
          e.printStackTrace();
        }
        
      }


    }

    return false;
    
  }

  private List<TemplateElement> buildElements(TypeElement el, ModelContext ctx) {

    TemplateElementVisitor visitor = new TemplateElementVisitor();
    List<TemplateElement> elements = el.accept(visitor, ctx);
    return elements;
    
  }

  private void writeState(MetaModel metaModel) {

    try {
      FileObject servicesfile = filer.createResource(StandardLocation.SOURCE_OUTPUT, generatedDirectory, "metaModel", null);
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
  protected abstract String templateName();
  protected abstract String generatedDirectory();
  protected abstract String ext();

}
