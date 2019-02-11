package restlight;

import java.util.concurrent.Executor;

public class Restlight {

// TODO: Varibles...
  private static Restlight instance;

  /** Cola de peticiones al servidor. */
  private RequestQueue requestQueue;
 
// TODO: Constructor...

  private Restlight() {
  }
  
  public static Restlight getInstance() {
    if (instance == null) instance = new Restlight();
    return instance;
  }
  
  public static <V> Call<V> fromCall(Request<V> request) {
    return Restlight.getInstance().newCall(request);
  }

// TODO: Funciones...
  
  public HttpStack getStack() {
    return getQueue().stack();
  }
  
  public void setStack(HttpStack stack) {
    getQueue().setStack(stack);
  }
  
  public Executor getExecutorDelivery() {
    return getQueue().executorDelivery();
  }
  
  public void setExecutorDelivery(Executor executor) {
    getQueue().setExecutorDelivery(executor);
  }
  
  public RequestQueue getQueue() {
    if (requestQueue == null) {
      requestQueue = new RequestQueue();
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
  public void queue(Request<?> request) {
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
  public <V> V execute(Request<V> request) throws Exception {
    return getStack().execute(request).result(request);
  }
  
  /**
   * Crea una invocación de un método que envía una solicitud a un servidor web 
   * y devuelve una respuesta.
   * @param <V> tipo de petición
   * @param request petición a realizar
   * @return una llamada
   */
  public <V> Call<V> newCall(final Request<V> request) {
    return new Call<V>() {
      @Override public void execute(Callback<V> callback) {
        request.setCallback(callback);
        Restlight.this.queue(request);
      }
      @Override public Request<V> request() {
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