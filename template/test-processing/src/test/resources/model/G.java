package model;

import org.wikbook.template.api.AnnotationA;
import org.wikbook.template.api.AnnotationC;

import java.util.Collection;
import java.util.Map;

@AnnotationA("g")
public class G {

  @AnnotationC
  @AnnotationA("foo")
  String[] m2() { return null; }

  @AnnotationC
  @AnnotationA("foo2")
  Collection<String> m3() { return null; }

  @AnnotationC
  @AnnotationA("foo3")
  Map<Float, Integer[]> m4() { return null; }

}