package restlight.body;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import restlight.RequestBody;
import restlight.io.IOUtils;

public class GsonBody<T> extends RequestBody {

  private final Gson gson;
  private final T src;
  private final TypeAdapter<T> adapter;

  public GsonBody(Gson gson, T src) {
    this.gson = gson;
    this.src = src;
    this.adapter = gson.getAdapter((Class<T>) src.getClass());
  }

  public GsonBody(T src) {
    this(new Gson(), src);
  }

  public Gson getGson() {
    return gson;
  }

  public T getSrc() {
    return src;
  }

  public TypeAdapter<T> getAdapter() {
    return adapter;
  }

  @Override
  public void writeTo(OutputStream out, Charset charset) throws IOException {
    JsonWriter jsonWriter = null;
    try {
      jsonWriter = gson.newJsonWriter(new OutputStreamWriter(out, charset));
      adapter.write(jsonWriter, src);
    } finally {
      IOUtils.closeQuietly(jsonWriter);
    }
  }

  @Override
  public String contentType(Charset charset) throws IOException {
    return String.format("application/json; charset=%s", charset.name());
  }

  @Override
  public long contentLength(Charset charset) throws IOException {
    ByteArrayOutputStream baos = null;
    try {
      baos = IOUtils.arrayOutputStream();
      writeTo(baos, charset);
      return baos.size();
    } finally {
      IOUtils.closeQuietly(baos);
    }
  }
}
