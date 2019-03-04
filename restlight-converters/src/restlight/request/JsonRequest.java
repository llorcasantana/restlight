package restlight.request;

import org.json.JSONArray;
import org.json.JSONObject;
import restlight.Request;
import restlight.ResponseBody;

public abstract class JsonRequest<T> extends Request.Parse<T> {

  public static class Object extends JsonRequest<JSONObject> {
    @Override
    public JSONObject parseResponse(ResponseBody response) throws Exception {
      String json = response.string(getCharset());
      return new JSONObject(json);
    }
  }
  
  public static class Array extends JsonRequest<JSONArray> {
    @Override
    public JSONArray parseResponse(ResponseBody response) throws Exception {
      String json = response.string(getCharset());
      return new JSONArray(json);
    }
  }
}
