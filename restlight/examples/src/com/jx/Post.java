package com.jx;

import restlight.BasicHttpStack;
import restlight.FormBody;
import restlight.Request;
import restlight.Response;
import restlight.StringRequest;

public class Post {

  BasicHttpStack stack = new BasicHttpStack();

  String run() throws Exception {
    FormBody body = new FormBody()
            .add("nombre", "Elizabéth Magaña")
            .add("edad", 22)
            .add("soltera", false);
    
    String url = "http://127.0.0.1/test.php";
    Request<String> request = new StringRequest()
            .setUrl(url)
            .setMethod("POST")
            .setBody(body);

    try (Response<String> response = stack.execute(request)) {
      return request.parseResponse(response);
    }
  }

  public static void main(String... args) throws Exception {
    Post obj = new Post();
    System.out.println(obj.run());
  }
}
