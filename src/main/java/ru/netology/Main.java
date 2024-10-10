package ru.netology;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        final var server = new Server(9999);


        server.addHandler("GET", "/messages", (request, responseStream) -> {
            // TODO: обработка GET запроса
            try {
                responseStream.write("HTTP/1.1 200 OK\r\n\r\nHello from GET /messages!".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        server.addHandler("POST", "/messages", (request, responseStream) -> {
            // TODO: обработка POST запроса
            try {
                responseStream.write("HTTP/1.1 200 OK\r\n\r\nMessage received!".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        server.start(9999);
    }
}








