import java.util.Map;

public class CompressorFactory {
  private static Map<String, ContentCompressor> compressors = Map.of(
    "gzip", new GzipCompressor()
  );

  public static ContentCompressor getCompressor(String[] encodings) {
    for (String encoding : encodings) {
      ContentCompressor compressor = compressors.get(encoding);

      if (compressor != null) {
        return compressor;
      }
    }

    return null;
  }
}
