package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


public class Authorization {
    final String CLIENT_ID = "5588f3755e33431ebd4ff0b7e9fdbfe8";
    final String CLIENT_SECRET = "2a8631c9f268488babf950a813473fd5";
    final String REDIRECT_URI = "http://localhost:8080";
    final String GRANT_TYPE = "authorization_code";
    final String RESPONSE_TYPE = "code";

    String authorizationCode = null;
    String authorizationServer;

    public Authorization (String authorizationServer) {
        this.authorizationServer = authorizationServer;
    }

    public String authorize() throws IOException, InterruptedException {
        // request the authorization code first
        String authorizationCode = requestAuthorizationCode();

        // if the user accept the authorization, then make another request for accessTokne
        if (authorizationCode != null && !authorizationCode.isBlank()) {
            System.out.println("making http request for access_token...");
            String accessToken = requestAccessToken(authorizationCode);
            return accessToken;
        }

        return null;
    }

    private String requestAuthorizationCode() throws IOException, InterruptedException {


        String link = authorizationServer + "/authorize?" +
                "client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&response_type=" + RESPONSE_TYPE;

        System.out.println("use this link to request the access code:");
        System.out.println(link);

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/",
            exchange -> {
                String browserMessage;

                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.startsWith("code")) {
                    authorizationCode = query.substring(5);
                    browserMessage = "Got the code. Return back to your program.";
                    System.out.println("code received");
                } else {
                    authorizationCode = "";
                    browserMessage ="Authorization code not found. Try again.";
                }

                exchange.sendResponseHeaders(200, browserMessage.length());
                exchange.getResponseBody().write(browserMessage.getBytes(StandardCharsets.UTF_8));
                exchange.close();
                // server.stop(1); // comment this line when you run test of the stage on Hyperskill
            });

        server.start();

        // wait until server receive the response from Spotify before shutting it down
        while (authorizationCode == null) {
            Thread.sleep(100);
        }

        server.stop(1);

        return authorizationCode;
    }

    private String requestAccessToken(String authorizationCode) throws IOException, InterruptedException {

        System.out.println("making http request for access_token...");
        HttpClient client = HttpClient.newBuilder().build();

        // the body of the POST request
        String requestBody = "grant_type=" + GRANT_TYPE +
                "&client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&code=" + authorizationCode +
                "&redirect_uri=" + REDIRECT_URI;

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(authorizationServer + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response:");
        System.out.println(response.body());;
        System.out.println("---SUCCESS---");

        JsonObject responseJsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String accessToken = responseJsonObject.get("access_token").getAsString();
        return accessToken;

    }
}
