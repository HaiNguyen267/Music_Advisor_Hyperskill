package advisor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {


    //{"access_token":"BQC7M_qAjMVHRPky7Sneqt_CxfRJhe9i7dpPeSn1LyMSpSTuo8EmXw0LW7-YwaEJnAihbttR9jj6IcyPaygzAxbgGDBEDpZ8aAQmUmExfHQDi3xrob1nP7HZ-sLBLjRBAtrPLcaGRKxEMCd204VCoLvgjJON2qjb5CvTyA","token_type":"Bearer","expires_in":3600,"refresh_token":"AQBcYAy4lM3hQ-BwTm_TfXiaoIIcvXNsWXSgO_nbHjDjhn8p-RY1iO6-4m7Ka9KFdvEd7jvnjJYybkjIWivxOYi844XG_gvit1itvcNSRAZ-FblA4IC6GyDmxdYhjauHEfs"}
    static String authorizationServer = "https://accounts.spotify.com";
    static String apiServer = "https://api.spotify.com";
    static int entriesPerPage = 5;
    public static String accessToken;

    public static void main(String[] args) throws IOException, InterruptedException {

        getEndPointsFromCommandLine(args);

        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        // ask user to authorize before executing user command
        while (true) {
            if (command.equals("auth")) {
                // user authorizes
                userAuthorize();
                // if the user accept the authorization
                if (accessToken != null) {
                    break;
                }
            } else {
                System.out.println("Please, provide access for application.");
            }
            command = sc.nextLine();
        }

        boolean exit = false;
        Menu menu = new Menu(apiServer, accessToken);
        PageDisplayer pageDisplayer = new PageDisplayer(entriesPerPage);
        // executing user commands
        while (!exit) {
            command = sc.nextLine();

            switch (command) {
                case "auth":
                    userAuthorize();;
                    break;
                case "exit":
                    System.out.println("Goodbye!");
                    //exit = true;// comment this line when you run test of the stage on Hyperskill
                    break;
                case "prev":
                    pageDisplayer.printPreviousPage();
                    break;
                case "next":
                    pageDisplayer.printNextPage();
                    break;
                default:
                    menu.printMenu(command, pageDisplayer);
                    break;
            }
        }
    }

    private static void getEndPointsFromCommandLine(String[] args) {

        List<String> cmdArgs = Arrays.asList(args);

        if (cmdArgs.contains("-access")) {
            authorizationServer = cmdArgs.get(cmdArgs.indexOf("-access") + 1);
        }

        if (cmdArgs.contains("-resource")) {
            apiServer = cmdArgs.get(cmdArgs.indexOf("-resource") + 1);
        }
        
        if (cmdArgs.contains("-page")) {
            String str = cmdArgs.get(cmdArgs.indexOf("-page") + 1);
            entriesPerPage = Integer.parseInt(str);
        }
    }

    private static void userAuthorize() throws IOException, InterruptedException {
        Authorization authorization = new Authorization(authorizationServer);

        String receivedAccessToken = authorization.authorize();
        // if the user accept the authorization, an accessToken is return, otherwise null value is return
        if (receivedAccessToken != null) {
            accessToken = receivedAccessToken;
        }
    }

}
