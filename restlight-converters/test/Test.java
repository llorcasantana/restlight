
import restlight.Converter;
import restlight.HttpUrlStack;
import restlight.MultipartBody;
import restlight.Request;
import restlight.ResponseBody;
import restlight.SimpleRequest;

public class Test {
     
  static {
    SimpleRequest.registerConverter(String.class, new StringConverter());
  }
  
  public static class StringConverter implements Converter<String> {
    @Override
    public String converter(ResponseBody body) throws Exception {
      return body.string(Request.DEFAULT_ENCODING);
    }
  }
  
  public static void main(String[] args) throws Exception {  
    HttpUrlStack stack = new HttpUrlStack();
    
    SimpleRequest<String> request = SimpleRequest.of(String.class);
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
