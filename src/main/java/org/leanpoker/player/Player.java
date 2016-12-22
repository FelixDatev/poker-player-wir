package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {

    static final String VERSION = "1";

    public static int betRequest(JsonElement request) {
            JsonObject o = request.getAsJsonObject();
            JsonArray players = o.get("players").getAsJsonArray();
            JsonObject wir = players.get(o.get("in_action").getAsInt()).getAsJsonObject();

        JsonArray holeCards = wir.get("hole_cards").getAsJsonArray();
        JsonArray communityCards = o.get("community_cards").getAsJsonArray();

        //callRank();

        if ( isPair(holeCards) ) {
            return raise(o, 100);
        } else if (containsAce(holeCards) ) {
            return raise(o, 50);
        }

        return 0;
    }

    public static void showdown(JsonElement game) {

    }

    public static int fold() {
        return 0;
    }

    public static int raise(JsonObject o, int amount) {
        //     current_buy_in - players[in_action][bet] + minimum_raise
        JsonArray players = o.get("players").getAsJsonArray();
        JsonObject wir = players.get(o.get("in_action").getAsInt()).getAsJsonObject();

        return o.get("current_buy_in").getAsInt() - wir.get("bet").getAsInt() + o.get("minimum_raise").getAsInt() + amount;
    }

    public static void callRank() {

        try {

            URL url = new URL("http://rainman.leanpoker.org/rank");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            //String input = "{ \"snippet\": {\"playlistId\": \"WL\",\"resourceId\": {\"videoId\": \""+videoId+"\",\"kind\": \"youtube#video\"},\"position\": 0}}";
            String input = "";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public static boolean isPair(JsonArray holeCards) {
        Card card1 = new Card(holeCards.get(0).getAsJsonObject());
        Card card2 = new Card(holeCards.get(1).getAsJsonObject());

        if (card1.rank.equals(card2.rank)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean containsAce(JsonArray holeCards) {
        Card card1 = new Card(holeCards.get(0).getAsJsonObject());
        Card card2 = new Card(holeCards.get(1).getAsJsonObject());

        if (card1.rank.equals("A") || card2.rank.equals("A")) {
            return true;
        } else {
            return false;
        }
    }


}
