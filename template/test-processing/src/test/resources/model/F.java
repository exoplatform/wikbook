package model;

import org.wikbook.template.api.AnnotationA;
import org.wikbook.template.api.AnnotationC;
import org.wikbook.template.api.AnnotationD;


@AnnotationA("f")
public class F {

  @AnnotationC
  @AnnotationA("bar")
  void m() {}

  @AnnotationD
  @AnnotationA("bar2")
  String m2() { return null; }

  @AnnotationC
  @AnnotationA("bar3")
  void m3(@AnnotationA("pName") String p) {}

}