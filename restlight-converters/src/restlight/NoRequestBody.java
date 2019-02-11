package restlight;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

class NoRequestBody extends RequestBody {
  final Class<?> src;
  NoRequestBody(Object object) {
    src = object.getClass();
  }
  @Override public String contentType(Charset charset) throws IOException {
    throw new UnsupportedOperationException("adapter: '" + src.getName() + "' not supported yet.");
  }
  @Override public long contentLength(Charset charset) throws IOException {
    throw new UnsupportedOperationException("adapter: '" + src.getName() + "' not supported yet.");
  }
  @Override
  public void writeTo(OutputStream out, Charset charset) throws IOException {
    throw new UnsupportedOperationException("adapter: '" + src.getName() + "' not supported yet."); 
  }
}