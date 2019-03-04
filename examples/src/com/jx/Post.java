package com.jx;

import restlight.FormBody;
import restlight.HttpUrlStack;
import restlight.Request;
import restlight.ResponseBody;

public class Post {

  HttpUrlStack stack = new HttpUrlStack();

  String run() throws Exception {
    FormBody body = new FormBody()
            .add("nombre", "Elizabéth Magaña")
            .add("edad", 22)
            .add("soltera", false);
    
    Request request = new Request();
    request.setUrl("http://127.0.0.1/test.php");
    request.setMethod("POST");
    request.setBody(body);

    try (ResponseBody response = stack.execute(request)) {
      return response.string(request.getCharset());
    }
  }

  public static void main(String... args) throws Exception {
    Post obj = new Post();
    System.out.println(obj.run());
  }
}
