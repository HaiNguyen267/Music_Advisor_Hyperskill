package advisor.request;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;


public class PlaylistRequest extends Request{
    String playlistName;
    String playlistId;

    public PlaylistRequest(String apiServer, String accessToken, String playlistName) {

        super(apiServer, accessToken);
        this.song_collection_type = "playlists";
        this.playlistId = Request.map.get(playlistName);
///api.spotify.com/v1/browse/categories/{category_id}/playlists
        this.api_path = apiServer + "/v1/browse/categories/" + playlistId + "/playlists";

    }



    @Override
    public void sendRequest() throws IOException, InterruptedException {
        if (playlistId != null) {
            super.sendRequest();
        }
    }
    @Override
    public List<String> processResponse() {

        if (playlistId == null) {
            content.add("Unknown category name.");
        } else {
            JsonObject error = getError(response.body());

            if (error != null) {
                String errorMessage = error.get("message").getAsString();
                content.add(errorMessage);
            } else {
                JsonArray items = getItemsJsonArray(response.body());

                for (int i = 0; i < items.size(); i++) {
                    JsonObject currentPlaylist = items.get(i).getAsJsonObject();

                    String name = getValue(currentPlaylist, "name");
                    String link = getValue(currentPlaylist, "external_urls.spotify");;

                    StringBuilder sb = new StringBuilder();
                    sb.append(name).append("\n")
                            .append(link).append("\n")
                            .append("\n");

                    content.add(sb.toString());
                }
            }
        }
        return content;
    }

    private JsonObject getError(String jsonString) {
        JsonObject jo = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonObject errorJsonObject = jo.getAsJsonObject("error");
        return errorJsonObject;
    }


}
