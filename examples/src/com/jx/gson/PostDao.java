package com.jx.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// import com.squareup.okhttp.OkHttpClient;
// import java.io.IOException;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import restlight.BasicHttpStack;
import restlight.Call;
import restlight.Restlight;
import restlight.StringRequest;
import restlight.body.GsonBody;
import restlight.request.GsonRequest;

public class PostDao {
  Gson gson;

  public PostDao() {
    gson = new GsonBuilder()
            .setDateFormat("M/d/yy hh:mm a")
            .create();
  }

  public Call<Post[]> getPosts() {
    GsonRequest<Post[]> request = GsonRequest.of(gson, Post[].class);
    request.setUrl("https://kylewbanks.com/rest/posts.json");
    request.setMethod("GET");

    Restlight restlight = Restlight.get();
    return restlight.newCall(request);
  }
  
  public Call<String> insert(Post p) {
    GsonBody<Post> body = new GsonBody<Post>(gson, p);
    
    StringRequest request = new StringRequest();
    request.setMethod("POST");
    request.setUrl("http://127.0.0.1/test.php");
    request.setBody(body);
    
    Restlight restlight = Restlight.get();
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
