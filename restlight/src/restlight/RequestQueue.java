package restlight;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Clase que controlara la cola de peticiones al servidor.
 *
 * @author Jesus Baez
 */
public class RequestQueue {
  
  /** Numero de despachadores que atenderan las peticiones de la red. */
  static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 4;
  
  /** Cola de peticiones que se procesaran a la red. */
  private final BlockingQueue<Request.Parse<?>> networkQueue;
  
  /** Procesara las peticiones a internet. */
  private final HttpStack httpStack;
  
  /** Hilo que atendera la cola. */
  private final Thread[] dispatchers;

  /** Puente que comunica las tareas con el hilo principal. */
  private Executor executorDelivery;

  public RequestQueue(HttpStack stack, int threadPoolSize) {
    networkQueue = new LinkedBlockingQueue<Request.Parse<?>>();
    dispatchers = new Thread[threadPoolSize];
    executorDelivery = Platform.get();
    httpStack = stack;
  }
  
  public RequestQueue(HttpStack stack) {
    this(stack, DEFAULT_NETWORK_THREAD_POOL_SIZE);
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
   * Agrega una Peticion a la cola de despacho.
   *
   * @param request La peticion al servicio
   * @return La peticion al servicio
   */
  public <T> Request.Parse<T> add(Request.Parse<T> request) {
    networkQueue.add(request);
    return request;
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
   * @return La cola de despacho.
   */
  public BlockingQueue<Request.Parse<?>> networkQueue() {
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
}
