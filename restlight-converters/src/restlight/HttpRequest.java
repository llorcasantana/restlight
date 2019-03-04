package restlight;

import java.util.HashMap;

public class HttpRequest<T> extends Request.Parse<T> { 

  public static final HashMap<Class<?>, Adapter<?>> ADAPTERS;
  public static final HashMap<Class<?>, Converter<?>> CONVERTERS;
  
  public static Adapter defaultAdapter;
  public static Converter defaultConverter;
  
  static {
    ADAPTERS = new HashMap<Class<?>, Adapter<?>>();
    CONVERTERS = new HashMap<Class<?>, Converter<?>>();
  }
  
  public static <V> void registerAdapter(Class<V> classOf, Adapter<V> converter) {
    ADAPTERS.put(classOf, converter);
  }        
          
  public static <V> void registerConverter(Class<V> classOf, Converter<V> converter) {
    CONVERTERS.put(classOf, converter);
  }
  
  public static <V> HttpRequest<V> of(Converter<V> converter) {
    return new HttpRequest<V>(converter);
  }

  public static <V> HttpRequest<V> of(Class<V> classOf) {
     Converter converter = CONVERTERS.get(classOf);
     return (converter == null) ? of(defaultConverter) : of(converter);
  }
  
  private final Converter<T> converter;

  private HttpRequest(Converter<T> converter) {
    this.converter = converter;
  }

  public HttpRequest() {
    this(null);
  }
  
  public Converter<T> getConverter() {
    return converter;
  }
  
  @Override public T parseResponse(ResponseBody response) throws Exception {
    if (converter == null) {
      throw new UnsupportedOperationException("converter not supported yet.");
    }
    return converter.converter(response, getCharset());
  }
  
  public void setAction(String method, String url) {
    setMethod(method);
    setUrl(url);
  }
  
  public void setBody(Object src) {
    Adapter adapter = ADAPTERS.get(src.getClass());
    if (adapter == null) 
      adapter = defaultAdapter;
    if (adapter != null) 
      setBody(adapter.adapter(src));
    else
      setBody(new NoRequestBody(src));
  }
}
