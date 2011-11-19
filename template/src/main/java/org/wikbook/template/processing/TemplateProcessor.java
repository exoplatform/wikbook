package org.wikbook.template.processing;

import org.wikbook.template.freemarker.FreemarkerModelBuilder;
import org.wikbook.template.freemarker.FreemarkerRenderer;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.ModelContext;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
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
@SupportedSourceVersion(SourceVersion.RELEASE_5)
@SupportedAnnotationTypes({"*"})
public class TemplateProcessor extends AbstractProcessor {

  private Class[] classes;
  private Filer filer;
  private Elements utils;

  @Override
  public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

    classes = annotations();
    this.filer = processingEnv.getFiler();
    this.utils = processingEnv.getElementUtils();

    ModelContext ctx = new ModelContext();
    ctx.setClasses(classes);
    ctx.setUtils(utils);

    for (Class clazz : classes) {
      Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(clazz);
      for (Element element : elements) {
        
        if (element.getKind() == ElementKind.CLASS) {
          MetaModel metamodel = buildMetaModel((TypeElement) element, ctx);

          try {
            copyTemplate();
            FileObject file = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", "" + ((TypeElement) element).getQualifiedName() + ".txt", null);
            OutputStream os = file.openOutputStream();

            new FreemarkerRenderer().render(metamodel, os);

          } catch (IOException e) {
            e.printStackTrace();
          }

        }
      }
    }

    return false;
    
  }

  private Class[] annotations() {

    List<Class> classes = new ArrayList<Class>();

    String annotationNames = System.getProperty("wikbook.template.annotations");
    String[] annotations = annotationNames.split(",");
    for (String annotationName : annotations) {
      try {
        classes.add(Class.forName(annotationName));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        continue;
      }
    }
    
    return classes.toArray(new Class[]{});

  }

  private MetaModel buildMetaModel(TypeElement el, ModelContext ctx) {

    TemplateElementVisitor visitor = new TemplateElementVisitor();
    MetaModel model = el.accept(visitor, ctx);
    return model;
    
  }

  private void copyTemplate() throws IOException {

    //
    FileObject fileObjectTemplate = filer.getResource(StandardLocation.SOURCE_PATH, "templates", "template.txt");
    InputStream sourceTemplate = fileObjectTemplate.openInputStream();

    new File("templates").mkdir();
    FileOutputStream template = new FileOutputStream("templates/templateTmp.txt");
    Scanner scanner = new Scanner(sourceTemplate);
    while (scanner.hasNextLine()) {
      template.write((scanner.nextLine() + "\n").getBytes());
    }
    template.flush();
    template.close();
  }

}
