package restlight;

public class StringRequest extends Request<String> {

  @Override
  public String parseResponse(ResponseBody response) throws Exception {
   return response.string(getCharset());
  }
}
