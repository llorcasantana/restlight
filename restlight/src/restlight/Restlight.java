package restlight;

import java.util.concurrent.Executor;
import restlight.io.IOUtils;

public class Restlight {

// TODO: Varibles...
  
  /** Procesara las peticiones a internet. */
  private HttpStack mStack;
  
  /** Puente que comunica las tareas con el hilo principal. */
  private Executor mExecutor;
  
  /** Cola de peticiones al servidor. */
  private RequestQueue mQueue;
  
// TODO: Constructor...
  
  private static Restlight instance;
  
  public Restlight() {
  }
  
  public static Restlight getInstance() {
    if (instance == null) instance = new Restlight();
    return instance;
  }

// TODO: Funciones...
  
  public HttpStack getHttpStack() {
    if (mStack == null) mStack = BasicHttpStack.getInstance();
    return mStack;
  }

  public void setHttpStack(HttpStack httpStack) {
    mStack = httpStack;
    if (mQueue != null) {
      if (mStack != mQueue.mNetwork) {
        mQueue = new RequestQueue(mStack, mQueue.mExecutor, mQueue.mDispatchers);
      }
    }
  }

  public Executor getExecutor() {
    if (mExecutor == null) mExecutor = Platform.get();
    return mExecutor;
  }

  public void setExecutor(Executor executor) {
    mExecutor = executor;
    if (mQueue != null) {
      if (mExecutor != mQueue.mExecutor) {
        mQueue = new RequestQueue(mQueue.mNetwork, mExecutor, mQueue.mDispatchers);
      }
    }
  }

  public RequestQueue getQueue() {
    if (mQueue == null) {
      mQueue = new RequestQueue(getHttpStack(), getExecutor());
      mQueue.start();
    }
    return mQueue;
  }

  public void setQueue(RequestQueue queue) {
    if (mQueue != null) mQueue.stop();
    mQueue = queue;
    mStack = mQueue.mNetwork;
    mExecutor = mQueue.mExecutor;
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
    Response.Network<T> response = null;
    try {
      response = getHttpStack().execute(request);
      return response.response();
    } finally {
      IOUtils.closeQuietly(response);
    }
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
      @Override public void queue(Callback<T> callback) {
        request.setCallback(callback);
        Restlight.this.queue(request);
      }
      @Override public Response<T> execute() throws Exception {
        return Restlight.this.execute(request);
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