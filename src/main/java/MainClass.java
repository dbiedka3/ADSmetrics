import analysis.EventCounter;
import dataprep.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainClass {


    public static void main(String[] args) throws IOException, ParseException {

        String pathToDatasetFile = "src/main/resources/dataset.json";
        JSONObject ds = (JSONObject) FileOperations.readFile(new FileReader(pathToDatasetFile));

        String seasonPath = ds.get("seasonsPath").toString();
        String eventsPath = ds.get("eventsPath").toString();
        String eventType = ds.get("eventType").toString();
        String eventSubType = ds.get("eventSubType").toString();
        HashMap<String, String> sf = new HashMap<>();
        ArrayList<String> players = new ArrayList<>();

        //get season files
        JSONArray ja = (JSONArray) ds.get("seasonFiles");

        for (int i = 0; i < ja.size(); i++) {
            JSONObject temp = (JSONObject) ja.get(i);
            String k = temp.get("season").toString();
            String v = temp.get("file").toString();
            sf.put(k, v);
        }

        //get player names
//        JSONArray pl = (JSONArray) ds.get("players");
//        for (int i = 0; i < pl.size(); i++) {
//            players.add(pl.get(i).toString());
//        }

        //Create List of Season objects
        ArrayList<Season> seasons = new ArrayList<>();

        sf.forEach((k, v) -> {
            try {
                SeasonFactory temp = new SeasonFactory(k, seasonPath + v, eventsPath);
                seasons.add(temp.getSeason());

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        //prepare print list of players
        String[] plrs = getDistinctPlayerNames(createPlayersList(seasons));
        for (int i = 0; i < plrs.length; i++) {
            players.add(plrs[i]);
        }
        players.add("ALL"); //for entire season

        //create list of event counters
        ArrayList<EventCounter> eventCounters = new ArrayList<>();
        for (Season s : seasons) {
            for (String p : players) {
                EventCounter ecTemp = new EventCounter(p, eventType, eventSubType, s);
                eventCounters.add(ecTemp);
                //System.out.println(ecTemp);
            }
        }

        FileOperations.saveCSVFILE("src/main/resources/measure_no_penalties.csv", eventCounters);

        System.out.println("done");
    }

    public static ArrayList<PlayerList> createPlayersList(ArrayList<Season> seasons) {

        ArrayList<PlayerList> playerLists = new ArrayList<>();

        for (Season s : seasons) {

            PlayerList temp = new PlayerList(s);
            playerLists.add(temp);
            //temp.printList();
        }
        return playerLists;
    }

    public static String[] getDistinctPlayerNames(ArrayList<PlayerList> pl) {

        ArrayList<String> allSeasons = new ArrayList<>();

        for (PlayerList p : pl) {
            allSeasons.addAll(p.getPlayerListNames());
        }

        String[] distinctPlayers = Arrays.asList(allSeasons.toArray()).stream().distinct().toArray(String[]::new);

        return distinctPlayers;
    }
}
