package restlight.platform;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;

public class AndroidExecutor implements Executor {
  final Handler handler = new Handler(Looper.getMainLooper());
  @Override public void execute(Runnable r) {
    handler.post(r);
  }
}
