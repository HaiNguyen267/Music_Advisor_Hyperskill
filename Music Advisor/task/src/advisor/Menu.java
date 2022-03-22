package advisor;

import advisor.request.*;

import java.io.IOException;
import java.util.List;

public class Menu {
    String apiServer;
    String accessToken;

    public Menu(String apiServer, String accessToken) {
        this.apiServer = apiServer;
        this.accessToken = accessToken;
    }

    public void printMenu(String command, PageDisplayer pageDisplayer) throws IOException, InterruptedException {
        String playlistName = null;
        // if the command is playlist command
        if (command.startsWith("playlists")) {
            playlistName = command.substring(10);
            command = "playlists";
        }

        Request request = null;
        switch (command) {
            case "new":
                request = new NewRequest(apiServer, accessToken);
                break;
            case "featured":
                request = new FeaturedRequest(apiServer, accessToken);
                break;
            case "categories":
                request = new CategoryRequest(apiServer, accessToken);
                break;
            case "playlists":
                // at least 1 category request must be made before any playlist request is made;
                if (Request.map.isEmpty()) {
                    request = new CategoryRequest(apiServer, accessToken);
                    request.sendRequest();
                    request.processResponse();
                }
                request = new PlaylistRequest(apiServer, accessToken, playlistName);
                break;
        }

        request.sendRequest();
        List<String> content = request.processResponse();
        pageDisplayer.setPageContent(content);
        pageDisplayer.printCurrentPage();

    }


}
