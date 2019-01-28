package com.jx;

import java.io.File;
import restlight.BasicHttpStack;
import restlight.MultipartBody;
import restlight.Request;
import restlight.Response;
import restlight.StringRequest;

public class Upload {

  BasicHttpStack stack = new BasicHttpStack();

  String run() throws Exception { 
    MultipartBody body = new MultipartBody()
            .addParam("nombre", "Elizabéth Magaña")
            .addFile("img", new File("C:\\Users\\jesus\\Pictures\\420089-Kycb_1600x1200.jpg"));
    
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
    Upload obj = new Upload();
    System.out.println(obj.run());
  }
}
