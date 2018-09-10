package restlight;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public interface RequestBody {

  /**
   * Devuelve el encabezado Content-Type para este cuerpo.
   * @param charset codificación para este cuerpo.
   */
  public String contentType(Charset charset) throws IOException;

  /**
   * Devuelve la cantidad de bytes que se escribirán para hundirse en una 
   * llamada a {@link #writeTo(java.io.OutputStream, java.nio.charset.Charset) }
   * @param charset codificación para este cuerpo.
   */
  public long contentLength(Charset charset) throws IOException;

  /**
   * Escribe el contenido de esta solicitud para hundirse.
   * @param out flujo de streams
   * @param chrst codificación para este cuerpo.
   */
  public void writeTo(OutputStream out, Charset charset) throws IOException;
}
