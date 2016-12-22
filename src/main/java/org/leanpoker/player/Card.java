package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by felixfischer on 22.12.16.
 */
public class Card {
    public String rank;
    public String suit;

    public Card(JsonObject card) {
        rank = card.get("rank").getAsString();
        suit = card.get("suit").getAsString();
    }
}
