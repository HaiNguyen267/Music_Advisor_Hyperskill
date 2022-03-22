package advisor.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class CategoryRequest extends Request {

    public CategoryRequest(String apiServer, String accessToken) {
        super(apiServer, accessToken);
        this.api_path = apiServer + "/v1/browse/categories";
        this.song_collection_type = "categories";
    }

    @Override
    public List<String> processResponse() {
        JsonArray items = getItemsJsonArray(response.body());
        for (int i = 0; i < items.size(); i++) {
            JsonObject currentCategory = items.get(i).getAsJsonObject();

            String categoryName = getValue(currentCategory, "name");
            String categoryId = getValue(currentCategory, "id");

            Request.map.put(categoryName, categoryId);// store the category id and category name to used later for playlist request
            content.add(categoryName);
        }
        return content;
    }



}
