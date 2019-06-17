package com.jx;

import restlight.HttpUrl;
import restlight.HttpUrlStack;
import restlight.Request;
import restlight.ResponseBody;

public class Delete {

  HttpUrlStack stack = new HttpUrlStack();

  String run() throws Exception {
    HttpUrl url = new HttpUrl("http://127.0.0.1/test.php")
            .addQueryParameter("id", 101010);

    Request request = new Request();
    request.setUrl(url); 
    request.setMethod("DELETE");

    try (ResponseBody response = stack.execute(request)) {
      return response.string(request.getCharset());
    }
  }

  public static void main(String... args) throws Exception {
    Delete obj = new Delete();
    System.out.println(obj.run());
  }
}
