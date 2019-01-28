package restlight.request;

import com.google.gson.Gson;
import java.io.Reader;
import restlight.Request;
import restlight.Response;

public class GsonRequest<T> extends Request<T> {

  private final Gson gson;
  private final Class<T> classOf;

  /**
   * Make a request and return a parsed object from JSON.
   *
   * @param gson
   * @param classOf Relevant class object, for Gson's reflection
   */
  private GsonRequest(Gson gson, Class<T> classOf) {
    this.gson = gson;
    this.classOf = classOf;
  }

  public static <V> Request<V> newRequest(Gson gson, Class<V> classOf) {
    return new GsonRequest<V>(gson, classOf);
  }
  
  public static <V> Request<V> newRequest(Class<V> classOf) {
    return new GsonRequest<V>(new Gson(), classOf);
  }

  public final Class<T> getClassOf() {
    return classOf;
  }

  public Gson getGson() {
    return gson;
  }

  @Override
  public T parseResponse(Response<T> response) throws Exception {
    Reader json = response.charStream(getCharset());
    //String json = response.string(getCharset());
    return gson.fromJson(json, classOf);
  }
}