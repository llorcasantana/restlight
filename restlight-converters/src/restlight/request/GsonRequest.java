package restlight.request;

import com.google.gson.Gson;
import restlight.Request;
import restlight.ResponseBody;

public class GsonRequest<T> extends Request<T> {

  private final Gson gson;
  private final Class<T> classOf;

  /**
   * Make a request and return a parsed object from JSON.
   *
   * @param gson
   * @param classOf Relevant class object, for Gson's reflection
   */
  public GsonRequest(Gson gson, Class<T> classOf) {
    this.gson = gson;
    this.classOf = classOf;
  }

  public static <V> GsonRequest<V> of(Gson gson, Class<V> classOf) {
    return new GsonRequest<V>(gson, classOf);
  }
  
  public static <V> GsonRequest<V> of(Class<V> classOf) {
    return new GsonRequest<V>(new Gson(), classOf);
  }

  public final Class<T> getClassOf() {
    return classOf;
  }

  public Gson getGson() {
    return gson;
  }

  @Override
  public T parseResponse(ResponseBody response) throws Exception {
    java.io.Reader json = response.charStream(getCharset());
    //String json = response.string(getCharset());
    return gson.fromJson(json, classOf);
  }
}