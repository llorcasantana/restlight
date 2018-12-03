package restlight;

import java.nio.charset.Charset;

public abstract class Request<T> implements Callback<T> {
  /** Codificación predeterminada. */
  public static final Charset DEFAULT_ENCODING = Charset.forName("utf-8");
   
  /** Tiempo limite de espera por default. */
  public static final int DEFAULT_TIMEOUT = 2500 * 2 * 2;
  
  /** Url de nuestra request. */
  private String url;
  
  /** Metodo de la request: OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE. */
  private String method = "GET";
  
  /** Lista de encabezados adicionales de HTTP para esta peticion. */
  private Headers headers;
 
  /** Parametros de nuestra request. */
  private RequestBody body;
  
  /** Tiempo limite de espera. */
  private int timeoutMs = DEFAULT_TIMEOUT;
  
  /** Codificacion. */
  private Charset charset = DEFAULT_ENCODING;
    
  /** Intefaz que escuchara la respuesta.*/
  private Callback<T> callback;
   
  private Object tag = Request.class;
  
  /** Valida si la request fue cancelada. */
  private boolean isCanceled;

  /**
   * Convercion de la respuesta obtenida de la Red.
   *
   * @param response resultado obtenido.
   *
   * @return tipo generico
   *
   * @throws java.lang.Exception
   */
  public abstract T parseResponse(Response<T> response) throws Exception;

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
      callback = null;
    }
  }

  /**
   * Livera la respuesta por la Interfaz.
   *
   * @param response resultado obtenido
   *
   * @throws java.lang.Exception
   */
  @Override public void onResponse(Response<T> response) throws Exception {
    if (callback != null) callback.onResponse(response);
  }

  /**
   * Livera el error por la Interfaz.
   *
   * @param error ocurrido
   */
  @Override public void onErrorResponse(Exception error) {
    if (callback != null) callback.onErrorResponse(error);
  }

  public String getUrl() {
    return url;
  }
  public Request<T> setUrl(String url) {
    this.url = url;
    return this;
  }
  public Request<T> setUrl(HttpUrl url) {
    return setUrl(url.toString(getCharset()));
  }
   
  public String getMethod() {
    return method;
  }
  public Request<T> setMethod(String method) {
    this.method = method;
    return this;
  }
  
  public Headers getHeaders() {
    return headers;
  }
  public Request<T> setHeaders(Headers headers) {
    this.headers = headers;
    return this;
  }
  public Request<T> header(String key, String value) {
    if (headers == null) headers = new Headers();
    headers.add(key, value);
    return this;
  }

  public RequestBody getBody() {
    return body;
  }
  public Request<T> setBody(RequestBody body) {
    this.body = body;
    return this;
  }
  
  public int getTimeoutMs() {
    return timeoutMs;
  }
  public Request<T> setTimeoutMs(int timeoutMs) {
    this.timeoutMs = timeoutMs;
    return this;
  }

  public Charset getCharset() {
    return charset;
  }
  public Request<T> setCharset(Charset charset) {
    this.charset = charset;
    return this;
  }

  public Callback<T> getCallback() {
    return callback;
  }
  public Request<T> setCallback(Callback<T> callback) {
    this.callback = callback;
    return this;
  }
  
  public Object getTag() {
    return tag;
  }
  public Request<T> setTag(Object tag) {
    this.tag = tag;
    return this;
  }
}