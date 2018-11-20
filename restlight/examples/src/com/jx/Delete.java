package com.jx;

import restlight.BasicHttpStack;
import restlight.HttpUrl;
import restlight.Request;
import restlight.Response;
import restlight.StringRequest;

public class Delete {

  BasicHttpStack stack = new BasicHttpStack();

  String run() throws Exception {
    String url = new HttpUrl()
            .setUrl("http://127.0.0.1/test.php")
            .addQueryParameter("id", 101010)
            .toString();

    Request<String> request = new StringRequest()
            .setUrl(url)
            .setMethod("DELETE");

    try (Response<String> response = stack.execute(request)) {
      return request.parseResponse(response);
    }
  }

  public static void main(String... args) throws Exception {
    Delete obj = new Delete();
    System.out.println(obj.run());
  }
}
