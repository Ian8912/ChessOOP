package Game;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Sends game results to the Chess REST API server.
 *
 * <p>The server location is configurable so the same client can talk to a local
 * dev server or a deployed one. Resolution order (first non-blank wins):</p>
 * <ol>
 *   <li>system property {@code -Dchess.server.url=...}</li>
 *   <li>environment variable {@code CHESS_SERVER_URL}</li>
 *   <li>default {@code http://localhost:8080}</li>
 * </ol>
 *
 * <p>If the server enforces an API key, supply it via {@code -Dchess.api.key=...}
 * or {@code CHESS_API_KEY}; it is sent in the {@code X-API-Key} header.</p>
 */
public class ServerClient {

    /** Base server URL (no trailing slash), resolved once at class load. */
    private static final String BASE_URL = resolveBaseUrl();

    /** Full results endpoint built from {@link #BASE_URL}. */
    private static final String RESULTS_ENDPOINT = BASE_URL + "/api/v1/results";

    /** Optional API key sent on write requests; empty when not configured. */
    private static final String API_KEY = firstNonBlank(
        System.getProperty("chess.api.key"), System.getenv("CHESS_API_KEY"), "");

    /**
     * Posts a game result to the server in the background.
     * Failures are silently ignored so the client never crashes if the server is offline.
     *
     * @param whiteName name of the white player
     * @param blackName name of the black player
     * @param winner    "WHITE" or "BLACK"
     */
    public static void postResult(String whiteName, String blackName, String winner) {
        String json = String.format(
            "{\"whiteName\":\"%s\",\"blackName\":\"%s\",\"winner\":\"%s\"}",
            whiteName, blackName, winner
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(RESULTS_ENDPOINT))
            .header("Content-Type", "application/json");
        if (!API_KEY.isBlank()) {
            builder.header("X-API-Key", API_KEY);
        }
        HttpRequest request = builder
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenAccept(response -> System.out.println("Server response: " + response.statusCode()))
            .exceptionally(ex -> {
                System.err.println("Could not reach server: " + ex.getMessage());
                return null;
            });
    }

    /** Resolves the base server URL from system property, env var, or the local default. */
    private static String resolveBaseUrl() {
        String url = firstNonBlank(
            System.getProperty("chess.server.url"), System.getenv("CHESS_SERVER_URL"),
            "http://localhost:8080");
        while (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /** Returns the first argument that is non-null and not blank. */
    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return "";
    }
}
