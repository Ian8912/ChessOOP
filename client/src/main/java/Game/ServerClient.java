package Game;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Sends game results to the Chess REST API server.
 */
public class ServerClient {

    private static final String SERVER_URL = "http://localhost:8080/api/v1/results";

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
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_URL))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenAccept(response -> System.out.println("Server response: " + response.statusCode()))
            .exceptionally(ex -> {
                System.err.println("Could not reach server: " + ex.getMessage());
                return null;
            });
    }
}