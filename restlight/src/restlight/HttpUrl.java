package restlight;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpUrl {
  final List<String> namesAndValues = new ArrayList<String>();
  final String url;

  public HttpUrl(String url) {
    this.url = url;
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
    if (size() == 0) {
      return url;
    }
    try {
      String encodedParams = encodedUrlParams(charset);
      int len = encodedParams.length();
      if (len > 0) {
        return new StringBuilder(url.length() + 1 + len)
              .append(url)
              .append(url.contains("?") ? '&' : '?')
              .append(encodedParams)
              .toString();
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
