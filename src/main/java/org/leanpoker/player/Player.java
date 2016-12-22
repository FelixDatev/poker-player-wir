package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class Player {

    static final String VERSION = "1";

    public static int betRequest(JsonElement request) {
            JsonObject o = request.getAsJsonObject();
            JsonArray players = o.get("players").getAsJsonArray();
            for (JsonElement player: players ) {
                JsonObject playerObj = player.getAsJsonObject();
                System.out.print("HOLAXXX");
                if (playerObj.get("name").getAsString().equals("WIR")) {
                    JsonArray holeCards = playerObj.get("hole_cards").getAsJsonArray();
                    if ( isPair(holeCards) ) {
                        return o.get("small_blind").getAsInt() * 100;
                    }
                }
            }
        return o.get("small_blind").getAsInt() * 2;
    }

    public static void showdown(JsonElement game) {
    }

    public static boolean isPair(JsonArray holeCards) {

        Card card1 = new Card(holeCards.get(0).getAsJsonObject());
        Card card2 = new Card(holeCards.get(1).getAsJsonObject());

        if (card1.rank == card2.rank) {
            return true;
        } else {
            return false;
        }
    }
}
