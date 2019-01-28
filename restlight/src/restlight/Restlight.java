package restlight;

import java.util.concurrent.Executor;

public class Restlight {

// TODO: Varibles...
  private static Restlight instance;
 
  /** Procesara las peticiones a internet. */
  private HttpStack httpStack;
  
  /** Puente que comunica las tareas con el hilo principal. */
  private Executor executorDelivery;
  
  /** Cola de peticiones al servidor. */
  private RequestQueue requestQueue;
 
// TODO: Constructor...

  private Restlight() {
    httpStack = new BasicHttpStack();
    executorDelivery = Platform.get();
  }
  
  public static Restlight getInstance() {
    if (instance == null) instance = new Restlight();
    return instance;
  }

// TODO: Funciones...
  
  public HttpStack getStack() {
    return httpStack;
  }
  
  public void setStack(HttpStack stack) {
    httpStack = stack;
  }
  
  public Executor getExecutorDelivery() {
    return executorDelivery;
  }
  
  public void setExecutorDelivery(Executor executor) {
    executorDelivery = executor;
  }
  
  public synchronized RequestQueue getQueue() {
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
  public void queue(Request<?> request) {
    getQueue().add(request);
  }
  
  /**
   * Envíe sincrónicamente la solicitud y devuelva su respuesta.
   * @param <T> tipo de petición
   * @param request petición a realizar
   * @return una respuesta para el tipo de petición realizada
   * @throws java.lang.Exception si se produjo un problema al hablar con el
   * servidor
   */
  public <T> Response<T> execute(Request<T> request) throws Exception {
    return getStack().execute(request).parseRequest(request);
  }
  
  /**
   * Crea una invocación de un método que envía una solicitud a un servidor web 
   * y devuelve una respuesta.
   * @param <T> tipo de petición
   * @param request petición a realizar
   * @return una llamada
   */
  public <T> Call<T> newCall(final Request<T> request) {
    return new Call<T>() {
      @Override public void execute(Callback<T> callback) {
        request.setCallback(callback);
        Restlight.this.queue(request);
      }
      @Override public Request<T> request() {
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