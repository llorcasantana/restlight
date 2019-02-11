package restlight;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import restlight.io.IOUtils;

public class ResponseBody implements Closeable {
  public int code;  
  public Headers headers;
  public String contentEncoding;
  public String contentType;
  public int contentLength;
  public InputStream inputStream;
  public boolean closed;
  
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
  
  <V> V result(Request<V> request) throws Exception {
    try { 
      return request.parseResponse(this);
    } finally {
      close();
    }
  }
  
  @Override public void close() {
    if (!closed) {
      closed = Boolean.TRUE;
      IOUtils.closeQuietly(inputStream);
    }
  }   
}
