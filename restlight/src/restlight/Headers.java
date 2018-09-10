package restlight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Headers {

  public static final String CONTENT_DISPOSITION = "Content-Disposition";
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";

  private final List<String> namesAndValues;

  public Headers() {
    this(10);
  }
  
  public Headers(int size) {
    namesAndValues = new ArrayList<String>(size * 2);
  }
  
  /** Devuelve la cantidad de valores de campo. */
  public int size() {
    return namesAndValues.size() / 2;
  }
  
  /**
   * Agrega un nuevo parámetro
   * @param key llave del parámetro
   * @param value valor del parámetro
   */
  public Headers add(String key, String value) {
    namesAndValues.add(key);
    namesAndValues.add(value);
    return this;
  }
  
  /** Devuelve el último valor correspondiente al campo especificado, o nulo. */
  public String key(int index) {
    return namesAndValues.get(index * 2);
  }
  
  /** Devuelve el valor en el índice. */
  public String value(int index) {
    return namesAndValues.get(index * 2 + 1);
  }
  
  /** Devuelve el último valor correspondiente al campo especificado, o nulo. */
  public String value(String key) {
    for (int i = namesAndValues.size() - 2; i >= 0; i -= 2) {
      if (key.equalsIgnoreCase(namesAndValues.get(i))) {
        return namesAndValues.get(i + 1);
      }
    }
    return null;
  }

  /** Devuelve encabezados para los nombres y valores de encabezado en el Mapa. */
  public static Headers of(Map<String, List<String>> map) {
    Headers headers = new Headers(map.size());
    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
      String key = entry.getKey();
      if (key != null) {
        List<String> value = entry.getValue();
        for (int i = 0; i < value.size(); i++) {
          headers.add(key, value.get(i));
        } 
      }
    }
    return headers;
  }

  /**
   * Returns headers for the alternating header names and values. There must be
   * an even number of arguments, and they must alternate between header names
   * and values.
   */
  public static Headers of(String... namesAndValues) {
    if (namesAndValues == null)  throw new NullPointerException("namesAndValues == null");
    if (namesAndValues.length % 2 != 0) {
      throw new IllegalArgumentException("Expected alternating header names and values");
    }

    Headers headers = new Headers(namesAndValues.length / 2);
    
    // Check for malformed headers.
    for (int i = 0; i < namesAndValues.length; i += 2) {
      String name = namesAndValues[i];
      String value = namesAndValues[i + 1];
      if (name == null || value == null) {
        throw new IllegalArgumentException("Headers cannot be null");
      }
      if (name.length() == 0 || name.indexOf('\0') != -1 || value.indexOf('\0') != -1) {
        throw new IllegalArgumentException("Unexpected header: " + name + ": " + value);
      }
      headers.add(name, value);
    }

    return headers;
  }

  @Override public String toString() {
    return namesAndValues.toString();
  }
}
