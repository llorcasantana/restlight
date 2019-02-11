package com.jx;

import restlight.HttpUrlStack;
import restlight.HttpUrl;
import restlight.Request;
import restlight.ResponseBody;
import restlight.StringRequest;

public class Delete {

  HttpUrlStack stack = new HttpUrlStack();

  String run() throws Exception {
    HttpUrl url = new HttpUrl()
            .setUrl("http://127.0.0.1/test.php")
            .addQueryParameter("id", 101010);

    Request<String> request = new StringRequest()
            .setUrl(url)
            .setMethod("DELETE");

    try (ResponseBody response = stack.execute(request)) {
      return request.parseResponse(response);
    }
  }

  public static void main(String... args) throws Exception {
    Delete obj = new Delete();
    System.out.println(obj.run());
  }
}
