package restlight;

/*import android.os.Process;*/

public class RequestDispatcher extends Thread {

  /** Cola de peticiones al servidor. */
  private final RequestQueue queue;
  
  /** Es usado para decir que el hilo a muerto. */
  private volatile boolean quit = false;

  /**
   * @param queue cola de peticiones.
   */
  public RequestDispatcher(RequestQueue queue) {
    this.queue = queue;
    setPriority(MIN_PRIORITY);
  }

  /**
   * Obliga al hilo a detenerce inmediatamente.
   */
  @Override public void interrupt() {
    quit = true;
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
      Request.Parse<?> request;
      try {
        // Toma y quita la peticion de la cola.
        request = queue.networkQueue().take();
      } catch (InterruptedException e) {
        // El hilo pudo haber sido interrumpido.
        if (quit) return;
        continue;
      }

      try {
        // Si la petición ya estaba cancelada, no funciona la petición de la red.
        if (request.isCanceled()) continue;

        // Procesa la request.
        ResponseBody responseBody = queue.stack().execute(request);
        
        // Si la petición ya estaba cancelada, no funciona la petición de la red.
        if (request.isCanceled()) {
          responseBody.close();
          continue;
        }
         
        this.onResponse(request, responseBody.result(request));
        
      } catch (Exception e) {
        // TODO: handle exception
        this.onFailure(request, e);
      }
    }
  }
  
  /**
   * Metodo que se encarga de liverar la respuesta obtenida de la conexión.
   */
  @SuppressWarnings("rawtypes")
  public void onResponse(final Callback callback, final Object response) {
    queue.executorDelivery().execute(new Runnable() {  
      @SuppressWarnings("unchecked")
      @Override
      public void run() {
        try {
          callback.onResponse(response);
        } catch (Exception error) {
          callback.onFailure(error);
        }
      }
    });
  }

  /**
   * Metodo que se encarga de liverar el error obtenido de la conexión.
   */
  public void onFailure(final Callback<?> callback, final Exception error) {
    queue.executorDelivery().execute(new Runnable() {
      @Override public void run() {
        callback.onFailure(error);
      }
    });
  }

}