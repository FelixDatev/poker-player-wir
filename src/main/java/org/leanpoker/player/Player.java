package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class Player {

    static final String VERSION = "1";

    public static int betRequest(JsonElement request) {
        if (request.isJsonObject()) {
            JsonObject o = request.getAsJsonObject();
            JsonArray players = o.get("players").getAsJsonArray();
            for (JsonElement player: players ) {
                JsonObject playerObj = player.getAsJsonObject();
                if (playerObj.get("name").equals("WIR")) {
                    return 1;
                }
            }
        }

    }

    public static void showdown(JsonElement game) {
    }
}
