package ru.netology;


import java.io.BufferedOutputStream;
import java.io.IOException;

public interface Handler {
    void handle (HttpRequest httpRequest, BufferedOutputStream responseStream) throws IOException;
}
