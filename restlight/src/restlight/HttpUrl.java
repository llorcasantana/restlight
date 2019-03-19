package restlight;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpUrl {
  final List<String> namesAndValues = new ArrayList<String>();
  String url;

  public HttpUrl setUrl(String url) {
    this.url = url;
    return this;
  }
  public String getUrl() {
    return url;
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
  
  public String encodedUrlParams(Charset chrst) throws IOException {
    StringBuilder sb = new StringBuilder();
    for (int i = 0, size = size(); i < size; i++) {
      if (i > 0) sb.append('&');
      sb.append(URLEncoder.encode(key(i), chrst.name()));
      sb.append('=');
      sb.append(URLEncoder.encode(value(i), chrst.name()));
    }
    return sb.toString();
  }

  public String toString(Charset charset) {
    try {
      String encodedParams = encodedUrlParams(charset);
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
  
  @Override public String toString() {
    return toString(Request.DEFAULT_ENCODING);
  }
}
