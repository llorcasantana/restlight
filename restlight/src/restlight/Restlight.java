package restlight;

import java.io.IOException;
import java.util.concurrent.Executor;

public class Restlight implements HttpStack {

// TODO: Varibles...
  private static Restlight instance;

  /** Procesara las peticiones a internet. */
  private HttpStack httpStack;
  
  /** Cola de peticiones al servidor. */
  private RequestQueue requestQueue;
 
// TODO: Constructor...

  private Restlight() {
  }
  
  public static Restlight getInstance() {
    if (instance == null) instance = new Restlight();
    return instance;
  }
  
  public static <V> Call<V> fromCall(Request.Parse<V> request) {
    return Restlight.getInstance().newCall(request);
  }

// TODO: Funciones...
  
  public HttpStack getStack() {
    if (httpStack == null) {
      httpStack = new HttpUrlStack();
    }
    return httpStack;
  }
  
  public void setStack(HttpStack stack) {
    httpStack = stack;
  }
  
  public Executor getExecutorDelivery() {
    return getQueue().executorDelivery();
  }
  
  public void setExecutorDelivery(Executor executor) {
    getQueue().setExecutorDelivery(executor);
  }
  
  public RequestQueue getQueue() {
    if (requestQueue == null) {
      requestQueue = new RequestQueue(this);
      requestQueue.start();
    }
    return requestQueue;
  }
  
  public void setQueue(RequestQueue queue, boolean quit) {
    if (requestQueue != null && quit) requestQueue.stop();
    requestQueue = queue;
  }
  
  /**
   * Envía de manera asíncrona la petición y notifica a tu aplicación con un
   * callback cuando una respuesta regresa. Ya que esta petición es asíncrona,
   * la ejecución se maneja en un hilo de fondo para que el hilo de la UI
   * principal no sea bloqueada o interfiera con esta.
   * @param request petición a realizar
   */
  public void enqueue(Request.Parse<?> request) {
    getQueue().add(request);
  }
  
  /**
   * Envíe sincrónicamente la solicitud y devuelva su respuesta.
   * @param <V> tipo de petición
   * @param request petición a realizar
   * @return una respuesta para el tipo de petición realizada
   * @throws java.lang.Exception si se produjo un problema al hablar con el
   * servidor
   */
  public ResponseBody execute(Request request) throws IOException {
    return getStack().execute(request);
  }
  
  public <V> V executeRequest(Request.Parse<V> request) throws Exception {
    return execute(request).result(request);
  }
  
  /**
   * Crea una invocación de un método que envía una solicitud a un servidor web 
   * y devuelve una respuesta.
   * @param <V> tipo de petición
   * @param request petición a realizar
   * @return una llamada
   */
  public <V> Call<V> newCall(final Request.Parse<V> request) {
    return new Call<V>() {
      @Override public void execute(Callback<V> callback) {
        request.setCallback(callback);
        enqueue(request);
      }
      @Override public V execute() throws Exception {
        return executeRequest(request);
      }
      @Override public Request.Parse<V> request() {
        return request;
      }
      @Override public void cancel() {
        request.cancel();
      }
      @Override public boolean isCancel() {
        return request.isCanceled();
      }
    };
  }
}