package restlight.body;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import org.simpleframework.xml.Serializer;
import restlight.RequestBody;
import restlight.io.IOUtils;

public class SimpleXmlBody<T> extends RequestBody {

  private final Serializer serializer;
  private final T src;

  public SimpleXmlBody(Serializer serializer, T src) {
    this.serializer = serializer;
    this.src = src;
  }

  public Serializer getSerializer() {
    return serializer;
  }

  public T getSrc() {
    return src;
  }
    
  @Override
  public String contentType(Charset charset) throws IOException {
    return String.format("application/xml; charset=%s", charset.name());
  }

  @Override
  public long contentLength(Charset charset) throws IOException {
    ByteArrayOutputStream baos = null;
    try {
      baos = IOUtils.arrayOutputStream();
      writeTo(baos, charset);
      return baos.size();
    } finally {
      IOUtils.closeQuietly(baos);
    }
  }

  @Override
  public void writeTo(OutputStream out, Charset charset) throws IOException {
    OutputStreamWriter osw = null;
    try {
      osw = new OutputStreamWriter(out, charset);
      serializer.write(src, osw);
      osw.flush();
    } catch(Exception e) {
      throw new IOException(e.getMessage(), e);
    } finally {
      IOUtils.closeQuietly(osw);
    } 
  }
  
}
