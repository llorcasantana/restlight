package restlight;

import java.util.HashMap;

public class SimpleRequest<T> extends Request<T> { 

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
  
  public static <V> SimpleRequest<V> of(Converter<V> converter) {
    return new SimpleRequest<V>(converter);
  }

  public static <V> SimpleRequest<V> of(Class<V> classOf) {
     Converter converter = CONVERTERS.get(classOf);
     return (converter == null) ? of(defaultConverter) : of(converter);
  }
  
  private final Converter<T> converter;

  private SimpleRequest(Converter<T> converter) {
    this.converter = converter;
  }

  public SimpleRequest() {
    this(null);
  }
  
  public Converter<T> getConverter() {
    return converter;
  }
  
  @Override public T parseResponse(ResponseBody response) throws Exception {
    if (converter == null) {
      throw new UnsupportedOperationException("converter not supported yet.");
    }
    return converter.converter(response);
  }
  
  public Request<T> setAction(String method, String url) {
    return setMethod(method).setUrl(url);
  }
  
  public Request<T> setBody(Object src) {
    Adapter adapter = ADAPTERS.get(src.getClass());
    if (adapter == null) 
      adapter = defaultAdapter;
    if (adapter != null) 
      return setBody(adapter.adapter(src));
    else
      return setBody(new NoRequestBody(src));
  }
}
