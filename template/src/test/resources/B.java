import javax.ws.rs.Consumes;
import javax.ws.rs.Path;

@Path("b")
@Consumes({"a/b", "c/d"})
public class B {}