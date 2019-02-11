package com.jx.gson;

import java.util.Arrays;
import java.util.List;
import restlight.Call;
import restlight.Callback;

public class Gson {

  public static void main(String[] args) {
    PostDao dao = new PostDao();
    
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
    
  }
}
