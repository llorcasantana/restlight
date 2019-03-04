
import java.nio.charset.Charset;
import restlight.Converter;
import restlight.HttpRequest;
import restlight.HttpUrlStack;
import restlight.MultipartBody;
import restlight.ResponseBody;

public class Test {
     
  static {
    HttpRequest.registerConverter(String.class, new StringConverter());
  }
  
  public static class StringConverter implements Converter<String> {
    @Override
    public String converter(ResponseBody body, Charset charset) throws Exception {
      System.out.println("{code = " + body.code + "}");
      System.out.println("{headers = " + body.headers.toString() + "}");
      System.out.println("{contentLength = " + body.contentLength + "}");
      System.out.println("{contentEncoding = " + body.contentEncoding + "}");
      System.out.println("{contentType = " + body.contentType + "}");
      return body.string(charset);
    }
  }
  
  public static void main(String[] args) throws Exception {  
    HttpUrlStack stack = new HttpUrlStack();
    
    HttpRequest<String> request = HttpRequest.of(String.class);
    request.setAction("POST", "http://127.0.0.1/test.php");
    
    MultipartBody body = new MultipartBody();
    body.addParam("first", "Jesus");
    body.addParam("last", "Bx"); 
    body.addFile("archivo", new byte[]{'H', 'O', 'L', 'A', ' ', 'M', 'U', 'N', 'D', 'O'}, "texto.txt");
    request.setBody(body);
    
    ResponseBody response = stack.execute(request);
    System.out.println(request.parseResponse(response));
  }
}
