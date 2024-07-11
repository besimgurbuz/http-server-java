import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompressor extends ContentCompressor {
  @Override
  byte[] compress(byte[] content) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try (GZIPOutputStream gzipOS = new GZIPOutputStream(outputStream)) {
      gzipOS.write(content);

      return outputStream.toByteArray();
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  byte[] decompress(byte[] compressedData) {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);

    try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[1024];
      int len;
      while ((len = gzipInputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, len);
      }

      return buffer;
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public String getEncoding() {
    return "gzip";
  }
}
