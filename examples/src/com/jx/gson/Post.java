package com.jx.gson;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import restlight.body.Field;

public class Post {
  @SerializedName("id")
  @Field("id")
  public long id;

  @SerializedName("date")
  @Field("date")
  public Date dateCreated;

  @SerializedName("title")
  @Field("title")
  public String title;
  
  @SerializedName("author")
  @Field("author")
  public String author;
  
  @SerializedName("url")
  public String url;
  
  @SerializedName("body")
  public String body;
}
