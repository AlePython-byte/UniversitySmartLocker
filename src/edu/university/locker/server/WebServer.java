package edu.university.locker.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.OperationResult;
import edu.university.locker.service.JsonResponseBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebServer {

    private final SmartLocker smartLocker;
    private final HttpServer server;

    public WebServer(SmartLocker smartLocker, int port) throws IOException {
        this.smartLocker = smartLocker;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.setExecutor(Executors.newCachedThreadPool());
        configureContexts();
    }

    public void start() {
        server.start();
    }

    private void configureContexts() {
        server.createContext("/", this::handleIndex);
        server.createContext("/api/locker/status", exchange -> handleGet(exchange, smartLocker::getStatus));
        server.createContext("/api/locker/reserve", exchange -> handlePostWithBody(exchange, "studentName", smartLocker::reserve));
        server.createContext("/api/locker/store", exchange -> handlePostWithBody(exchange, "packageCode", smartLocker::storePackage));
        server.createContext("/api/locker/ready", exchange -> handlePost(exchange, smartLocker::markReadyForPickup));
        server.createContext("/api/locker/pickup", exchange -> handlePostWithBody(exchange, "securityCode", smartLocker::pickupPackage));
        server.createContext("/api/locker/overdue", exchange -> handlePost(exchange, smartLocker::markAsOverdue));
        server.createContext("/api/locker/maintenance/start", exchange -> handlePost(exchange, smartLocker::startMaintenance));
        server.createContext("/api/locker/maintenance/finish", exchange -> handlePost(exchange, smartLocker::finishMaintenance));
    }

    private void handleIndex(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, JsonResponseBuilder.buildBasicResponse(false, "Método no permitido."));
            return;
        }

        String path = exchange.getRequestURI().getPath();
        if (!"/".equals(path) && !"/index.html".equals(path)) {
            sendTextResponse(exchange, 404, "Ruta no encontrada.", "text/plain; charset=utf-8");
            return;
        }

        Path indexPath = Path.of("frontend", "index.html");
        if (!Files.exists(indexPath)) {
            sendTextResponse(exchange, 500, "No se encontró el archivo frontend/index.html.", "text/plain; charset=utf-8");
            return;
        }

        String html = Files.readString(indexPath, StandardCharsets.UTF_8);
        sendTextResponse(exchange, 200, html, "text/html; charset=utf-8");
    }

    private void handleGet(HttpExchange exchange, Supplier<OperationResult> action) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, JsonResponseBuilder.buildBasicResponse(false, "Método no permitido."));
            return;
        }
        sendLockerResponse(exchange, action.get());
    }

    private void handlePost(HttpExchange exchange, Supplier<OperationResult> action) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, JsonResponseBuilder.buildBasicResponse(false, "Método no permitido."));
            return;
        }
        sendLockerResponse(exchange, action.get());
    }

    private void handlePostWithBody(HttpExchange exchange, String fieldName, Function<String, OperationResult> action) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, JsonResponseBuilder.buildBasicResponse(false, "Método no permitido."));
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String value = extractJsonValue(body, fieldName);
        sendLockerResponse(exchange, action.apply(value));
    }

    private void sendLockerResponse(HttpExchange exchange, OperationResult result) throws IOException {
        String responseBody = JsonResponseBuilder.buildLockerResponse(result, smartLocker);
        sendJsonResponse(exchange, 200, responseBody);
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        sendTextResponse(exchange, statusCode, responseBody, "application/json; charset=utf-8");
    }

    private void sendTextResponse(HttpExchange exchange, int statusCode, String responseBody, String contentType) throws IOException {
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private String extractJsonValue(String body, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*\"((?:\\\\.|[^\"])*)\"");
        Matcher matcher = pattern.matcher(body);
        if (!matcher.find()) {
            return "";
        }
        return unescapeJson(matcher.group(1));
    }

    private String unescapeJson(String value) {
        return value
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }
}
