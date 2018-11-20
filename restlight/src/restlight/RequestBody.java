package restlight;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public interface RequestBody {

  String contentType(Charset charset) throws IOException;

  long contentLength(Charset charset) throws IOException;

  void writeTo(OutputStream out, Charset charset) throws IOException;
}
