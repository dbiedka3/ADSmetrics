package dataprep;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Getter
@Setter
public class SeasonFactory {

    private String seasonFilePath;
    private String matchFilesPath;
    private String seasonID;
    private JSONArray seasonJSONArray;
    private ArrayList<Match> seasonMatches = new ArrayList<>();

    private Season season;



    public SeasonFactory(String seasonID, String seasonFilePath, String matchFilesPath) throws IOException, ParseException {
        this.seasonFilePath = seasonFilePath;
        this.matchFilesPath = matchFilesPath;
        this.seasonID = seasonID;
        createSeasonMatchList();
        createSeasonMatchesArray(retrieveMatchesID());
        createSeasonObject();

    }

    public void createSeasonObject() {
        this.season = new Season(this.seasonID, this.seasonJSONArray, this.seasonMatches);

    }


    public void createSeasonMatchList() throws IOException, ParseException {
        FileReader fileReader = new FileReader(this.seasonFilePath);
        Object jsonObject = FileOperations.readFile(fileReader);
        this.seasonJSONArray = (JSONArray) jsonObject;


    }


    public ArrayList<String> retrieveMatchesID() {
        ArrayList<String> matches = new ArrayList<>();

        for (int i = 0; i < this.seasonJSONArray.size(); i++) {
            JSONObject temp = (JSONObject) seasonJSONArray.get(i);
            matches.add((String) temp.get("match_id").toString());
        }
        return matches;
    }

    private void createSeasonMatchesArray(ArrayList<String> matchIDList) throws IOException, ParseException {
        for (String s : matchIDList) {
            this.seasonMatches.add(readMatchJSON(s));

        }

    }

    private Match readMatchJSON(String matchID) throws IOException, ParseException {
        String finalPath = this.matchFilesPath + matchID + ".json";
        FileReader fileReader = new FileReader(finalPath);
        Object jsonObject = FileOperations.readFile(fileReader);
        Match match = new Match(matchID, (JSONArray) jsonObject);
        return match;
    }


}
