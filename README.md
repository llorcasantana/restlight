## Restlight

Restlight es una librería **HTTP** para Android y Java, que facilita la creación de peticiones **HTTP** como: GET, POST, HEAD, OPTIONS, PUT, DELETE y TRACE; hacia servidores externos. [Descargar .jar](https://github.com/JesusBetaX/Restlight/raw/master/dist/restlight.jar) o [Ver demo](https://github.com/JesusBetaX/restlight/tree/master/restlight/examples/src/com/jx) 

## Ejemplos

Creamos un objeto para ejecutar las request.  
```
BasicHttpStack stack = new BasicHttpStack();
```

### GET
```
String run() throws Exception {
  String url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=130010";
  Request<String> request = new StringRequest()
          .setUrl(url)
          .setMethod("GET");

  try (Response.Network<String> net = stack.execute(request)) {
    return request.parseResponse(net);
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
    
  Request<String> request = new StringRequest()
          .setUrl("http://127.0.0.1/test.php")
          .setMethod("POST")
          .setBody(body);

  try (Response.Network<String> net = stack.execute(request)) {
    return request.parseResponse(net);
  }
}
```

### DELETE
```
String run() throws Exception {
  String url = new HttpUrl()
          .setUrl("http://127.0.0.1/test.php")
          .addQueryParameter("id", 101010)
          .toString();

  Request<String> request = new StringRequest()
          .setUrl(url)
          .setMethod("DELETE");

  try (Response.Network<String> net = stack.execute(request)) {
    return request.parseResponse(net);
  }
}
```

### DOWLOAD
```
File run() throws Exception {
  String downloadPath = "C:\\Users\\Jesus\\Desktop\\restlight.jar";
  Request<File> request = new DownloadRequest(downloadPath)
          .setUrl("https://github.com/JesusBetaX/Restlight/raw/master/dist/restlight.jar")
          .setMethod("GET");

  try (Response.Network<File> net = stack.execute(request)) {
    return request.parseResponse(net);
  }
}
```

### UPLOAD
```
String run() throws Exception { 
  MultipartBody body = new MultipartBody()
          .addParam("nombre", "Elizabéth Magaña")
          .addFile("img", new File("C:\\Users\\jesus\\Pictures\\420089-Kycb_1600x1200.jpg"));
    
  Request<String> request = new StringRequest()
          .setUrl("http://127.0.0.1/test.php")
          .setMethod("POST")
          .setBody(body);

  try (Response.Network<String> net = stack.execute(request)) {
    return request.parseResponse(net);
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

```
public final class WebService {

  private static WebService instance;
  
  private final Gson gson = new GsonBuilder()
  		.setDateFormat("M/d/yy hh:mm a")
		.create();
  
  private final Restlight restlight;
  
  private WebService() {
    restlight = new Restlight(new Executor() {
      final Handler handler = new Handler(Looper.getMainLooper());
      @Override public void execute(Runnable r) {
        handler.post(r);
      }
    });
  }
  
  public static WebService getInstance() {
    if (instance == null)  instance = new WebService();
    return instance;
  }
  
  public <T> Request<T> gsonRequest(final Class<T> classOf) {
    return new Request<T>() {
      @Override
      public T parseResponse(Response.Network<T> response) throws Exception {
        String json = new String(response.readByteArray(), getCharset());
        return gson.fromJson(json, classOf);
      }
    };
  }
  
  public Restlight restlight() {
    return restlight;
  }
}
```


Definimos las interacciones de la base de datos. Pueden incluir una variedad de métodos de consulta.:

```
public class Dao {
  WebService service;
    
  public Dao() {
    service = WebService.getInstance();
  }

  public Call<Post[]> getPosts() {
    Request<Post[]> request = service.gsonRequest(Post[].class)
            .setUrl("https://kylewbanks.com/rest/posts.json")
            .setMethod("GET");
    
    return service.restlight().newCall(request);
  }
}
```

Programa la solicitud para ser ejecutada en segundo plano.
```
...
Dao dao = new Dao();
    
Call<Post[]> call = dao.getPosts(); 
call.queue(new Callback<Post[]>() {
  @Override
  public void onResponse(Response<Post[]> response) throws Exception {
    List<Post> list = Arrays.asList(response.result());
    for (Post post : list) {
      System.out.println(post.title);
    }
  }
  @Override
  public void onErrorResponse(Exception e) {
    e.printStackTrace(System.out);
  }
});
```
