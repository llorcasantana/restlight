package com.jx;

import restlight.Callback;
import restlight.Request;
import restlight.RequestBody;
import restlight.Response;
import restlight.Restlight;
import restlight.StringRequest;

public class HttpRequest<T> implements Callback<T> {
  private final Request<T> request;
  private ResponseListener<T> onresponse;
  private ErrorListener onerror;
  private boolean async;
  public HttpRequest(Request<T> request) {
    this.request = request;
  }
  public Request<T> request() {
    return request;
  }
  public HttpRequest<T> action(String method, String url, boolean async) {
    request.setMethod(method).setUrl(url);
    this.async = async;
    return this;
  }
  public HttpRequest<T> action(String method, String url) {
    action(method, url, false);
    return this;
  }
  public interface ResponseListener<V> {
    void onResponse(Response<V> response) throws Exception;
  }
  public HttpRequest<T> setOnResponseListener(ResponseListener<T> listener) {
    onresponse = listener;
    return this;
  }
  @Override public void onResponse(Response<T> response) throws Exception {
    if (onresponse != null) onresponse.onResponse(response);
  }
  public interface ErrorListener {
    void onErrorResponse(Exception e);
  }
  public HttpRequest<T> setOnErrorListener(ErrorListener listener) {
    onerror = listener;
    return this;
  }
  @Override public void onErrorResponse(Exception e) {
    if (onerror != null) onerror.onErrorResponse(e);
  }
  public HttpRequest<T> send() {
    return send(null);
  }
  public HttpRequest<T> send(RequestBody body) {
    request.setCallback(this).setBody(body);
    if (async) { 
      Restlight.getInstance().queue(request); 
    } else {
      try {
        onResponse(exec());
      } catch (Exception e) {
        onErrorResponse(e);
      }
    }
    return this;
  }
  public Response<T> exec() throws Exception {
    return Restlight.getInstance().execute(request);
  }
  
  public static void main(String[] args) {
    new HttpRequest<String>(new StringRequest()) {
      @Override
      public void onResponse(Response<String> response) throws Exception {
        System.out.println(response);
      }
      @Override
      public void onErrorResponse(Exception e) {
        e.printStackTrace(System.out);
      }
    }
    .action("GET", "http://weather.livedoor.com/forecast/webservice/json/v1?city=130010")
    .send();
  }
}
