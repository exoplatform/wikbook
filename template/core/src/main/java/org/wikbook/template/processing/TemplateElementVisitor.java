package org.wikbook.template.processing;

import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.ModelContext;
import org.wikbook.template.processing.metamodel.TemplateElement;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

    TemplateElement classElement = new TemplateElement(typeElement.getSimpleName().toString(), typeElement.getSimpleName().toString(), false);

    applyDoc(typeElement, classElement, ctx);

    if (ctx == null) {
      throw new NullPointerException();
    }

    MetaModel model = new MetaModel();
    ctx.setTypeElement(classElement);

    for (Class clazz : ctx.getAnnotations()) {
      Object a = typeElement.getAnnotation(clazz);

      if (a != null) {

        TemplateAnnotation annotation = createAnnotation(typeElement, clazz, ctx, classElement);

        classElement.addAnnotation(annotation);
        if (!model.getElements().contains(classElement)) {
          model.add(classElement);
        }

        for (Element child : typeElement.getEnclosedElements()) {
          child.accept(this, ctx);
        }

      }
    }


    return model;
    
  }

  public MetaModel visitVariable(VariableElement variableElement, ModelContext ctx) {

    TemplateElement paramElement = new TemplateElement(variableElement.getSimpleName().toString(), variableElement.getSimpleName().toString(), false);
    TemplateElement methodElement = ctx.getExecutableElement();

    for (Class clazz : ctx.getAnnotations()) {
      TemplateAnnotation annotation = createAnnotation(variableElement, clazz, ctx, paramElement);
      if (annotation != null) {
        for (String key : methodElement.getJavadoc().keySet()) {
          if ("param".equals(key)) {
            List<List<String>> paramsDoc = methodElement.getJavadoc().get(key);
            docParam(annotation, variableElement, paramsDoc);
          }
        }
        paramElement.addAnnotation(annotation);
        if (!methodElement.getElement().contains(paramElement)) {
          methodElement.addElement(paramElement);
        }
      }
    }

    return null;
  }

  public MetaModel visitExecutable(ExecutableElement executableElement, ModelContext ctx) {

    TypeMirror returnType = executableElement.getReturnType();
    Element returnElement = ctx.getTypesUtils().asElement(returnType);

    String executableName = executableElement.getSimpleName().toString();
    String returnName = returnElement == null ? "" : returnElement.getSimpleName().toString();
    Boolean isArray = TypeKind.ARRAY.equals(returnType.getKind());

    TemplateElement methodElement = new TemplateElement(executableName, returnName, isArray);
    TemplateElement typeElement = ctx.getTypeElement();

    applyDoc(executableElement, methodElement, ctx);

    for (Class clazz : ctx.getAnnotations()) {

      TemplateAnnotation annotation = createAnnotation(executableElement, clazz, ctx, methodElement);
      if (annotation != null) {

        methodElement.addAnnotation(annotation);
        if (!typeElement.getElement().contains(methodElement)) {
          typeElement.addElement(methodElement);
          ctx.setExecutableElement(methodElement);
          for (VariableElement e : executableElement.getParameters()) {
            e.accept(this, ctx);
          }
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
            annotation.addValue(method.getName(), method.invoke(a));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }

      return annotation;
    }
    return null;
  }

  private void doc(TemplateElement tel, String name, List<String> l) {

    if (name == null) {
      tel.getJavadoc(null).add(new ArrayList<String>(l));
    }
    else {
      tel.getJavadoc(name.substring(1)).add(new ArrayList<String>(l));
    }

    l.clear();

  }

  private void applyDoc(Element el, TemplateElement tel, ModelContext ctx) {
    
    //
    String documentation = ctx.getElementsUtils().getDocComment(el);
    if (documentation == null) return;

    //
    Scanner sc = new Scanner(documentation);
    String currentName = null;
    List<String> l = new ArrayList<String>();
    while (sc.hasNextLine()) {
      String line = sc.nextLine().trim();
      if (line.startsWith("@")) {
        doc(tel, currentName, l);
        int delimiterPos = line.indexOf(" ");
        if (delimiterPos != -1) {
          currentName = line.substring(0, delimiterPos);
          l.add(line.substring(delimiterPos + 1));
        }
        else {
          currentName = line;
          l.add(line.substring(1));
        }
      }
      else {
        l.add(line);
      }
    }
    doc(tel, currentName, l);
  }

  private void docParam(TemplateAnnotation annotation, VariableElement variableElement, List<List<String>> paramsDoc) {

    for (List<String> docValue : paramsDoc) {
      if (docValue.size() > 0) {
        String elementName  = variableElement.getSimpleName().toString();
        if (docValue.get(0).startsWith(elementName)) {
          int pos = elementName.length() + 1;
          if (pos < docValue.get(0).length()) {
            docValue.set(0, docValue.get(0).substring(pos));
            annotation.getJavadoc(null).add(docValue);
          }
          else {
            annotation.getJavadoc(null).add(new ArrayList<String>());
          }
        }
      }
    }

  }

}
