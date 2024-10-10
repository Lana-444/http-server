package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HttpRequestTest {
    private HttpRequest request;

    @BeforeEach
    public void setUp() {
        String method = "GET";
        String path = "/messages?user=1&topic=java&topic=python";
        Map<String, String> headers = new HashMap<>();
        InputStream body = new ByteArrayInputStream(new byte[0]);
        request = new HttpRequest(method, path, headers, body);
    }

    @Test
    public void testGetMethod() {
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testGetPath() {
        assertEquals("/messages", request.getPath());
    }

    @Test
    public void testGetHeaders() {
        assertTrue(request.getHeaders().isEmpty());
    }

    @Test
    public void testGetQueryParams() {
        List<String> topics = request.getQueryParams("topic");
        assertNotNull(topics);
        assertEquals(2, topics.size());
        assertEquals("java", topics.get(0));
        assertEquals("python", topics.get(1));
    }


}