Programa la solicitud para ser ejecutada en segundo plano. Ideal para aplicaciones android. 
Envía de manera asíncrona la petición y notifica a tu aplicación con un callback cuando una respuesta regresa.
```
import restlight.Call;
import restlight.MultipartBody;
import restlight.Request;
import restlight.Restlight;
import restlight.StringRequest;

public class Test {
     
  public static void main(String[] args) throws Exception {  
    Request request = new Request();
    request.setMethod("POST");
    request.setUrl("http://127.0.0.1/test.php");
    
    MultipartBody body = new MultipartBody();
    body.addParam("first", "Jesus");
    body.addParam("last", "Bx"); 
    body.addFile("archivo", new byte[]{'H', 'O', 'L', 'A', ' ', 'M', 'U', 'N', 'D', 'O'}, "texto.txt");
    request.setBody(body);
    
    Restlight restlight = Restlight.get();
    Request.Parse<String> parse = new StringRequest();
    Call<String> call = restlight.newCall(request, parse); 
    
    String data = call.execute();
    System.out.println(data);
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
