package org.wikbook.template.processing;

import org.wikbook.template.processing.metamodel.TemplateAnnotation;
import org.wikbook.template.processing.metamodel.MetaModel;
import org.wikbook.template.processing.metamodel.ModelContext;
import org.wikbook.template.processing.metamodel.TemplateElement;
import org.wikbook.template.processing.metamodel.TemplateType;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
    return null;
  }

  public List<TemplateElement> visitType(TypeElement typeElement, ModelContext ctx) {

    TemplateType type = new TemplateType(typeElement.getSimpleName().toString(), typeElement.getQualifiedName().toString(), false);
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

        TemplateAnnotation annotation = createAnnotation(a, classElement);

        classElement.addAnnotation(annotation);
        if (!elements.contains(classElement)) {
          elements.add(classElement);
        }

        for (Element child : typeElement.getEnclosedElements()) {
          child.accept(this, ctx);
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
      TemplateAnnotation annotation = createAnnotation(a, paramElement);
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

  public List<TemplateElement> visitExecutable(ExecutableElement executableElement, ModelContext ctx) {

    TypeMirror returnType = executableElement.getReturnType();
    TemplateType type = buildTemplateType(returnType, ctx);
    String executableName = executableElement.getSimpleName().toString();
    TemplateElement methodElement = new TemplateElement(executableName, type);
    TemplateElement typeElement = ctx.getTypeElement();

    applyDoc(executableElement, methodElement, ctx);

    for (Class clazz : ctx.getAnnotations()) {

      Annotation a = executableElement.getAnnotation(clazz);
      TemplateAnnotation annotation = createAnnotation(a, methodElement);
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

  public List<TemplateElement> visitTypeParameter(TypeParameterElement typeParameterElement, ModelContext ctx) {
    return null;
  }

  public List<TemplateElement> visitUnknown(Element element, ModelContext ctx) {
    return null;
  }

  private TemplateAnnotation createAnnotation(Annotation a, TemplateElement element) {

    if (a == null) {
      return null;
    }

    Class clazz = a.annotationType();

    TemplateAnnotation annotation = new TemplateAnnotation(clazz.getSimpleName(), element);
    for (Method method : clazz.getMethods()) {
      if (method.getDeclaringClass().equals(clazz)) {
        try {

          Class type = method.getReturnType();

          // Annotation[]
          if (type.isArray() && type.getComponentType().isAnnotation()) {

            Annotation[] annotations = (Annotation[]) method.invoke(a);
            List<TemplateAnnotation> tAs = new ArrayList<TemplateAnnotation>();

            for (Annotation currentAnnotation : annotations) {
              TemplateAnnotation currentTA = createAnnotation(currentAnnotation, element);
              tAs.add(currentTA);
            }
            
            annotation.addValue(method.getName(), tAs.toArray(new TemplateAnnotation[]{}));

          }

          // Annotation
          else if (type.isAnnotation()) {
            TemplateAnnotation sub = createAnnotation((Annotation) method.invoke(a), element);
            annotation.addValue(method.getName(), sub);
          }

          // Object and Object[]
          else {
            annotation.addValue(method.getName(), method.invoke(a));
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    return annotation;

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
    Scanner sc = new Scanner(documentation);
    String currentName = null;
    List<String> l = new ArrayList<String>();
    while (sc.hasNextLine()) {
      String line = cleanLeft(sc.nextLine());
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

    TypeElement returnTypeElement = (TypeElement) ctx.getTypesUtils().asElement(typeMirror);
    String returnName = returnTypeElement == null ? "" : returnTypeElement.getSimpleName().toString();
    String returnFullName = returnTypeElement == null ? "" : returnTypeElement.getQualifiedName().toString();
    
    Boolean isArray = TypeKind.ARRAY.equals(typeMirror.getKind());
    if (isArray) {
      returnFullName = typeMirror.toString();
      int lastDot = typeMirror.toString().lastIndexOf(".");
      returnName = typeMirror.toString().substring(lastDot + 1);
    }

    return new TemplateType(returnName, returnFullName, isArray);

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
