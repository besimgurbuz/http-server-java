import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
  public static Map<String, String> parseHeaders(BufferedReader reader) throws IOException {
    Map<String, String> headers = new HashMap<>();
    String headerLine = reader.readLine();
    while (!headerLine.isEmpty() && headerLine != null) {
      System.out.println(headerLine);
      String[] headerSplit = headerLine.split(": ");
      headers.put(headerSplit[0], headerSplit[1]);
      headerLine = reader.readLine();
    }

    return headers;
  }

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage

    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    try {
      serverSocket = new ServerSocket(4221);
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept(); // Wait for connection from client.

      InputStream inputStream = clientSocket.getInputStream();

      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String requestLine = reader.readLine();
      String[] request = requestLine.split(" ", 0);
      String route = request[1];
      Map<String, String> headers = parseHeaders(reader);

      OutputStream output = clientSocket.getOutputStream();

      if (route.equals("/")) {
        output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
      } else if (route.startsWith("/echo")) {
        String responseBody = route.substring(route.lastIndexOf('/') + 1);
        String response = String.format("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r\n%s", responseBody.length(), responseBody);

        output.write(response.getBytes());
      } else if (route.equals("/user-agent")) {
        System.out.println("Headers: " + headers);
        String userAgent = headers.get("User-Agent");
        String response = String.format("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r\n%s", userAgent.length(), userAgent);

        output.write(response.getBytes());
      } else {
        output.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
      }
      System.out.println("accepted new connection");
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
