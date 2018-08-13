package com.jx.dao;

import com.jx.library.WebService;
import com.jx.model.Post;
import restlight.Call;
import restlight.Request;

public class PostDao {

  WebService service;

  public PostDao() {
    service = WebService.getInstance();
  }

  public Call<Post[]> getPosts() {
    Request<Post[]> request = service.gsonRequest(Post[].class)
            .setUrl("https://kylewbanks.com/rest/posts.json")
            .setMethod("GET");
    
    return service.restlight().newCall(request);
  }
}
