package com.jx;

import restlight.HttpUrlStack;
import restlight.FormBody;
import restlight.Request;
import restlight.ResponseBody;
import restlight.StringRequest;

public class Post {

  HttpUrlStack stack = new HttpUrlStack();

  String run() throws Exception {
    FormBody body = new FormBody()
            .add("nombre", "Elizabéth Magaña")
            .add("edad", 22)
            .add("soltera", false);
    
    Request<String> request = new StringRequest()
            .setUrl("http://127.0.0.1/test.php")
            .setMethod("POST")
            .setBody(body);

    try (ResponseBody response = stack.execute(request)) {
      return request.parseResponse(response);
    }
  }

  public static void main(String... args) throws Exception {
    Post obj = new Post();
    System.out.println(obj.run());
  }
}
