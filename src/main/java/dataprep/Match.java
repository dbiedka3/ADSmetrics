package dataprep;

import lombok.Getter;
import org.json.simple.JSONArray;

@Getter
public class Match {

    private String matchID;
    private JSONArray matchJSONArray;

    public Match(String matchID, JSONArray matchJSONObject) {
        this.matchID = matchID;
        this.matchJSONArray = matchJSONObject;
    }
}
