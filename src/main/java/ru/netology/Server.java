package ru.netology;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private final Map<String, Handler> handlers = new HashMap<>();
    private final int port;
    private final ExecutorService threadPool;

    public Server(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(64);
    }

    public void addHandler(String method, String path, Handler handler) {
        handlers.put(method + " " + path, handler);
    }

    public void start(int i) {
        try (final var serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);
            while (true) {
                try {
                    var clientSocket = serverSocket.accept();
                    handleRequest(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(Socket clientSocket) {
        try (var inputStream = clientSocket.getInputStream();
             var outputStream = new BufferedOutputStream(clientSocket.getOutputStream());
             var reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String requestLine = reader.readLine();
            if (requestLine == null) {
                return;
            }
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            //заголовки
            Map<String, String> headers = new HashMap<>();
            String headerLine;
            while (!(headerLine = reader.readLine()).isEmpty()) {
                String[] headerParts = headerLine.split(": ", 2);
                headers.put(headerParts[0], headerParts[1]);
            }

            //тело запроса
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            ru.netology.HttpRequest httpRequest = new ru.netology.HttpRequest(method, path, headers, new ByteArrayInputStream(body.toString().getBytes()));

            handleRequest(httpRequest, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(HttpRequest httpRequest, BufferedOutputStream responseStream) throws IOException {
        String method = httpRequest.getMethod();
        String path = httpRequest.getPath();

        Handler handler = handlers.get(method + " " + path);

        if (handler != null) {
            handler.handle(httpRequest, responseStream);
        } else {
            sendResponse(responseStream, "404 Not Found", "Ресурс не найден");
        }
    }


    void sendResponse(BufferedOutputStream responseStream, String status, String body) {
        try {
            String response = "HTTP/1.1 " + status + "\r\n" +
                    "Content-Length: " + body.length() + "\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "\r\n" +
                    body;
            responseStream.write(response.getBytes());
            responseStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}









