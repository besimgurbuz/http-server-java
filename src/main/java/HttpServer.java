import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
  private final int port;
  private final ExecutorService executorService;

  public HttpServer(int port, final int threadPoolSize) {
    this.port = port;
    this.executorService = Executors.newFixedThreadPool(threadPoolSize);
  }

  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      serverSocket.setReuseAddress(true);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        executorService.submit(() -> handleRequest(clientSocket));
      }

    } catch (Exception e) {
      System.out.println("HttpServer Exception: " + e.getMessage());
    }
  }

  private void handleRequest(Socket clientSocket) {
    try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         final OutputStream outputStream = clientSocket.getOutputStream()) {

      HttpRequest request = HttpRequest.from(bufferedReader);
      HttpResponse response;

      if (request.getRoute().equals("/")) {
        response = new HttpResponse.Builder()
          .status(StatusCode.OK)
          .build();
      } else if (request.getRoute().equals("/user-agent")) {
        response = new HttpResponse.Builder()
          .status(StatusCode.OK)
          .contentType("text/plain")
          .body(request.getHeaders().get("User-Agent"))
          .build();
      } else if (request.getRoute().startsWith("/echo")) {
        response = new HttpResponse.Builder()
          .status(StatusCode.OK)
          .contentType("text/plain")
          .body(request.getRoute().substring(request.getRoute().lastIndexOf('/') + 1))
          .build();
      } else {
        response = new HttpResponse.Builder()
          .status(StatusCode.NOT_FOUND)
          .build();
      }

      outputStream.write(response.serialize().getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
