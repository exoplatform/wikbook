package model;

import org.wikbook.template.api.AnnotationA;
import org.wikbook.template.api.AnnotationB;
import org.wikbook.template.api.AnnotationB2;
import org.wikbook.template.api.AnnotationB3;

@AnnotationA("b")
@AnnotationB({"a", "b"})
@AnnotationB2(@AnnotationA("a"))
@AnnotationB3({@AnnotationA("a1"), @AnnotationA("a2")})
public class B {}