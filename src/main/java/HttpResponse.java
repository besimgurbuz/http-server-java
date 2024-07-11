import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final String version;
    private final int statusCode;
    private final String statusText;
    private final Map<String, String> headers;
    private final String body;

    private HttpResponse(Builder builder) {
        this.version = builder.version;
        this.statusCode = builder.statusCode;
        this.statusText = builder.statusText;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public String getVersion() {
        return version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String serialize() {
        StringBuilder response = new StringBuilder();
        response.append(version)
                .append(" ")
                .append(statusCode)
                .append(" ")
                .append(statusText)
                .append("\r\n");

        headers.forEach((key, value) -> response.append(key)
                .append(": ")
                .append(value)
                .append("\r\n"));

        response.append("\r\n")
                .append(body);

        return response.toString();
    }


    public static class Builder {
        private String version = "HTTP/1.1";
        private int statusCode = 200;
        private String statusText = "OK";
        private Map<String, String> headers = new HashMap<>();
        private String body = "";

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder status(StatusCode statusCode) {
            this.statusCode = statusCode.getCode();
            this.statusText = statusCode.getText();
            return this;
        }

        public Builder contentType(String contentType) {
            headers.put("Content-Type", contentType);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(String body, String[] encodings) {
          ContentCompressor compressor = CompressorFactory.getCompressor(encodings);

          if (compressor != null) {
            this.body = compressor.compress(body.getBytes()).toString();
            this.headers.put("Content-Encoding", compressor.getEncoding());
          } else {
            this.body = body;
          }

          this.headers.put("Content-Length", String.valueOf(body.length()));
          return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }
}
