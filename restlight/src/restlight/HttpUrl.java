package restlight;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpUrl {
  Charset charset = Request.DEFAULT_ENCODING;
  final List<String> namesAndValues = new ArrayList<String>();
  String url;

  public HttpUrl setUrl(String url) {
    this.url = url;
    return this;
  }
  public String getUrl() {
    return url;
  }
  
  public Charset getCharset() {
    return charset;
  }
  public HttpUrl setCharset(Charset charset) {
    this.charset = charset;
    return this;
  }
  
  public int size() {
    return namesAndValues.size() / 2;
  }
  
  public HttpUrl addQueryParameter(String key, Object value) {
    String strValue = value == null ? "" : value.toString();
    namesAndValues.add(key);
    namesAndValues.add(strValue);
    return this;
  }
  
  public String key(int index) {
    return namesAndValues.get(index * 2);
  }
  public String value(int index) {
    return namesAndValues.get(index * 2 + 1);
  }
  
  public String encodedUrlParams() throws IOException {
    final String chrst = charset.name();
    StringBuilder sql = new StringBuilder();
    for (int i = 0, size = size(); i < size; i++) {
      if (i > 0) sql.append('&');
      sql.append(URLEncoder.encode(key(i), chrst));
      sql.append('=');
      sql.append(URLEncoder.encode(value(i), chrst));
    }
    return sql.toString();
  }

  @Override public String toString() {
    try {
      String encodedParams = encodedUrlParams();
      if (encodedParams != null && encodedParams.length() > 0) {
        return new StringBuilder(url.length() + 1 + encodedParams.length())
              .append(url)
              .append(url.contains("?") ? '&' : '?')
              .append(encodedParams).toString();
      }
    } catch(IOException ignore) {
      // empty
    }
    return url;
  }
}
