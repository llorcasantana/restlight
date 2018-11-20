package restlight;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import restlight.io.IOUtils;

public class Response<T> implements Closeable {
  final Request<T> request;
  int code;  
  Headers headers;
  String contentEncoding;
  String contentType;
  int contentLength;
  T result;
  InputStream inputStream;

  public Response(Request<T> request) {
    this.request = request;
  }
  
  public int code() { return code; }

  public Headers headers() { return headers; }
  
  public String contentEncoding() { return contentEncoding; }
  
  public String contentType() { return contentType; }
  
  public int contentLength() { return contentLength; }
  
  public Request<T> request() { return request; }
  
  public T result() { return result; }
    
  public InputStream inputStream() { 
    return inputStream; 
  }
  public Reader charStream(Charset charset) throws IOException {
    return new InputStreamReader(inputStream, charset);
  }
  public byte[] bytes() throws IOException {
    //try {
      return IOUtils.toByteArray(inputStream);
//    } finally {
//      IOUtils.closeQuietly(inputStream);
//    }
  }
  public String string(Charset charset) throws IOException {
    byte[] data = bytes();
    return new String(data, charset);
  }
  public Response<T> parseResponse() throws Exception {
    if (result != null) return this;
    try {
      result = request.parseResponse(this);
      return this;
    } finally {
      close();
    }
  }
  @Override public void close() {
    IOUtils.closeQuietly(inputStream);
  }   
  @Override public String toString() {
    return String.valueOf(result);
  }
}
