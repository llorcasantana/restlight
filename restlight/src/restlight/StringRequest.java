package restlight;

public class StringRequest extends Request<String> {

  @Override
  public String parseResponse(Response.Network<String> response) throws Exception {
   return new String(response.readByteArray(), getCharset());
  }
}
