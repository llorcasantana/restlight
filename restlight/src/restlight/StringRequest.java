package restlight;

public class StringRequest extends Request.Parse<String> {

  @Override
  public String parseResponse(ResponseBody response) throws Exception {
   return response.string(getCharset());
  }
}
