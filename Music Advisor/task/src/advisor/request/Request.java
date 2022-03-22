package advisor.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Request {
    public static Map<String, String> map = new HashMap<String, String>();

    protected HttpClient client;
    protected HttpRequest request;
    protected HttpResponse<String> response;

    protected String song_collection_type; // it can be album, playlists, categories
    protected String apiServer;
    protected String accessToken;
    protected String api_path;
    protected List<String> content;
    public Request(String apiServer, String accessToken) {
        this.apiServer = apiServer;
        this.accessToken = accessToken;
        this.client = HttpClient.newBuilder().build();
        this.content = new ArrayList<>();
    }


    public void sendRequest() throws IOException, InterruptedException {
        request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(api_path))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public abstract List<String> processResponse();

    public void printRequest() {
        System.out.println(content);
    }

    public void processAndPrintResponse() {
        processResponse();
        printRequest();
    }


    public JsonArray getItemsJsonArray(String jsonString) {
        JsonObject jo = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonArray items = jo.getAsJsonObject(song_collection_type).getAsJsonArray("items");
        return items;
    }

    public String getValue(JsonObject jo, String key) {
        String[] keyArray = key.split("\\.");
        for (int i = 0; i < keyArray.length - 1; i++) {
            jo = jo.getAsJsonObject(keyArray[i]);
            if (jo == null) {
                return null;
            }
        }

        String lastKey = keyArray[keyArray.length - 1];
        String desiredValue = jo.get(lastKey).getAsString();
        return desiredValue;
    }
}
