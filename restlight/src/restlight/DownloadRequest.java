package restlight;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import restlight.io.IOUtils;

public class DownloadRequest extends Request<File> {

  private final String mDownloadPath;

  public DownloadRequest(String download_path) {
    mDownloadPath = download_path;
    setTimeoutMs(5000 * 2 * 2);
  }

  @Override
  public File parseResponse(ResponseBody response) throws Exception {
    File file = new File(mDownloadPath);
    BufferedOutputStream bos = null;
    try {
      bos = new BufferedOutputStream(new FileOutputStream(file));
      IOUtils.copy(response.inputStream, bos);
      return file;
    } finally {
      IOUtils.closeQuietly(bos);
    }
  }
}