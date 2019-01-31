
import restlight.BasicHttpStack;
import restlight.FormBody;
import restlight.Response;
import restlight.StringRequest;


public class TestConverter {

  public static void main(String[] args) throws Exception {
    BasicHttpStack stack = new BasicHttpStack();
    StringRequest request = new StringRequest();
    request.setMethod("POST");
    request.setUrl("http://www.example.com");
    
    FormBody body = new FormBody();
    body.add("username", "John");
    body.add("password", "pass");
    request.setBody(body);
    
    Response<String> response = stack.execute(request);
    response.parseRequest(request);
    System.out.println(response.result());
  }
}
