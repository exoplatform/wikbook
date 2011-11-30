import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("d")
public class E {

  /**
   * @param uriInfo A
   * @param pathParameter Path parameter description
   * @param queryParameter Query parameter description
   */
  @Path("bar")
  void m(
      @Context UriInfo uriInfo,
      @PathParam("pathParameter") String pathParameter,
      @QueryParam("queryParameter") String queryParameter,
      String nothing) {}

}