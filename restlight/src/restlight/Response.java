package restlight;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import restlight.io.IOUtils;

public class Response<T> implements Closeable {
  int code;  
  Headers headers;
  String contentEncoding;
  String contentType;
  int contentLength;
  T result;
  InputStream inputStream;
  boolean closed;
  
  public int code() { return code; }

  public Headers headers() { return headers; }
  
  public String contentEncoding() { return contentEncoding; }
  
  public String contentType() { return contentType; }
  
  public int contentLength() { return contentLength; }
  
  public T result() { return result; }
    
  public InputStream inputStream() { return inputStream; }
  
  public Reader charStream(Charset charset) throws IOException {
    return new InputStreamReader(inputStream, charset);
  }
  
  public byte[] bytes() throws IOException {
    try {
      return IOUtils.toByteArray(inputStream);
    } finally {
      close();
    }
  }
  
  public String string(Charset charset) throws IOException {
    byte[] data = bytes();
    return new String(data, charset);
  }
  
  public Response<T> parseRequest(Request<T> request) throws Exception {
    try {
      result = request.parseResponse(this);
      return this;
    } finally {
      close();
    }
  }
  
  public boolean isClosed() {
    return closed;
  }
  
  @Override public void close() {
    if (!closed) {
      closed = true;
      IOUtils.closeQuietly(inputStream);
    }
  }   
  
  @Override public String toString() {
    return String.valueOf(result);
  }
}
