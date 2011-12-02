import javax.ws.rs.Consumes;
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
  @Consumes({"a/b", "c/d"})
  void m() {}

}