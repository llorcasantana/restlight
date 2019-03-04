package restlight;

import java.nio.charset.Charset;

public interface Converter<T> {
  T converter(ResponseBody body, Charset charset) throws Exception;
}