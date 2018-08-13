package restlight;

public interface Callback<T> {
  /**
   * Se llama cuando se recibe una respuesta.
   *
   * @param response respuesta obtenida
   * @throws java.lang.Exception error al recibir la respueta
   */
  public void onResponse(Response<T> response) throws Exception;
  
  /**
   * Método de devolución de llamada que indica que se ha producido un error con
   * el error proporcionado código y mensaje opcional legible por el usuario.
   * @param e error causado
   */
  public void onErrorResponse(Exception e);
}
