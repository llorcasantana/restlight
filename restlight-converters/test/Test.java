
import restlight.Call;
import restlight.MultipartBody;
import restlight.Request;
import restlight.Restlight;
import restlight.StringRequest;

public class Test {   
  
  public static void main(String[] args) throws Exception { 
    Request request = new Request();
    request.setMethod("POST");
    request.setUrl("http://127.0.0.1/test.php");
    
    MultipartBody body = new MultipartBody();
    body.addParam("first", "Jesus");
    body.addParam("last", "Bx"); 
    body.addFile("archivo", new byte[]{'H', 'O', 'L', 'A', ' ', 'M', 'U', 'N', 'D', 'O'}, "texto.txt");
    request.setBody(body);
    
    Restlight restlight = Restlight.get();
    Request.Parse<String> parse = new StringRequest();
    Call<String> call = restlight.newCall(request, parse); 
    
    String data = call.execute();
    System.out.println(data);
  }
}
