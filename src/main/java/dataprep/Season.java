package dataprep;

import lombok.Getter;
import org.json.simple.JSONArray;
import java.util.ArrayList;

@Getter
public class Season {

    private String seasonID;
    private JSONArray seasonJSONArray;
    private ArrayList<Match> seasonMatches = new ArrayList<>();

    public Season(String seasonID, JSONArray seasonJSONArray, ArrayList<Match> seasonMatches) {
        this.seasonID = seasonID;
        this.seasonJSONArray = seasonJSONArray;
        this.seasonMatches = seasonMatches;
    }
}
