public class Main {

  public static void main(String[] args) {
    String directory = "./tmp";

    if (args.length > 1 && args[0].equals("--directory")) {
      directory = args[1];
    }

    HttpServer server = new HttpServer(4221, 10, directory);
    server.run();
    System.out.println("Server is running on port 4221");
  }
}
