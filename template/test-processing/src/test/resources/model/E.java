package model;

import org.wikbook.template.api.AnnotationA;
import org.wikbook.template.api.AnnotationA2;
import org.wikbook.template.api.AnnotationC;

import javax.ws.rs.core.UriInfo;

@AnnotationA("d")
public class E {

  /**
   * @param uriInfo A
   * @param p1 P1 parameter description
   * @param p2 P2 parameter description
   */
  @AnnotationC
  @AnnotationA("bar")
  void m(
      @AnnotationC UriInfo uriInfo,
      @AnnotationA("p1Name") String p1,
      @AnnotationA2("p2Name") String[] p2,
      String nothing) {}

}