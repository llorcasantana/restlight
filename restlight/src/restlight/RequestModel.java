package restlight;

import java.util.concurrent.Executor;

public class RequestModel {

  private HttpStack httpStack;
  private Executor executorDelivery;
  
  public RequestModel() {
    httpStack = new HttpUrlStack();
    executorDelivery = Platform.get();
  }
  
  public HttpStack stack() {
    return httpStack;
  }

  public void setStack(HttpStack stack) {
    httpStack = stack;
  }

  public Executor executorDelivery() {
    return executorDelivery;
  }

  public void setExecutorDelivery(Executor executor) {
    executorDelivery = executor;
  }
}
