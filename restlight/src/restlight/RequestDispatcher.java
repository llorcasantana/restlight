package restlight;

import restlight.io.IOUtils;

/*import android.os.Process;*/

public class RequestDispatcher extends Thread {

  /** Cola de peticiones al servidor. */
  private final RequestQueue mQueue;
  
  /** Es usado para decir que el hilo a muerto. */
  private volatile boolean mQuit = false;

  /**
   * @param queue cola de peticiones.
   */
  public RequestDispatcher(RequestQueue queue) {
    mQueue = queue;
    setPriority(MIN_PRIORITY);
  }

  /**
   * Obliga al hilo a detenerce inmediatamente.
   */
  @Override public void interrupt() {
    mQuit = true;
    super.interrupt();
  }

  /**
   * Metodo que desarrolla un bucle que estara observando si existe una o varias
   * 'Request' en la cola, si hay una request la procesara por medio del objeto
   * 'NetworkConnection'.
   */
  @Override public void run() {
    /*Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);*/
    while (true) {
      Request<?> request;
      try {
        // Toma y quita la peticion de la cola.
        request = mQueue.mNetworkQueue.take();
      } catch (InterruptedException e) {
        // El hilo pudo haber sido interrumpido.
        if (mQuit) return;
        continue;
      }

      try {
        // Si la petición ya estaba cancelada, no funciona la petición de la red.
        if (request.isCanceled()) continue;

        // Procesa la request.
        Response.Network response = null;
        try {
          response = mQueue.mNetwork.execute(request);
          // Si la petición ya estaba cancelada, no funciona la petición de la red.
          if (request.isCanceled()) continue;
         
          deliveryResponse(request, response.response());
        } finally {
          IOUtils.closeQuietly(response);
        }
      } catch (Exception e) {
        // TODO: handle exception
        deliveryErrorResponse(request, e);
      }
    }
  }

  /**
   * Metodo que se encarga de liverar la respuesta obtenida de la conexión.
   */
  @SuppressWarnings("rawtypes")
  private void deliveryResponse(final Callback callback, final Response response) {
    mQueue.mExecutor.execute(new Runnable() {  
      @SuppressWarnings("unchecked")
      @Override
      public void run() {
        try {
          callback.onResponse(response);
        } catch (Exception error) {
          callback.onErrorResponse(error);
        }
      }
    });
  }

  /**
   * Metodo que se encarga de liverar el error obtenido de la conexión.
   */
  private void deliveryErrorResponse(final Callback<?> callback, final Exception error) {
    mQueue.mExecutor.execute(new Runnable() {
      @Override public void run() {
        callback.onErrorResponse(error);
      }
    });
  }
 
}