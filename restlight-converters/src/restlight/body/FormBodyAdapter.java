package restlight.body;

import java.io.File;
import restlight.FormBody;
import restlight.MultipartBody;

public final class FormBodyAdapter {

  private FormBodyAdapter() {
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
}
