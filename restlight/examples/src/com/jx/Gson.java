package com.jx;

import com.jx.dao.PostDao;
import com.jx.model.Post;
import java.util.Arrays;
import java.util.List;
import restlight.Call;
import restlight.Callback;
import restlight.Response;

public class Gson {

  public static void main(String[] args) {
    PostDao dao = new PostDao();
    
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
    
  }
}
