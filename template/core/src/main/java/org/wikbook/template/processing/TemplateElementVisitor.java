package org.wikbook.template.processing;

import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.ModelContext;
import org.wikbook.template.processing.metamodel.TemplateElement;
import org.wikbook.template.processing.metamodel.TemplateType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class TemplateElementVisitor implements ElementVisitor<List<TemplateElement>, ModelContext> {

  public List<TemplateElement> visit(Element element, ModelContext ctx) {
    return null;
  }

  public List<TemplateElement> visit(Element element) {
    return null;
  }

  public List<TemplateElement> visitPackage(PackageElement packageElement, ModelContext ctx) {

    List<TemplateElement> elements = new ArrayList<TemplateElement>();
    TemplateElement pkgTmplElement = new TemplateElement(packageElement.getQualifiedName().toString());

    applyDoc(packageElement, pkgTmplElement, ctx);

    for (Class clazz : ctx.getAnnotations()) {
      Annotation a = packageElement.getAnnotation(clazz);

      if (a != null) {

        TemplateAnnotation annotation = ProcessingUtils.createAnnotation(a, pkgTmplElement);
        
        pkgTmplElement.addAnnotation(annotation);
        if (!elements.contains(pkgTmplElement)) {
          elements.add(pkgTmplElement);
        }
        
        for (Element el : packageElement.getEnclosedElements()) {
          for (TemplateElement te : el.accept(this, ctx)) {
            pkgTmplElement.addElement(te);
          }
        }

      }
    }

    return elements;

  }

  public List<TemplateElement> visitType(TypeElement typeElement, ModelContext ctx) {


    TypeMirror paramType = typeElement.asType();
    TemplateType type = buildTemplateType(paramType, ctx);
    TemplateElement classElement = new TemplateElement(typeElement.getSimpleName().toString(), type);

    applyDoc(typeElement, classElement, ctx);

    if (ctx == null) {
      throw new NullPointerException();
    }

    List<TemplateElement> elements = new ArrayList<TemplateElement>();
    ctx.setTypeElement(classElement);

    for (Class clazz : ctx.getAnnotations()) {
      Annotation a = typeElement.getAnnotation(clazz);

      if (a != null) {

        TemplateAnnotation annotation = ProcessingUtils.createAnnotation(a, classElement);

        classElement.addAnnotation(annotation);
        if (!elements.contains(classElement)) {
          elements.add(classElement);
        }

        for (Element child : typeElement.getEnclosedElements()) {
          if (child.getKind().equals(ElementKind.METHOD)) {
            child.accept(this, ctx);
          }
        }

      }
    }


    return elements;
    
  }

  public List<TemplateElement> visitVariable(VariableElement variableElement, ModelContext ctx) {

    TypeMirror paramType = variableElement.asType();
    TemplateType type = buildTemplateType(paramType, ctx);
    TemplateElement paramElement = new TemplateElement(variableElement.getSimpleName().toString(), type);
    TemplateElement methodElement = ctx.getExecutableElement();

    for (Class clazz : ctx.getAnnotations()) {
      Annotation a = variableElement.getAnnotation(clazz);
      TemplateAnnotation annotation = ProcessingUtils.createAnnotation(a, paramElement);
      if (annotation != null) {
        for (String key : methodElement.getJavadoc().keySet()) {
          if ("param".equals(key)) {
            List<List<String>> paramsDoc = methodElement.getJavadoc().get(key);
            docParam(annotation, variableElement, paramsDoc);
          }
        }
        paramElement.addAnnotation(annotation);
        if (!methodElement.getElements().contains(paramElement)) {
          methodElement.addElement(paramElement);
        }
      }
    }

    return null;
  }

  public List<TemplateElement> visitExecutable(ExecutableElement executableElement, ModelContext ctx) {

    TypeMirror returnType = executableElement.getReturnType();
    TemplateType type = buildTemplateType(returnType, ctx);
    String executableName = executableElement.getSimpleName().toString();
    TemplateElement methodElement = new TemplateElement(executableName, type);
    TemplateElement typeElement = ctx.getTypeElement();

    applyDoc(executableElement, methodElement, ctx);

    for (Class clazz : ctx.getAnnotations()) {

      Annotation a = executableElement.getAnnotation(clazz);
      TemplateAnnotation annotation = ProcessingUtils.createAnnotation(a, methodElement);
      if (annotation != null) {

        methodElement.addAnnotation(annotation);
        if (!typeElement.getElements().contains(methodElement)) {
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

  public List<TemplateElement> visitTypeParameter(TypeParameterElement typeParameterElement, ModelContext ctx) {
    return null;
  }

  public List<TemplateElement> visitUnknown(Element element, ModelContext ctx) {
    return null;
  }

  private void doc(TemplateElement tel, String name, List<String> l) {

    if (name == null) {
      tel.getJavadoc(null).add(new ArrayList<String>(l));
    }
    else {
      if (l.size() == 0) {
        l.add(name.substring(1));
      }
      tel.getJavadoc(name.substring(1)).add(new ArrayList<String>(l));
    }

    l.clear();

  }

  private void applyDoc(Element el, TemplateElement tel, ModelContext ctx) {
    
    //
    String documentation = ctx.getElementsUtils().getDocComment(el);
    if (documentation == null) return;

    //
    BufferedReader br = new BufferedReader(new StringReader(documentation));
    String currentName = null;
    List<String> l = new ArrayList<String>();
    try {
      String line;
      while ((line = br.readLine()) != null) {
        line = cleanLeft(line);
        if (line.startsWith("@")) {
          doc(tel, currentName, l);
          int delimiterPos = line.indexOf(" ");
          if (delimiterPos != -1) {
            currentName = line.substring(0, delimiterPos);
            l.add(line.substring(delimiterPos + 1));
          }
          else {
            currentName = line;
          }
        }
        else {
          l.add(line);
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
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

  private TemplateType buildTemplateType(TypeMirror typeMirror, ModelContext ctx) {

    switch (typeMirror.getKind()) {

      case ARRAY:
        ArrayType arrayType = (ArrayType) typeMirror;
        DeclaredType componentDeclaredType = (DeclaredType) arrayType.getComponentType();
        TemplateType templateType = buildTemplateType(componentDeclaredType, ctx);

        return new TemplateType(
            templateType.getName(),
            templateType.getFqn(),
            true,
            templateType.getParameters());

      case DECLARED:
        DeclaredType declaredType = (DeclaredType) typeMirror;
        TypeElement declaredTypeElement = (TypeElement) declaredType.asElement();
        List<TemplateType> typeParameters = new ArrayList<TemplateType>();
        for (TypeMirror mirror : declaredType.getTypeArguments()) {
          typeParameters.add(buildTemplateType(mirror, ctx));
        }

        return new TemplateType(
            declaredTypeElement.getSimpleName().toString(),
            declaredTypeElement.getQualifiedName().toString(),
            false,
            typeParameters.toArray(new TemplateType[]{}));

      default:
        return new TemplateType("", "", false, new TemplateType[]{});
    }

  }

  private String cleanLeft(String newJavadocEntry) {
    
    for (int i = 0; i < newJavadocEntry.length(); ++i) {

      switch (newJavadocEntry.charAt(i)) {
        case ' ':
          continue;

        case '@':
          return newJavadocEntry.substring(i);

        default:
          return newJavadocEntry;
      }

    }

    return "";

  }

}
