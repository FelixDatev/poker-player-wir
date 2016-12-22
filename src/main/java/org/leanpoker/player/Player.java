package org.leanpoker.player;

import com.google.gson.*;
import jdk.nashorn.internal.runtime.URIUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
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



        if (communityCards.size() > 2) {
            int rank = callRank(holeCards, communityCards);

            return raise(o, 5*rank);
        } else {
            if (isPair(holeCards)) {
                return raise(o, 5);
            } else if (containsAce(holeCards)) {
                return raise(o, 2);
            } else if (isOneCardHigherThanNine(holeCards)) {
                return 0;
            }
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

        return o.get("current_buy_in").getAsInt() - wir.get("bet").getAsInt() + o.get("minimum_raise").getAsInt() + (amount * o.get("small_blind").getAsInt());
    }

    public static int callRank(JsonArray holeCards, JsonArray communityCards) {

        try {

            //INPUT:
            List<Card> cards = new ArrayList<Card>();

            cards.add(new Card(holeCards.get(0).getAsJsonObject()));
            cards.add(new Card(holeCards.get(1).getAsJsonObject()));

            for (JsonElement ccard : communityCards ) {
                cards.add(new Card(ccard.getAsJsonObject()));
            }

            if (cards.size() > 2) {
                //String input = "{ \"snippet\": {\"playlistId\": \"WL\",\"resourceId\": {\"videoId\": \""+videoId+"\",\"kind\": \"youtube#video\"},\"position\": 0}}";
                String input = "[";

                String separator = "";

                for (Card card : cards) {
                    String i = String.format("%s{\"rank\":\"%s\",\"suit\":\"%s\"}", separator, card.rank, card.suit);
                    input = input.concat(i);
                    separator = ",";
                }


                input = input.concat("]");
                input = URLEncoder.encode(input, "UTF-8");

                String urlString = "http://rainman.leanpoker.org/rank?cards=".concat(input);


                System.out.println(urlString);


                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");


                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                StringBuilder sb = new StringBuilder();

                String output;
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                    sb.append(output);
                }

                conn.disconnect();



                JsonParser parser = new JsonParser();
                    JsonObject out2 = parser.parse(sb.toString()).getAsJsonObject();
                    return out2.get("rank").getAsInt();

            }

            return 0;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 0;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;

        } catch (NullPointerException e)  {
            e.printStackTrace();
            return 0;
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

    public static boolean isOneCardHigherThanNine(JsonArray holeCards) {
        Card card1 = new Card(holeCards.get(0).getAsJsonObject());
        Card card2 = new Card(holeCards.get(1).getAsJsonObject());



        if (card1.rank.equals("10") || card1.rank.equals("K") || card1.rank.equals("J") || card1.rank.equals("Q") || card1.rank.equals("A") || card2.rank.equals("10") || card2.rank.equals("K") || card2.rank.equals("J") || card2.rank.equals("Q") || card2.rank.equals("A")) {
            return false;
        } else {
            return true;
        }
    }


}
