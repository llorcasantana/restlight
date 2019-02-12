```

Programa la solicitud para ser ejecutada en segundo plano. Ideal para aplicaciones android. 
Envía de manera asíncrona la petición y notifica a tu aplicación con un callback cuando una respuesta regresa.
```
import restlight.Converter;
import restlight.HttpUrlStack;
import restlight.MultipartBody;
import restlight.Request;
import restlight.ResponseBody;
import restlight.SimpleRequest;

public class Test {
     
  static {
    SimpleRequest.registerConverter(String.class, new StringConverter());
  }
  
  public static class StringConverter implements Converter<String> {
    @Override
    public String converter(ResponseBody body) throws Exception {
      return body.string(Request.DEFAULT_ENCODING);
    }
  }
  
  public static void main(String[] args) throws Exception {  
    HttpUrlStack stack = new HttpUrlStack();
    
    SimpleRequest<String> request = SimpleRequest.of(String.class);
    request.setAction("POST", "http://127.0.0.1/test.php");
    
    MultipartBody body = new MultipartBody();
    body.addParam("first", "Jesus");
    body.addParam("last", "Bx"); 
    body.addFile("archivo", new byte[]{'H', 'O', 'L', 'A', ' ', 'M', 'U', 'N', 'D', 'O'}, "texto.txt");
    request.setBody(body);
    
    ResponseBody response = stack.execute(request);
    System.out.println(request.parseResponse(response));
  }
}
```
License
=======

    Copyright 2018 JesusBetaX, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
