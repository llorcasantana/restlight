package restlight;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public interface RequestBody {

  public String contentType(Charset charset) throws IOException;

  public long contentLength(Charset charset) throws IOException;

  public void writeTo(OutputStream out, Charset charset) throws IOException;
}
