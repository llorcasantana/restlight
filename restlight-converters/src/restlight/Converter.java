package restlight;

public interface Converter<T> {
  T converter(ResponseBody body) throws Exception;
}