package restlight.body;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import restlight.FormBody;
import restlight.MultipartBody;
import restlight.RequestBody;

public final class SimpleRequestBody implements RequestBody {

  final String data;
  final String contentType;
  
  private SimpleRequestBody(String data, String contentType) {
    this.data = data;
    this.contentType = contentType;
  }
  
  public static RequestBody create(String data, String contentType) {
    return new SimpleRequestBody(data, contentType);
  }
  
  public static FormBody formUrlEncoded(Object src) {
    FormBody body = new FormBody();
    writeFields(body, src);
    return body;
  }
  
  public static MultipartBody multipart(Object src) {
    MultipartBody body = new MultipartBody();
    writeParts(body, src);
    return body;
  }

  private static void writeFields(FormBody body, Object obj) {
    java.lang.reflect.Field[] fields = obj.getClass().getDeclaredFields();
    try {
      for (java.lang.reflect.Field var : fields) {
        Field field = var.getAnnotation(Field.class);
        if (field != null) {
          String name = var.getName();
          String fieldName = field.value();
          if (fieldName != null && !fieldName.isEmpty()) {
            name = fieldName;
          }
          body.add(name, var.get(obj));
        }
      }
    } catch (Exception ignore) {
    }
  }
  
  private static void writeParts(MultipartBody body, Object obj) {
    java.lang.reflect.Field[] fields = obj.getClass().getDeclaredFields();
    try {
      for (java.lang.reflect.Field var : fields) {
        Field field = var.getAnnotation(Field.class);
        if (field != null) {
          String name = var.getName();
          String fieldName = field.value();
          if (fieldName != null && !fieldName.isEmpty()) {
            name = fieldName;
          }
          
          Object value = var.get(obj);
          if (value == null) 
            body.addParam(name, var.get(obj));
          else if (value instanceof File) 
            body.addFile(name, (File)value);
          else if (value instanceof MultipartBody.Part)
            body.addPart((MultipartBody.Part)value);
          else 
            body.addParam(name, var.get(obj));
        }
      }
    } catch (Exception ignore) {
    }
  }
  
//  static class Person {
//    public long id;
//    @Field("first_name") String first;
//    @Field("last_name") String last;
//    @Field("foto") File file = new File("C:\\Users\\Jesus\\Pictures\\add.png");
//  }
//  
//  public static void main(String[] args) throws Exception {
//    Person p = new Person();
//    p.first = "Jesus";
//    p.last = "B";
//    
//    RequestBody body = multipart(p);
//    body.writeTo(System.out, Charset.defaultCharset());
//  }

  @Override
  public String contentType(Charset charset) throws IOException {
    if (!contentType.contains("charset")) {
      StringBuilder sb = new StringBuilder(contentType);
      sb.append("; charset=");
      sb.append(charset.name());
      return sb.toString();
    }
    return contentType;
  }

  @Override
  public long contentLength(Charset charset) throws IOException {
    return data.getBytes(charset).length;
  }

  @Override
  public void writeTo(OutputStream out, Charset charset) throws IOException {
    out.write(data.getBytes(charset));
  }

}