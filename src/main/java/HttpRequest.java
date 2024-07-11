import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
  private String method;
  private String route;
  private String version;
  private String body;

  private Map<String, String> headers = new HashMap<>();

  public HttpRequest(String method, String route, String version, Map<String, String> headers, String body) {
    this.method = method;
    this.route = route;
    this.version = version;
    this.headers = headers;
    this.body = body;
  }

  public String getMethod() {
    return method;
  }

  public String getRoute() {
    return route;
  }

  public String[] getRouteParts() {
    return route.split("/");
  }

  public String getVersion() {
    return version;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public String[] getAcceptEncodings() {
    return headers.getOrDefault("Accept-Encoding", "").split(", ");
  }

  public String getBody() {
    return body;
  }

  public boolean hasHeader(String header) {
    return headers.containsKey(header);
  }

  public boolean hasBody() {
    return !body.isEmpty();
  }

  public boolean isGet() {
    return method.equals("GET");
  }

  public boolean isPost() {
    return method.equals("POST");
  }

  public static HttpRequest from(BufferedReader bufferedReader) throws IOException {
    String requestLine = bufferedReader.readLine();
    String[] request = requestLine.split(" ", 0);
    Map<String, String> headers = parseHeaders(bufferedReader);
    int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
    String body = contentLength > 0 ? parseBody(bufferedReader, contentLength) : "";

    HttpRequest httpRequest = new HttpRequest(
      request[0],
      request[1],
      request[2],
      headers,
      body
    );

    return httpRequest;
  }

  private static Map<String, String> parseHeaders(BufferedReader bufferedReader) throws IOException {
    Map<String, String> headers = new HashMap<>();
    String line;
    while (!(line = bufferedReader.readLine()).isBlank()) {
      String[] header = line.split(": ", 2);
      headers.put(header[0], header[1]);
    }
    return headers;
  }

  private static String parseBody(BufferedReader bufferedReader, int contentLength) throws IOException {
    char[] buffer = new char[contentLength];
    bufferedReader.read(buffer, 0, contentLength);
    return new String(buffer);
  }
}
