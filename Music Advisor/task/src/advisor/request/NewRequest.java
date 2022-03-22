package advisor.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class NewRequest extends Request {

    public NewRequest(String apiServer, String accessToken) {
        super(apiServer, accessToken);
        this.api_path = apiServer + "/v1/browse/new-releases";
        this.song_collection_type = "albums";
    }

    @Override
    public List<String> processResponse() {
        JsonArray items = getItemsJsonArray(response.body());

        for (int i = 0; i < items.size(); i++) {
            JsonObject currentAlbum = items.get(i).getAsJsonObject();

            String name = getValue(currentAlbum, "name");
            List<String> artistNames = getArtistNames(currentAlbum);
            String link = getValue(currentAlbum, "external_urls.spotify");

            StringBuilder sb = new StringBuilder();
            sb.append(name).append("\n")
                  .append(artistNames).append("\n")
                  .append(link).append("\n")
                  .append("\n");

            content.add(sb.toString());

        }

        return content;


    }

    private List<String> getArtistNames(JsonObject currentAlbum) {
        List<String> artistNames = new ArrayList<String>();
        JsonArray artists = currentAlbum.getAsJsonArray("artists");

        for (int i = 0; i < artists.size(); i++) {
            JsonObject currentArtist = artists.get(i).getAsJsonObject();
            String name = getValue(currentArtist, "name");

            artistNames.add(name);
        }

        return artistNames;
    }


}
