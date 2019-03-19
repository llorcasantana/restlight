package restlight.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSON {

  public static JSON of(String json) throws JSONException {
    if (json == null) 
      throw new JSONException("json == null");

    if (json.isEmpty()) 
      throw new JSONException("json is empty");

    int c = json.charAt(0);
    switch (c) {
      case '{':
        return new JSON(new JSONObject(json), null);

      case '[':
        return new JSON(null, new JSONArray(json));

      default:
        throw new JSONException("invalidate: " + json);
    }
  }
  
  private final JSONObject object;
  private final JSONArray array;
  private final boolean isObject;
  private final boolean isArray;

  JSON(JSONObject object, JSONArray array) {
    this.object = object;
    this.array = array;
    this.isObject = object != null;
    this.isArray = array != null;
  }

  public JSONObject getObject() {
    return object;
  }

  public JSONObject getObjectOrEmpty() {
    return isObject() ? getObject() : new JSONObject();
  }

  public JSONArray getArray() {
    return array;
  }

  public JSONArray getArrayOrEmpty() {
    return isArray() ? getArray() : new JSONArray();
  }

  public boolean isObject() {
    return isObject;
  }

  public boolean isArray() {
    return isArray;
  }

  public String toString(int indentFactor) {
    if (isArray()) 
      return array.toString(indentFactor);
    if (isObject()) 
      return object.toString(indentFactor);
    else 
      return null;
  }

  @Override public String toString() {
    return toString(0);
  }
}