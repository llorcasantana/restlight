package restlight;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import restlight.io.IOUtils;

public class BasicHttpStack implements HttpStack {   
  /**
   * Ejecuta una petición.
   *
   * @param request petición a ejecutar
   *
   * @return el resultado de la petición realizada
   *
   * @throws java.lang.Exception
   */
  @Override
  public <T> Response.Network<T> execute(Request<T> request) throws IOException {
    HttpURLConnection conn = open(request);
    writeHeaders(conn, request);
    setParametersForRequest(conn, request);

    int responseCode = conn.getResponseCode();
    if (responseCode == -1) {
      // -1 is returned by getResponseCode() if the response code could not be retrieved.
      // Signal to the caller that something was wrong with the connection.
      throw new IOException("Could not retrieve response code from HttpUrlConnection.");
    }

    Response.Network<T> response = new Response.Network<T>(responseCode);
    response.headers = Headers.of(conn.getHeaderFields());
    response.contentLength = conn.getContentLength();
    response.contentEncoding = conn.getContentEncoding();
    response.contentType = conn.getContentType();
    response.source = conn.getInputStream();
    response.request = request;
    return response;
  }
  
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
  public HttpURLConnection open(Request<?> request) throws IOException {  
    HttpURLConnection conn = open(new URL(request.getUrl()));
    conn.setConnectTimeout(request.getTimeoutMs());
    conn.setReadTimeout(request.getTimeoutMs());
    conn.setUseCaches(Boolean.FALSE);
    conn.setDoInput(Boolean.TRUE);
    return conn;
  }

  /**
   * Manda una lista de encabezados adicionales de HTTP para esta petición.
   *
   * @param connection HTTP
   * @param request peticion
   *
   * @throws IOException
   */
  public void writeHeaders(HttpURLConnection connection,
          Request<?> request) throws IOException {
    Headers headers = request.getHeaders();
    if (headers != null) {
      int len = headers.size();
      for (int i = 0; i < len; i++) {
        connection.addRequestProperty(headers.key(i), headers.value(i));
      }
    }
  }
  
  /**
   * Lista de encabezados adicionales de HTTP para esta petición.
   *
   * @param connection HTTP
   * @param request peticion
   *
   * @throws IOException
   */
  public void setParametersForRequest(HttpURLConnection connection,
          Request<?> request) throws IOException {
    String method = request.getMethod();
    connection.setRequestMethod(method);

    if (method.equals("POST") || method.equals("PUT")) {
      // Write request to server.
      writeTo(connection, request);
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
  public void writeTo(HttpURLConnection conn, Request<?> request) throws IOException {
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
}