package restlight;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import restlight.io.IOUtils;

public abstract class Part<T> {
  
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
      out.write(MultipartBody.COLON_SPACE);
      out.write(String.format("form-data; name=\"%s\"", key).getBytes(charset));
      out.write(MultipartBody.CR_LF);

      out.write(Headers.CONTENT_TYPE.getBytes(charset));
      out.write(MultipartBody.COLON_SPACE);
      out.write(String.format("text/plain; charset=%s", charset.name()).getBytes(charset));
      out.write(MultipartBody.CR_LF);

      out.write(Headers.CONTENT_TRANSFER_ENCODING.getBytes(charset));
      out.write(MultipartBody.COLON_SPACE);
      out.write("8bit".getBytes(charset));
      out.write(MultipartBody.CR_LF);
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
      out.write(MultipartBody.COLON_SPACE);
      out.write(String.format("form-data; name=\"%s\"; filename=\"%s\"", key, value.getName()).getBytes(charset));
      out.write(MultipartBody.CR_LF);

      out.write(Headers.CONTENT_TYPE.getBytes(charset));
      out.write(MultipartBody.COLON_SPACE);
      out.write("application/octet-stream".getBytes(charset));
      //URLConnection.guessContentTypeFromName(file.getName())
      out.write(MultipartBody.CR_LF);

      out.write(Headers.CONTENT_TRANSFER_ENCODING.getBytes(charset));
      out.write(MultipartBody.COLON_SPACE);
      out.write("binary".getBytes(charset));
      out.write(MultipartBody.CR_LF);
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