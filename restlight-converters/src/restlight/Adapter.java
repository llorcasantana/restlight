
package restlight;

public interface Adapter<T> {
  RequestBody adapter(T src);
}
