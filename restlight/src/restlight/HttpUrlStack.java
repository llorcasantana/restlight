package restlight;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import restlight.io.IOUtils;

public class HttpUrlStack implements HttpStack { 
  
  /**
   * Abre una conexión HTTP a intenert apartir de una petición.
   *
   * @param src conección.
   *
   * @return una conexión HTTP abierta.
   *
   * @throws java.io.IOException
   */
  public HttpURLConnection open(URL src) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) src.openConnection();
    // Workaround for the M release HttpURLConnection not observing the
    // HttpURLConnection.setFollowRedirects() property.
    // https://code.google.com/p/android/issues/detail?id=194495
    conn.setInstanceFollowRedirects(HttpURLConnection.getFollowRedirects());
    return conn;
  }

  /**
   * Abre una conexión HTTP a intenert apartir de una petición.
   *
   * @param request petición a intenert.
   *
   * @return una conexión HTTP abierta.
   *
   * @throws java.io.IOException
   */
  public HttpURLConnection open(Request request) throws IOException {  
    URL src = request.getUrl().toUrl(request.getCharset());
    HttpURLConnection conn = open(src);
    conn.setConnectTimeout(request.getTimeoutMs());
    conn.setReadTimeout(request.getTimeoutMs());
    conn.setUseCaches(Boolean.FALSE);
    conn.setDoInput(Boolean.TRUE);
    return conn;
  }

  /**
   * Manda una lista de encabezados adicionales de HTTP para esta petición.
   *
   * @param conn HTTP
   * @param request peticion
   *
   * @throws IOException
   */
  public void writeHeaders(HttpURLConnection conn, Request request)
  throws IOException {
    Headers headers = request.getHeaders();
    if (headers != null) {
      for (int i = 0, size = headers.size(); i < size; i++) {
        conn.addRequestProperty(headers.name(i), headers.value(i));
      }
    }
  }
  
  /**
   * Escribe el cuerpo de de esta petición.
   *
   * @param conn HTTP
   * @param request peticion
   *
   * @throws IOException
   */
  public void writeBody(HttpURLConnection conn, Request request) 
  throws IOException {
    String method = request.getMethod();
    conn.setRequestMethod(method);

    if (Request.requiresRequestBody(method)) {
      // Write request to server.
      writeTo(conn, request);
    }
  }

  /**
   * Metodo que se encargar de eviar los datos a internet por medio de una
   * escritura de streams.
   *
   * @param conn conexión abierta a internet
   * @param request peticion
   *
   * @throws java.io.IOException
   */
  public void writeTo(HttpURLConnection conn, Request request) throws IOException {
    RequestBody requestBody = request.getBody();
    if (requestBody != null) {
      // Setup connection:
      conn.setDoOutput(Boolean.TRUE);
      conn.addRequestProperty(Headers.CONTENT_TYPE,
              requestBody.contentType(request.getCharset()));

      // Length:
      long contentLength = requestBody.contentLength(request.getCharset());
      conn.setFixedLengthStreamingMode((int) contentLength);

      // Write params:
      BufferedOutputStream bos = null;
      try {
        bos = new BufferedOutputStream(conn.getOutputStream());
        requestBody.writeTo(bos, request.getCharset());
      } finally {
        IOUtils.closeQuietly(bos);
      }
    }
  }
  
  public ResponseBody getResponse(HttpURLConnection conn, Request request) 
  throws IOException {
    int responseCode = conn.getResponseCode();
    if (responseCode == -1) {
      // -1 is returned by getResponseCode() if the response code could not be retrieved.
      // Signal to the caller that something was wrong with the connection.
      throw new IOException("Could not retrieve response code from HttpUrlConnection.");
    }
    
    ResponseBody response = body(conn);
    response.code = responseCode;
    response.headers = Headers.of(conn.getHeaderFields());
    response.contentLength = conn.getContentLength();
    response.contentEncoding = conn.getContentEncoding();
    response.contentType = conn.getContentType();
    return response;
  }
  
  static ResponseBody body(final HttpURLConnection hurlc) {
    InputStream inputStream;
    try {
      inputStream = hurlc.getInputStream();
    } catch(IOException e) {
      inputStream = hurlc.getErrorStream();
    }
    return new ResponseBody(inputStream) {
      @Override public void close() {
        super.close();
        hurlc.disconnect();
      }
    };
  }
  
  /**
   * Ejecuta una petición.
   *
   * @param request petición a ejecutar
   *
   * @return el resultado de la petición realizada
   *
   * @throws java.lang.Exception
   */
  @Override public ResponseBody execute(Request request) throws IOException {
    HttpURLConnection conn = null;
    try {
      conn = open(request);
      if (request.isCanceled()) throw new IOException("request is cancel");
      
      writeHeaders(conn, request);
      writeBody(conn, request);
      return getResponse(conn, request);
     
    } catch (IOException e) {
      if (conn != null) conn.disconnect();
      throw e;
    }
  }
  
}