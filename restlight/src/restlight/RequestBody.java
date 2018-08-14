package restlight;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public abstract class RequestBody {

  public abstract String contentType(Charset charset) throws IOException;

  public abstract long contentLength(Charset charset) throws IOException;

  public abstract void writeTo(OutputStream out, Charset charset) throws IOException;
  
  public static RequestBody create(final String contentType, final String data) {
    return new RequestBody() {
      @Override public String contentType(Charset charset) throws IOException {
        return contentType + "; charset=" + charset.name();
      }
      @Override public long contentLength(Charset charset) throws IOException {
        return data.getBytes(charset).length;
      }
      @Override public void writeTo(OutputStream out, Charset charset) throws IOException {
        out.write(data.getBytes(charset));
      }
    };
  }
}
