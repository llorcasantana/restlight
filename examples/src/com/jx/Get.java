package com.jx;

import restlight.HttpUrlStack;
import restlight.Request;
import restlight.ResponseBody;
import restlight.StringRequest;

public class Get {

  HttpUrlStack stack = new HttpUrlStack();

  String run() throws Exception {
    Request<String> request = new StringRequest()
            .setUrl("http://weather.livedoor.com/forecast/webservice/json/v1?city=130010")
            .setMethod("GET");

    try (ResponseBody response = stack.execute(request)) {
      return request.parseResponse(response);
    }
  }

  public static void main(String... args) throws Exception {
    Get obj = new Get();
    System.out.println(obj.run());
  }
}
