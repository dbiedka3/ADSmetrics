package dataprep;

import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

@Getter
public class PlayerList {

    private String seasonID;
    private HashSet<String> playerListAllDetails = new HashSet<>();
    private HashSet<String> playerListNames = new HashSet<>();

    public PlayerList(Season season) {
        createPlayersList(season);
    }

    public void createPlayersList(Season season) {

        this.seasonID = season.getSeasonID();

        ArrayList<Match> matches = season.getSeasonMatches();
        for (Match m : matches) {
            JSONArray matchArray = m.getMatchJSONArray();
            for (int i = 0; i < matchArray.size(); i++) {
                JSONObject p = (JSONObject) matchArray.get(i);
                if (p.get("player") != null) {
                    JSONObject p1 = (JSONObject) p.get("player");
                    JSONObject p2 = (JSONObject) p.get("position");
                    JSONObject p3 = (JSONObject) p.get("team");
                    String playerName = p1.get("name").toString();
                    String playerPos = p2.get("name").toString();
                    String playerTeam = p3.get("name").toString();
                    this.playerListAllDetails.add(playerName + "," + playerPos + "," + playerTeam);
                    this.playerListNames.add(playerName);
                }

            }

        }

    }

    public void printList() {

        this.playerListAllDetails.forEach(v -> {
            System.out.println(this.seasonID + "," + v);

        });
    }
}
