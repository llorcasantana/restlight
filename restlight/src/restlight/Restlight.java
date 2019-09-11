package restlight;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Clase que controlara la cola de peticiones al servidor.
 *
 * @author Jesus Baez
 */
public class Restlight implements HttpStack {
  /** Numero de despachadores que atenderan las peticiones de la red. */
  static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 4;
  
// TODO: Varibles...
  private static Restlight instance;

  /** Cola de peticiones que se procesaran a la red. */
  private BlockingQueue<Request.Parse<?>> networkQueue;
  
  /** Procesara las peticiones a internet. */
  private final HttpStack httpStack;
  
  /** Hilo que atendera la cola. */
  private final Thread[] dispatchers;

  /** Puente que comunica las tareas con el hilo principal. */
  private Executor executorDelivery;
 
// TODO: Constructor...

  public Restlight(HttpStack stack, int threadPoolSize) {
    dispatchers = new Thread[threadPoolSize];
    executorDelivery = Platform.get();
    httpStack = stack;
  }
  
  private Restlight(HttpStack stack) {
    this(stack, DEFAULT_NETWORK_THREAD_POOL_SIZE);
  }
  
  public static Restlight get() {
    if (instance == null) {
      instance = new Restlight(new HttpUrlStack());
    }
    return instance;
  }

// TODO: Funciones...
    
  /**
   * @return La cola de despacho.
   */
  public BlockingQueue<Request.Parse<?>> networkQueue() {
    if (networkQueue == null) {
      networkQueue = new LinkedBlockingQueue<Request.Parse<?>>();
      start();
    }
    return networkQueue;
  }  
  
  public HttpStack stack() {
    return httpStack;
  }

  public Executor executorDelivery() {
    return executorDelivery;
  }
  
  public void setExecutorDelivery(Executor executor) {
    executorDelivery = executor;
  }
  
  /**
   * Inicia los hilos que atendera la cola de peticiones.
   */
  public void start() {
    stop();
    for (int i = 0; i < dispatchers.length; i++) {
      dispatchers[i] = new RequestDispatcher(this);
      dispatchers[i].start();
    }
  }

  /**
   * Obliga a detener todos los hilos.
   */
  public void stop() {
    for (int i = 0; i < dispatchers.length; i++) {
      if (dispatchers[i] != null) {
        dispatchers[i].interrupt();
        dispatchers[i] = null;
      }
    }
  }
 
  /**
   * Elimina una Peticion a la cola de despacho.
   *
   * @param request La peticion a remover
   * @return La peticion removida
   */
  public <T> Request.Parse<T> remove(Request.Parse<T> request) {
    networkQueue.remove(request);
    return request;
  }
  
  /**
   * Cancela todas las peticiones en esta cola.
   */
  public void cancelAll() {
    synchronized (networkQueue) {
      for (Request.Parse<?> request : networkQueue) {
        request.cancel();
      }
    }
  }
  
  /**
   * Cancela todas las peticiones de esta cola con la etiqueta dada.
   */
  public void cancelAll(final Object tag) {
    synchronized (networkQueue) {
      for (Request.Parse<?> request : networkQueue) {
        if (request.getTag() == tag) {
          request.cancel();
        }
      }
    }
  } 
  
  /**
   * Envía de manera asíncrona la petición y notifica a tu aplicación con un
   * callback cuando una respuesta regresa. Ya que esta petición es asíncrona,
   * la ejecución se maneja en un hilo de fondo para que el hilo de la UI
   * principal no sea bloqueada o interfiera con esta.
   * 
   * @param request petición a realizar
   */
  public <T> Request.Parse<T> enqueue(Request.Parse<T> request) {
    networkQueue().add(request);
    return request;
  }
  
  /**
   * Envíe sincrónicamente la solicitud y devuelva su respuesta.
   * 
   * @param request petición a realizar
   * 
   * @return una respuesta para el tipo de petición realizada
   * 
   * @throws java.lang.Exception si se produjo un problema al hablar con el
   * servidor
   */
  public ResponseBody execute(Request request) throws IOException {
    return stack().execute(request);
  }

  public <V> V execute(Request.Parse<V> parse) throws Exception {
    return execute((Request)parse).result(parse);
  }
  
  public <V> V execute(Request request, Request.Parse<V> parse) throws Exception {
    parse.setRequest(request);
    return execute(parse);
  }
  
  /**
   * Crea una invocación de un método que envía una solicitud a un servidor web 
   * y devuelve una respuesta.
   * 
   * @param request petición a realizar
   * 
   * @return una llamada
   */
  public <V> Call<V> newCall(final Request.Parse<V> request) {
    return new Call<V>() {
      @Override public void execute(Callback<V> callback) {
        request.setCallback(callback);
        enqueue(request);
      }
      @Override public V execute() throws Exception {
        return Restlight.this.execute(request);
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
  
  public <V> Call<V> newCall(Request request, Request.Parse<V> parse) {
    parse.setRequest(request);
    return newCall(parse);
  }
}