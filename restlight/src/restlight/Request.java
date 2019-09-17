package restlight;

import java.nio.charset.Charset;

public class Request {
  /** Codificación predeterminada. */
  public static final Charset DEFAULT_ENCODING = Charset.forName("utf-8");

  /** Tiempo limite de espera por default. */
  public static final int DEFAULT_TIMEOUT = 2500 * 2 * 2;
  
  /** Metodo por default. */
  public static final String DEFAULT_METHOD = "GET";
  
  /** Url de nuestra request. */
  HttpUrl url;
  
  /** Metodo de la request: OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE. */
  String method = DEFAULT_METHOD;
  
  /** Lista de encabezados adicionales de HTTP para esta peticion. */
  Headers headers;
 
  /** Parametros de nuestra request. */
  RequestBody body;
  
  /** Etiqueta para identificar la request. */ 
  Object tag = Request.class;
  
  /** Tiempo limite de espera. */
  int timeoutMs = DEFAULT_TIMEOUT;
  
  /** Codificacion. */
  Charset charset = DEFAULT_ENCODING;
  
  /** Valida si la request fue cancelada. */
  boolean isCanceled;
  
  /**
   * @return true si se cancelo la peticion.
   */
  public boolean isCanceled() {
    synchronized (this) {
      return isCanceled;
    }
  }

  /**
   * Cancela la peticion.
   */
  public void cancel() {
    synchronized (this) {
      isCanceled = true;
    }
  }

  public HttpUrl getUrl() {
    return url;
  }
  public void setUrl(String url) {
    setUrl(new HttpUrl(url));
  }
  public void setUrl(HttpUrl url) {
    this.url = url;
  }
   
  public String getMethod() {
    return method;
  }
  public void setMethod(String method) {
    this.method = method;
  }
  
  public Headers getHeaders() {
    return headers;
  }
  public void setHeaders(Headers headers) {
    this.headers = headers;
  }
  public void addHeader(String name, String value) {
    if (headers == null) headers = new Headers();
    headers.add(name, value);
  }

  public RequestBody getBody() {
    return body;
  }
  public void setBody(RequestBody body) {
    this.body = body;
  }
  
  public int getTimeoutMs() {
    return timeoutMs;
  }
  public void setTimeoutMs(int timeoutMs) {
    this.timeoutMs = timeoutMs;
  }

  public Charset getCharset() {
    return charset;
  }
  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  
  public Object getTag() {
    return tag;
  }
  public void setTag(Object tag) {
    this.tag = tag;
  }

  
  public static boolean requiresRequestBody(String method) {
    return method.equals("POST") || method.equals("PUT");
  }

  @Override public String toString() {
    return "Request{" + "url=" + url + ", method=" + method + ", headers=" 
            + headers + ", tag=" + tag + ", timeoutMs=" + timeoutMs 
            + ", charset=" + charset + ", isCanceled=" + isCanceled + '}';
  }
  

  public static abstract class Parse<T> extends Request implements Callback<T> {
    /** Intefaz que escuchara la respuesta. */
    private Callback<T> callback;
    
    /**
     * Convercion de la respuesta obtenida de la Red.
     *
     * @param response resultado obtenido.
     *
     * @return tipo generico
     *
     * @throws java.lang.Exception
     */
    public abstract T parseResponse(ResponseBody response) throws Exception;

    public Callback<T> getCallback() {
      return callback;
    }
    public void setCallback(Callback<T> callback) {
      this.callback = callback;
    }
    
    /**
     * Livera la respuesta por la Interfaz.
     *
     * @param result resultado obtenido
     *
     * @throws java.lang.Exception
     */
    @Override public void onResponse(T result) throws Exception {
      if (callback != null)  callback.onResponse(result);
    }

    /**
     * Livera el error por la Interfaz.
     *
     * @param error ocurrido
     */
    @Override public void onFailure(Exception error) {
      if (callback != null) callback.onFailure(error);
    }
    
    /** Cancela la peticion. */
    @Override public void cancel() {
      synchronized (this) {
        isCanceled = true;
        callback = null;
      }
    }
    
    public void setRequest(Request r) {
      url = r.url;
      method = r.method;
      headers = r.headers;
      body = r.body;
      timeoutMs = r.timeoutMs;
      charset = r.charset;
      tag = r.tag;
      isCanceled = r.isCanceled;
    }
  }
}