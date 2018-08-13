package com.jx;

import java.io.File;
import restlight.BasicHttpStack;
import restlight.DownloadRequest;
import restlight.Request;
import restlight.Response;

public class Dowload {
  BasicHttpStack stack = new BasicHttpStack();

  File run() throws Exception {
    String downloadPath = "C:\\Users\\Jesus\\Desktop\\restlight.jar";
    Request<File> request = new DownloadRequest(downloadPath)
            .setUrl("https://github.com/JesusBetaX/Restlight/raw/master/dist/restlight.jar")
            .setMethod("GET");

    try (Response.Network<File> net = stack.execute(request)) {
      return request.parseResponse(net);
    }
  }

  public static void main(String... args) throws Exception {
    Dowload obj = new Dowload();
    System.out.println(obj.run());
  }
}
