package restlight.platform;

import java.util.concurrent.Executor;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import javax.swing.SwingUtilities;

public class Platform implements Executor {
  private static Platform platform;
  public static Platform get() {
    if (platform == null) platform = findPlatform();
    return platform;
  }
  public static void set(Platform platform) {
    Platform.platform = platform;
  }
    
  /** Attempt to match the host runtime to a capable Platform implementation. */
  private static Platform findPlatform() {
    try {
      Class.forName("android.os.Build");
      if (Build.VERSION.SDK_INT != 0) {
        return new Android();
      }
    } catch (ClassNotFoundException ignored) {
    }
    try {
      Class.forName("javax.swing.SwingUtilities");
      return new JavaSwing();
    } catch (ClassNotFoundException ignored) {
    }
    return new Platform();
  }

  @Override public void execute(Runnable r) {
    r.run();
  }
  
  public static class Android extends Platform {
    final Handler handler = new Handler(Looper.getMainLooper());
    @Override public void execute(Runnable r) {
      handler.post(r);
    }
  }

  public static class JavaSwing extends Platform {
    @Override public void execute(Runnable r) {
      SwingUtilities.invokeLater(r);
    }
  }
}
