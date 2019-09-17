package restlight;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import restlight.io.IOUtils;

public class ResponseBody implements Closeable {
  public int code;  
  public Headers headers;
  public String contentEncoding;
  public String contentType;
  public int contentLength;
  public boolean closed;
  public final InputStream in;
  
  public ResponseBody(InputStream in) {
    this.in = in;
  }
  
  public Reader charStream(Charset charset) throws IOException {
    return new InputStreamReader(in, charset);
  }
  
  public byte[] bytes() throws IOException {
    try {
      return IOUtils.toByteArray(in);
    } finally {
      close();
    }
  }
  
  public String string(Charset charset) throws IOException {
    byte[] data = bytes();
    return new String(data, charset);
  }
  
  public void writeTo(OutputStream out) throws IOException {
    try {
      IOUtils.copy(in, out);
    } finally {
      close();
    }
  }
  
  <V> V parse(Request.Parse<V> request) throws Exception {
    try { 
      return request.parseResponse(this);
    } finally {
      close();
    }
  }
  
  @Override public void close() {
    if (!closed) {
      closed = Boolean.TRUE;
      IOUtils.closeQuietly(in);
    }
  }   
}
