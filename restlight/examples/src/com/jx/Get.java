package com.jx;

import restlight.BasicHttpStack;
import restlight.Request;
import restlight.Response;
import restlight.StringRequest;

public class Get {

  BasicHttpStack stack = new BasicHttpStack();

  String run() throws Exception {
    String url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=130010";
    Request<String> request = new StringRequest()
            .setUrl(url)
            .setMethod("GET");

    try (Response.Network<String> net = stack.execute(request)) {
      return request.parseResponse(net);
    }
  }

  public static void main(String... args) throws Exception {
    Get obj = new Get();
    System.out.println(obj.run());
  }
}
