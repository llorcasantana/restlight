package com.jx.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// import com.squareup.okhttp.OkHttpClient;
// import java.io.IOException;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import restlight.BasicHttpStack;
import restlight.Call;
import restlight.Request;
import restlight.Restlight;
import restlight.StringRequest;
import restlight.body.FormBodyAdapter;
import restlight.request.GsonRequest;

public class PostDao {
  Gson gson;
  Restlight restlight;

  public PostDao() {
    gson = new GsonBuilder()
            .setDateFormat("M/d/yy hh:mm a")
            .create();

    restlight = Restlight.getInstance();
  }

  public Call<Post[]> getPosts() {
    Request<Post[]> request = GsonRequest.newRequest(gson, Post[].class)
            .setUrl("https://kylewbanks.com/rest/posts.json")
            .setMethod("GET");

    return restlight.newCall(request);
  }
  
  public Call<String> insert(Post p) {
//    GsonBody<Persona> body = new GsonBody<Persona>(gson, p);
    FormBodyAdapter body = FormBodyAdapter.of(p);
    
    Request<String> request = new StringRequest()
            .setMethod("POST")
            .setUrl("http://127.0.0.1/test.php")
            .setBody(body);
    
    return restlight.newCall(request);
  }

  //  BasicHttpStack stack = new BasicHttpStack() {  
  //    final OkHttpClient client = new OkHttpClient();
  //    @Override
  //    public HttpURLConnection open(URL src) throws IOException {
  //      return client.open(src);
  //    }
  //  };
}