import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("d")
public class D_JaxRs {

  /**
   * My method.
   * @author foo
   */
  @POST
  @Path("bar")
  void m() {}

}