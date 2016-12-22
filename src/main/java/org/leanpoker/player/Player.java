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
            JsonObject wir = players.get(o.get("in_action").getAsInt()).getAsJsonObject();

        JsonArray holeCards = wir.get("hole_cards").getAsJsonArray();
        if ( isPair(holeCards) ) {
            return raise(o, 100);
        } else if (containsAce(holeCards) ) {
            //return raise(o, 50);
        }

        return raise(o, 0);
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
