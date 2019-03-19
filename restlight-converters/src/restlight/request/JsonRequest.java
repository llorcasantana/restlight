package restlight.request;

import org.json.JSONArray;
import org.json.JSONObject;
import restlight.Request;
import restlight.ResponseBody;
import restlight.json.JSON;

public class JsonRequest extends Request.Parse<JSON> {
  
  public static class Object extends Request.Parse<JSONObject> {
    @Override
    public JSONObject parseResponse(ResponseBody response) throws Exception {
      String json = response.string(getCharset());
      return new JSONObject(json);
    }
  }
  
  public static class Array extends Request.Parse<JSONArray> {
    @Override
    public JSONArray parseResponse(ResponseBody response) throws Exception {
      String json = response.string(getCharset());
      return new JSONArray(json);
    }
  }
  
  @Override
  public JSON parseResponse(ResponseBody response) throws Exception {
    String json = response.string(getCharset());
    return JSON.of(json);
  }
}
