package restlight.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Wrapper for a {@link HttpURLConnection}'s InputStream which disconnects the
 * connection on stream close.
 */
public class UrlConnectionInputStream extends InputStream {

  private final InputStream mInputStream;
  private final HttpURLConnection mConnection;
  
  public UrlConnectionInputStream(HttpURLConnection connection) {
    mInputStream = inputStreamFromConnection(connection);
    mConnection = connection;
  }
  
  /**
   * Initializes an {@link InputStream} from the given
   * {@link HttpURLConnection}.
   *
   * @param connection
   * @return an HttpEntity populated with data from <code>connection</code>.
   */
  private static InputStream inputStreamFromConnection(HttpURLConnection connection) {
    try {
      return connection.getInputStream();
    } catch (IOException ioe) {
      return connection.getErrorStream();
    }
  }

  @Override public int read() throws IOException {
    return mInputStream.read();
  }
  
  @Override public void close() throws IOException {
    super.close();
    mConnection.disconnect();
  }
}
