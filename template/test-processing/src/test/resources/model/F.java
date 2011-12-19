package model;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("f")
public class F {

  @POST
  @Path("bar")
  void m() {}

  @GET
  @Path("bar2")
  Response m2() { return null; }

  @POST
  @Path("bar3")
  void m3(@PathParam("pathParameter") String pathParameter) {}

}