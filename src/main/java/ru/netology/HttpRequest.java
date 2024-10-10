package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final InputStream body;
    private final Map<String, List<String>> queryParams;

    public HttpRequest(String method, String path, Map<String, String> headers, InputStream body) {
        this.method = method;
        this.path = splitPath(path);
        this.headers = new HashMap<>(headers);
        this.body = body;
        this.queryParams = parseQueryParams(path);
    }

    private String splitPath(String path) {
        int index = path.indexOf("?");
        return (index == -1) ? path : path.substring(0, index);
    }

    private Map<String, List<String>> parseQueryParams(String path) {
        int index = path.indexOf("?");
        String query = (index == -1) ? "" : path.substring(index + 1);
        List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
        Map<String, List<String>> params = new HashMap<>();

        for (NameValuePair pair : nameValuePairs) {
            params.computeIfAbsent(pair.getName(), k -> new ArrayList<>()).add(pair.getValue());
        }
        return params;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public InputStream getBody() {
        return body;
    }

    public List<String> getQueryParams(String name) {
        return queryParams.get(name);
    }

    public Map<String, List<String>> getAllQueryParams() {
        return queryParams;
    }
}
