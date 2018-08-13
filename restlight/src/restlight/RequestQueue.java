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
  final BlockingQueue<Request<?>> mNetworkQueue;
  
  /** Objeto que procesa las peticiones a internet. */
  final HttpStack mNetwork;
  
  /** Puente que comunica las tareas con el hilo principal. */
  final Executor mExecutor;
  
  /** Hilo que atendera la cola. */
  final Thread[] mDispatchers;

  /**
   * @param network
   * @param executor
   */
  public RequestQueue(HttpStack network, Executor executor) {
    this(network, executor, DEFAULT_NETWORK_THREAD_POOL_SIZE);
  }

  /**
   * @param network
   * @param executor
   * @param threadPoolSize
   */
  public RequestQueue(HttpStack network, Executor executor, int threadPoolSize) {
    mNetworkQueue = new LinkedBlockingQueue<Request<?>>();
    mNetwork = network;
    mExecutor = executor;
    mDispatchers = new Thread[threadPoolSize];
  }

  /**
   * Inicia los hilos que atendera la cola de peticiones.
   */
  public void start() {
    stop();
    for (int i = 0; i < mDispatchers.length; i++) {
      mDispatchers[i] = new RequestDispatcher(this);
      mDispatchers[i].start();
    }
  }

  /**
   * Obliga a detener todos los hilos.
   */
  public void stop() {
    for (int i = 0; i < mDispatchers.length; i++) {
      if (mDispatchers[i] != null) {
        mDispatchers[i].interrupt();
        mDispatchers[i] = null;
      }
    }
  }
  
  /**
   * Agrega una Peticion a la cola de despacho.
   *
   * @param request La peticion al servicio
   * @return La peticion al servicio
   */
  public <T> Request<T> add(Request<T> request) {
    mNetworkQueue.add(request);
    return request;
  }
  
  /**
   * Elimina una Peticion a la cola de despacho.
   *
   * @param request La peticion a remover
   * @return La peticion removida
   */
  public <T> Request<T> remove(Request<T> request) {
    mNetworkQueue.remove(request);
    return request;
  }
  
  /**
   * Cancela todas las peticiones en esta cola.
   */
  public void cancelAll() {
    synchronized (mNetworkQueue) {
      for (Request<?> request : mNetworkQueue) {
        request.cancel();
      }
    }
  }
  
  /**
   * Cancela todas las peticiones de esta cola con la etiqueta dada.
   */
  public void cancelAll(final Object tag) {
    synchronized (mNetworkQueue) {
      for (Request<?> request : mNetworkQueue) {
        if (request.getTag() == tag) {
          request.cancel();
        }
      }
    }
  }

  /**
   * @return La cola de despacho.
   */
  public BlockingQueue<Request<?>> queue() {
    return mNetworkQueue;
  }  
}
