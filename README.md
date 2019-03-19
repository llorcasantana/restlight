## RestLight

Restlight es una librería **HTTP** para Android y Java, que facilita la creación de peticiones **HTTP** como: GET, POST, HEAD, OPTIONS, PUT, DELETE y TRACE; hacia servidores externos. [Descargar .jar](https://github.com/JesusBetaX/Restlight/raw/master/dist/restlight.jar) o [Ver demo](https://github.com/JesusBetaX/restlight/tree/master/examples/src/com/jx) 

## Ejemplos

Creamos un objeto para ejecutar las request.  
Envíe sincrónicamente la solicitud y devuelva su respuesta.
```
Restlight rest = Restlight.get();
```

### GET
```
String run() throws Exception {
  String url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=130010";
  Request request = new Request();
  request.setUrl(url);
  request.setMethod("GET");

  try (ResponseBody response = rest.execute(request)) {
    return response.string(request.getCharset());
  }
}
```

### POST
```
String run() throws Exception {
  FormBody body = new FormBody()
          .add("nombre", "Elizabéth Magaña")
          .add("edad", 22)
          .add("soltera", false);
    
  Request request = new Request();
  request.setUrl("http://127.0.0.1/test.php");
  request.setMethod("POST");
  request.setBody(body);

  try (ResponseBody response = rest.execute(request)) {
    return response.string(request.getCharset());
  }
}
```

### DELETE
```
String run() throws Exception {
  HttpUrl url = new HttpUrl()
          .setUrl("http://127.0.0.1/test.php")
          .addQueryParameter("id", 101010);

  Request request = new Request();
  request.setUrl(url);
  request.setMethod("DELETE");

  try (ResponseBody response = rest.execute(request)) {
    return response.string(request.getCharset());
  }
}
```

### DOWLOAD
```
File run() throws Exception {
  String downloadPath = "C:\\Users\\Jesus\\Desktop\\restlight.jar";
  Request.Parse<File> request = new DownloadRequest(downloadPath);
  request.setUrl("https://github.com/JesusBetaX/Restlight/raw/master/dist/restlight.jar");
  request.setMethod("GET");

  return rest.execute(request);
}
```

### UPLOAD
```
String run() throws Exception { 
  MultipartBody body = new MultipartBody()
          .addParam("nombre", "Elizabéth Magaña")
          .addFile("img", new File("C:\\Users\\jesus\\Pictures\\420089-Kycb_1600x1200.jpg"));
    
  Request request = new Request();
  request.setUrl("http://127.0.0.1/test.php");
  request.setMethod("POST");
  request.setBody(body);

  try (ResponseBody response = rest.execute(request)) {
    return response.string(request.getCharset());
  }
}
```

## [GSON](https://github.com/JesusBetaX/WebServiceDemo) 

En tu **build.gradle** necesitarás agregar las dependencias para **GSON**:

```
dependencies {
  ...
  compile 'com.google.code.gson:gson:2.4'
}
```

[Y descargar restlight-converters.jar](https://github.com/JesusBetaX/Restlight/raw/master/dist/restlight-converters.jar)


Ahora estamos listos para comenzar a escribir un código. Lo primero que querremos hacer es definir nuestro modelo **Post**
Cree un nuevo archivo llamado **Post.java** y defina la clase **Post** de esta manera:

```
public class Post {
  
  @SerializedName("id")
  public long ID;
    
  @SerializedName("date")
  public Date dateCreated;
 
  public String title;
  public String author;
  public String url;
  public String body;
}
```


Creemos una nueva instancia de **GSON** antes de llamar a la request. También necesitaremos establecer un formato de fecha personalizado en la instancia **GSON** para manejar las fechas que devuelve la API:

Definimos las interacciones de la base de datos. Pueden incluir una variedad de métodos de consulta.:

```
public class Dao {
  private Gson gson;
    
  public Dao() {
    gson = new GsonBuilder()
        .setDateFormat("M/d/yy hh:mm a")
        .create();
  }

  public Call<Post[]> getPosts() {
    Request.Parse<Post[]> request = GsonRequest.of(gson, Post[].class);
    request.setUrl("https://kylewbanks.com/rest/posts.json");
    request.setMethod("GET");
    
    Restlight restlight = Restlight.get();
    return restlight.newCall(request);
  }
}
```

Programa la solicitud para ser ejecutada en segundo plano. Ideal para aplicaciones android. 
Envía de manera asíncrona la petición y notifica a tu aplicación con un callback cuando una respuesta regresa.
```
...
Dao dao = new Dao();
    
Call<Post[]> call = dao.getPosts(); 
call.execute(new Callback<Post[]>() {
  @Override
  public void onResponse(Post[] result) throws Exception {
    List<Post> list = Arrays.asList(result);
    for (Post post : list) {
      System.out.println(post.title);
    }
  }
  @Override
  public void onFailure(Exception e) {
    e.printStackTrace(System.out);
  }
});
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
