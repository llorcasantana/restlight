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

    try (Response<String> response = stack.execute(request)) {
      return request.parseResponse(response);
    }
  }

  public static void main(String... args) throws Exception {
    Get obj = new Get();
    for (int i = 0; i < 100; i++) {
      System.out.println("result " + i + " = " + obj.run());
    }
    
  }
}
