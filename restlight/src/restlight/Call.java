
package restlight;

public interface Call<T> {
  /**
   * Envíe de forma asíncrona la solicitud y notifique su respuesta o si se 
   * produjo un error al hablar con el servidor, al crear la solicitud o al 
   * procesar la respuesta.
   * @param callback devolución de llamada
   */
  void execute(Callback<T> callback);
  
  /**
   * Devuelve la petición de la invocación del método.
   * @return petición
   */
  Request<T> request();
  
  /**
   * Cancele esta llamada. 
   */
  void cancel();
  
  /**
   * Valida si la llamada ha sido cancelada.
   * @return true se se cancelo
   */
  boolean isCancel();
}
