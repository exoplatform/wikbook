package model;

import org.wikbook.template.api.AnnotationA;
import org.wikbook.template.api.AnnotationB;
import org.wikbook.template.api.AnnotationC;

@AnnotationA("d")
public class D {

  /**
   * General comment.
   * @author foo
   * @author bar
   * @data here there is
   * a
   * bloc
   * @since 1.0
   * @deprecated
   */
  @AnnotationC
  @AnnotationA("bar")
  @AnnotationB({"a/b", "c/d"})
  void m() {}

}