package restlight.request;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import restlight.Request;
import restlight.ResponseBody;

public class SimpleXmlRequest<T> extends Request<T> {

  private final Serializer serializer;
  private final Class<T> classOf;
  private final boolean strict;

  /**
   * Make a request and return a parsed object from XML.
   *
   * @param serializer
   * @param classOf Relevant class object, for Gson's reflection
   */
  public SimpleXmlRequest(Serializer serializer, Class<T> classOf, boolean strict) {
    this.serializer = serializer;
    this.classOf = classOf;
    this.strict = strict;
  }

  public static <V> SimpleXmlRequest<V> of(Serializer serializer, 
          Class<V> classOf, boolean strict) {
    return new SimpleXmlRequest<V>(serializer, classOf, strict);
  }
  
  public static <V> SimpleXmlRequest<V> of(Class<V> classOf, boolean strict) {
    return new SimpleXmlRequest<V>(new Persister(), classOf, strict);
  }

  public final Class<T> getClassOf() {
    return classOf;
  }

  public Serializer getSerializer() {
    return serializer;
  }
  
  @Override
  public T parseResponse(ResponseBody response) throws Exception {
    T read = serializer.read(classOf, response.string(getCharset()), strict);
    if (read == null) {
      throw new IllegalStateException("Could not deserialize body as " + classOf);
    }
    return read;
  }
  
}
