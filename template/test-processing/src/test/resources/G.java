import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("g")
public class G {

  @GET
  @Path("foo")
  Response[] m2() { return null; }

}