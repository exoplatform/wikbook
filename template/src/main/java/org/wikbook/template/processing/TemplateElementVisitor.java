package org.wikbook.template.processing;

import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.ModelContext;
import org.wikbook.template.processing.metamodel.TemplateElement;

import javax.lang.model.element.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class TemplateElementVisitor implements ElementVisitor<MetaModel, ModelContext> {

  public MetaModel visit(Element element, ModelContext ctx) {
    return null;
  }

  public MetaModel visit(Element element) {
    return null;
  }

  public MetaModel visitPackage(PackageElement packageElement, ModelContext ctx) {
    return null;
  }

  public MetaModel visitType(TypeElement typeElement, ModelContext ctx) {

    TemplateElement classElement = new TemplateElement(typeElement.getSimpleName().toString());

    if (ctx == null) {
      throw new NullPointerException();
    }

    MetaModel model = new MetaModel();

    for (Class clazz : ctx.getClasses()) {
      Object a = typeElement.getAnnotation(clazz);
      
      if (a != null) {

        TemplateAnnotation annotation = createAnnotation(typeElement, clazz, ctx, classElement);

        classElement.addAnnotation(annotation);
        model.add(annotation);

        ctx.setCurrentAnnotation(annotation);
        for (Element child : typeElement.getEnclosedElements()) {
          child.accept(this, ctx);
        }

      }
    }


    return model;
    
  }

  public MetaModel visitVariable(VariableElement variableElement, ModelContext ctx) {

    TemplateElement paramElement = new TemplateElement(variableElement.getSimpleName().toString());
    TemplateAnnotation methodAnnotation = ctx.getCurrentAnnotation();

    for (Class clazz : ctx.getClasses()) {
      TemplateAnnotation annotation = createAnnotation(variableElement, clazz, ctx, paramElement);
      if (annotation != null) {
        for (String key : methodAnnotation.getJavadoc().keySet()) {
          if ("param".equals(key)) {
            List<String> paramsDoc = methodAnnotation.getJavadoc().get(key);
            docParam(annotation, variableElement, paramsDoc);
          }
        }
        paramElement.addAnnotation(methodAnnotation);
        methodAnnotation.addChild(annotation);
      }
    }

    return null;
  }

  public MetaModel visitExecutable(ExecutableElement executableElement, ModelContext ctx) {

    TemplateElement methodElement = new TemplateElement(executableElement.getSimpleName().toString());
    TemplateAnnotation typeAnnotation = ctx.getCurrentAnnotation();

    for (Class clazz : ctx.getClasses()) {

      TemplateAnnotation annotation = createAnnotation(executableElement, clazz, ctx, methodElement);

      if (annotation != null) {

        methodElement.addAnnotation(annotation);
        typeAnnotation.addChild(annotation);

        ctx.setCurrentAnnotation(annotation);
        for (VariableElement e : executableElement.getParameters()) {
          e.accept(this, ctx);
        }
      }
    }
    
    return null;

  }

  public MetaModel visitTypeParameter(TypeParameterElement typeParameterElement, ModelContext ctx) {
    return null;
  }

  public MetaModel visitUnknown(Element element, ModelContext ctx) {
    return null;
  }

  private TemplateAnnotation createAnnotation(Element el, Class clazz, ModelContext ctx, TemplateElement element) {

    Object a = el.getAnnotation(clazz);

    if (a != null) {
      TemplateAnnotation annotation = new TemplateAnnotation(clazz.getSimpleName(), element);
      for (Method method : clazz.getMethods()) {
        if (method.getDeclaringClass().equals(clazz)) {
          try {
            annotation.addValue(method.getName(), method.invoke(a).toString());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }

      //
      String documentation = ctx.getUtils().getDocComment(el);
      if (documentation == null) return annotation;

      //
      Scanner sc = new Scanner(documentation);
      String currentName = null;
      StringBuilder b = new StringBuilder();
      while (sc.hasNextLine()) {
        String line = sc.nextLine().trim();
        if (line.startsWith("@")) {
          doc(annotation, currentName, b);
          int delimiterPos = line.indexOf(" ");
          if (delimiterPos != -1) {
            currentName = line.substring(0, delimiterPos);
            b.append(line.substring(delimiterPos + 1));
          }
          else {
            currentName = line;
            b.append(line.substring(1));
          }
        }
        else {
          b.append(line + " ");
        }
      }
      doc(annotation, currentName, b);

      return annotation;
    }
    return null;
  }

  private void doc(TemplateAnnotation annotation, String name, StringBuilder b) {

    if (name == null) {
      annotation.getJavadoc(null).add(b.toString().trim());
    }
    else {
      annotation.getJavadoc(name.substring(1)).add(b.toString().trim());
    }

    b.delete(0, b.length());

  }

  private void docParam(TemplateAnnotation annotation, VariableElement variableElement, List<String> paramsDoc) {

    for (String docValue : paramsDoc) {
      String elementName  = variableElement.getSimpleName().toString();
      if (docValue.startsWith(elementName)) {
        int pos = elementName.length() + 1;
        if (pos < docValue.length()) {
          annotation.getJavadoc(null).add(docValue.substring(pos));
        }
        else {
          annotation.getJavadoc(null).add("");
        }
      }
    }

  }

}
