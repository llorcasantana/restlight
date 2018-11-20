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

public class MultipartBody implements RequestBody {
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
    return addPart(new StringPart(name, newValue));
  }

  public MultipartBody addFile(String name, File value) {
    return addPart(new FilePart(name, value));
  }

  public List<Part<?>> parts() {
    return parts;
  }
  
  public static abstract class Part<T> {
    protected final String key;
    protected final T value;

    public Part(String key, T value) {
      this.key = key;
      this.value = value;
    }
  
    public String key() {
      return key;
    }
    
    public T value() {
      return value;
    }
  
    @Override public String toString() {
      return String.valueOf(value);
    }
   
   /**
    * Escribe los campos de encabezado multiparte; depende del estilo.
    */
    public abstract void writeHeaders(OutputStream out, Charset charset) throws IOException;
  
    public abstract long contentLength(Charset charset) throws IOException;

   /**
    * Inicia la escritura del part a internet en bits.
    *
    * @param out flujo de salida
    * @param charset codificacion
    *
    * @throws java.io.IOException
    */
    public abstract void writeTo(OutputStream out, Charset charset) throws IOException;
    }
  
  /*
   * ====================================================================
   * Clase modelo que contendra el nombre del input y el valor que se 
   * enviara por una petición a internet.
   * ====================================================================
   */
  public static class StringPart extends Part<String> {

    public StringPart(String key, String value) {
      super(key, (value == null) ? "" : value);
    }  

    @Override
    public void writeHeaders(OutputStream out, Charset charset) throws IOException {
      out.write(Headers.CONTENT_DISPOSITION.getBytes(charset));
      out.write(COLON_SPACE);
      out.write(String.format("form-data; name=\"%s\"", key).getBytes(charset));
      out.write(CR_LF);

      out.write(Headers.CONTENT_TYPE.getBytes(charset));
      out.write(COLON_SPACE);
      out.write(String.format("text/plain; charset=%s", charset.name()).getBytes(charset));
      out.write(CR_LF);

      out.write(Headers.CONTENT_TRANSFER_ENCODING.getBytes(charset));
      out.write(COLON_SPACE);
      out.write("8bit".getBytes(charset));
      out.write(CR_LF);
    }

    @Override public long contentLength(Charset charset) throws IOException {
      return value.getBytes(charset).length;
    }
    
    @Override
    public void writeTo(OutputStream out, Charset charset) throws IOException {
      out.write(value.getBytes(charset));
    }
  }
  
  /*
   * ====================================================================
   * Clase modelo que contendra el nombre del input y el archivo que se 
   * enviara por una petición a internet.
   * ====================================================================
   */
  public static class FilePart extends Part<File> {
  
    public FilePart(String key, File value) {
      super(key, value);
    }

    @Override
    public void writeHeaders(OutputStream out, Charset charset) throws IOException {
      out.write(Headers.CONTENT_DISPOSITION.getBytes(charset));
      out.write(COLON_SPACE);
      out.write(String.format("form-data; name=\"%s\"; filename=\"%s\"", key, value.getName()).getBytes(charset));
      out.write(CR_LF);

      out.write(Headers.CONTENT_TYPE.getBytes(charset));
      out.write(COLON_SPACE);
      out.write("application/octet-stream".getBytes(charset));
      //URLConnection.guessContentTypeFromName(file.getName())
      out.write(CR_LF);

      out.write(Headers.CONTENT_TRANSFER_ENCODING.getBytes(charset));
      out.write(COLON_SPACE);
      out.write("binary".getBytes(charset));
      out.write(CR_LF);
    }

    @Override public long contentLength(Charset charset) throws IOException {
      return value.length();
    }

    @Override
    public void writeTo(OutputStream out, Charset charset) throws IOException {
      IOUtils.copy(value, out);
    }
  }
}