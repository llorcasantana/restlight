/**
 * ====================================================================
 * Codificacion {@code multipart/form-data}.
 * Contenido mixto POST (datos binarios y caracter) RFC2388.
 * ====================================================================
 */
package restlight;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import restlight.io.IOUtils;

public class MultipartBody extends RequestBody {
  /** Espacio colon */
  public static final byte[] COLON_SPACE = {':', ' '};
  
  /** Linea separadora. */
  public static final byte[] CR_LF = {'\r', '\n'};
  
  /** Dos Guiones. */
  public static final byte[] TWO_DASHES = {'-', '-'};

  /** Lista de datos. */
  private final List<Part<?>> parts;
  
  /** Variable requerida para : multipart/form-data. */
  private final String boundary;
  
  
  public MultipartBody() {
    this(Long.toOctalString(System.currentTimeMillis()));
  }

  public MultipartBody(String boundary) {
    this.parts = new ArrayList<Part<?>>();
    this.boundary = boundary;
  }
  
  public String boundary() {
    return boundary;
  }
  
  @Override public String contentType(Charset charset) {
    return "multipart/form-data; boundary=" + boundary;
  }
  
  @Override public long contentLength(Charset charset) throws IOException {
    long len = 0;
    for (int i = 0; i < parts.size(); i++) {
      Part<?> part = parts.get(i);
      len += part.contentLength(charset);
    }
    ByteArrayOutputStream baos = null;
    try {
      baos = IOUtils.arrayOutputStream();
      doWrite(baos, charset, Boolean.FALSE);
      return baos.size() + len;
    } finally {
      IOUtils.closeQuietly(baos);
    }
  }

  @Override public void writeTo(OutputStream out, Charset charset) throws IOException {
    doWrite(out, charset, Boolean.TRUE);
  }
  
  private void doWrite(OutputStream out, Charset charset, boolean write) throws IOException {
    byte[] boundaryToCharArray = boundary.getBytes();

    for (Part<?> part : parts) {
      out.write(TWO_DASHES);
      out.write(boundaryToCharArray);
      out.write(CR_LF);

      // Write Format Multipart Header:
      part.writeHeaders(out, charset);

      // Write Body:
      out.write(CR_LF);
      if (write) part.writeTo(out, charset);
      out.write(CR_LF);
    }

    // End of multipart/form-data.
    out.write(TWO_DASHES);
    out.write(boundaryToCharArray);
    out.write(TWO_DASHES);
    out.write(CR_LF);
  }

  public MultipartBody addPart(Part<?> bodyPart) {
    parts.add(bodyPart);
    return this;
  }

  public MultipartBody addParam(String name, Object value) {
    String newValue = value == null ? "" : value.toString();
    return addPart(new Part.StringPart(name, newValue));
  }

  public MultipartBody addFile(String name, File value) {
    return addPart(new Part.FilePart(name, value));
  }

  public List<Part<?>> parts() {
    return parts;
  }
}