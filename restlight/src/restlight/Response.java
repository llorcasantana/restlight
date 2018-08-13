
package restlight;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import restlight.io.IOUtils;

public class Response<T> {

  final int code;  
  Headers headers;
  String contentEncoding;
  String contentType;
  int contentLength;
  Request<T> request;
  T result;

  public Response(int code) {
    this.code = code;
  }
  
  public int code() { return code; }

  public Headers headers() { return headers; }
  
  public String contentEncoding() { return contentEncoding; }
  
  public String contentType() { return contentType; }
  
  public int contentLength() { return contentLength; }
  
  public Request<T> request() { return request; }
  
  public T result() { return result; }
  
  @Override public String toString() {
    return String.valueOf(result);
  }
  
  public static class Network<T> extends Response<T> implements Closeable {
    InputStream source;
    public Network(int code) {
      super(code);
    }
    public InputStream source() { 
      return source; 
    }
    public byte[] readByteArray() throws IOException {
      return IOUtils.toByteArray(source);
    }
    public Response<T> response() throws Exception {
      if (result == null) result = request.parseResponse(this);
      return this;
    }
    @Override public void close() {
      IOUtils.closeQuietly(source);
    }
  }
}
