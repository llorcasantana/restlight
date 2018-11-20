package restlight;

public class StringRequest extends Request<String> {

  @Override
  public String parseResponse(Response<String> response) throws Exception {
   return response.string(getCharset());
  }
}
