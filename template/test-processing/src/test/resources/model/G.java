package model;

import org.wikbook.template.api.AnnotationA;
import org.wikbook.template.api.AnnotationC;

@AnnotationA("g")
public class G {

  @AnnotationC
  @AnnotationA("foo")
  String[] m2() { return null; }

}