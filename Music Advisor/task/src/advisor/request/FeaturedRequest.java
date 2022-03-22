package advisor.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class FeaturedRequest extends Request {

    public FeaturedRequest(String apiServer, String accessToken) {
        super(apiServer, accessToken);
        this.api_path = apiServer + "/v1/browse/featured-playlists";
        this.song_collection_type = "playlists";
    }


    @Override
    public List<String> processResponse() {
        JsonArray items = getItemsJsonArray(response.body());

        for (int i = 0; i < items.size(); i++) {
            JsonObject currentPlaylist = items.get(i).getAsJsonObject();

            String name = getValue(currentPlaylist, "name");
            String link = getValue(currentPlaylist, "external_urls.spotify");

            StringBuilder sb = new StringBuilder();
            sb.append(name).append("\n")
                  .append(link).append("\n")
                  .append("\n");

            content.add(sb.toString());
        }

        return content;
    }
}
