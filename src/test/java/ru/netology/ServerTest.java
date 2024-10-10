package ru.netology;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {
    private Server server;
    private Thread serverThread;

    @BeforeEach
    public void setUp() {
        server = new Server(9999);
        server.addHandler("GET", "/test", (request, response) -> {
            String body = "Test response";
            server.sendResponse(response, "200 OK", body);
        });
        serverThread = new Thread(() -> server.start(0));
        serverThread.start();
    }

    @AfterEach
    public void tearDown() throws IOException {
        serverThread.interrupt();
        serverThread = null;
    }

    @Test
    public void testHandleGetRequest() throws Exception {
        Socket socket = new Socket("localhost", 9999);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("GET /test HTTP/1.1");
        out.println("Host: localhost");
        out.println();
        out.flush();

        String responseLine = in.readLine();
        assertNotNull(responseLine);
        assertTrue(responseLine.contains("200 OK"));

        StringBuilder responseBody = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            responseBody.append(line).append("\n");
        }

        assertTrue(responseBody.toString().contains("Test response"));

        in.close();
        out.close();
        socket.close();
    }

}