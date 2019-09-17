package restlight.request;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import restlight.Request;
import restlight.ResponseBody;

public class GsonRequest<T> extends Request.Parse<T> {

  private final Gson gson;
  final TypeAdapter<T> adapter;

  /**
   * Make a request and return a parsed object from JSON.
   *
   * @param gson
   * @param adapter Relevant class object, for Gson's reflection
   */
  public GsonRequest(Gson gson, TypeAdapter<T> adapter) {
    this.gson = gson;
    this.adapter = adapter;
  }

  public static <V> GsonRequest<V> of(Gson gson, Class<V> classOf) {
    return new GsonRequest<V>(gson, gson.getAdapter(classOf));
  }
  public static <V> GsonRequest<V> of(Class<V> classOf) {
    return of(new Gson(), classOf);
  }
  
  public static <V> GsonRequest<V> of(Gson gson, TypeToken<V> token) {
    return new GsonRequest<V>(gson, gson.getAdapter(token));
  }
  public static <V> GsonRequest<V> of(TypeToken<V> token) {
    return of(new Gson(), token);
  }

  public final TypeAdapter<T> getAdapter() {
    return adapter;
  }

  public Gson getGson() {
    return gson;
  }

  @Override public T parseResponse(ResponseBody response) throws Exception {
    Reader json = response.charStream(getCharset());
    //String json = response.string(getCharset());
    JsonReader reader = gson.newJsonReader(json);
    return adapter.read(reader);
  }
}