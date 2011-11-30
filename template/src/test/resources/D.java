import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("d")
public class D {

  /**
   * General comment.
   * @author foo
   * @author bar
   * @since 1.0
   * @deprecated
   */
  @POST
  @Path("bar")
  void m() {}

}