package restlight.platform;

import java.util.concurrent.Executor;
import javax.swing.SwingUtilities;

public class JavaSwingExecutor implements Executor {
  @Override public void execute(Runnable r) {
    SwingUtilities.invokeLater(r);
  }
}
