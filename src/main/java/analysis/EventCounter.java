package analysis;

import dataprep.Match;
import dataprep.Season;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Getter
public class EventCounter {

    private static String ALL_PLAYERS = "ALL"; //constant flag for whole season and each player

    private String player;
    private String eventType;
    private String subEventType;
    private Season season;
    private ArrayList<String> eventList = new ArrayList<>();
    private HashMap<String, Integer> eventsCount;
    private double score;


    public EventCounter(String player, String eventType, String subEventType, Season season) {
        this.player = player;
        this.eventType = eventType;
        this.subEventType = subEventType;
        this.season = season;
        countSeasonEvents();
        countUniqueEventsByKind();
        calculateScore();
    }

    public void countSeasonEvents() {
        ArrayList<Match> matches = this.season.getSeasonMatches();

        for (Match m : matches) {
            countMatchEvents(m);
        }

    }

    public void countMatchEvents(Match match) {

        JSONArray events = match.getMatchJSONArray();

        for (int i = 0; i < events.size(); i++) {
            JSONObject eventObj = (JSONObject) events.get(i);
            String eventPlayer = retrieveEventPlayer(eventObj);
            JSONObject seekedEvent = (JSONObject) eventObj.get(this.eventType);

            //eliminate penalties from seeked list
            if(seekedEvent!=null){
                JSONObject evType=(JSONObject) seekedEvent.get("type");
                if(evType.get("name").equals("Penalty")) seekedEvent=null;

            }



            //switch for all players
            if (this.player.equals(this.ALL_PLAYERS)) {
                eventPlayer = this.ALL_PLAYERS;
            }

            if (eventPlayer != null && eventPlayer.equals(this.player) && seekedEvent != null) {

                this.eventList.add(retrieveSeekedEventDetails(seekedEvent));

            }

        }

    }

    public String retrieveEventPlayer(JSONObject event) {
        String eventPlayer = null;

        if (event.get("player") != null) {
            JSONObject playerObj = (JSONObject) event.get("player");
            if (playerObj.get("name") != null) {

                eventPlayer = playerObj.get("name").toString();
            }
        }
        return eventPlayer;
    }

    public String retrieveSeekedEventDetails(JSONObject event) {

        JSONObject subEvent = (JSONObject) event.get(this.subEventType);
        String outcome = subEvent.get("name").toString();

        return outcome;
    }

    public void countUniqueEventsByKind() {
        String[] distinct = retrieveDistinctElements();
        HashMap<String, Integer> counted = new HashMap<>();

        for (int i = 0; i < distinct.length; i++) {
            String key = distinct[i];
            int count = 0;
            for (String s : this.eventList) {
                if (s.equals(key)) count++;
            }
            counted.put(key, count);

        }
        this.eventsCount = counted;

    }


    public String[] retrieveDistinctElements() {
        String[] distinct = Arrays.stream(this.eventList.toArray()).distinct().toArray(String[]::new);
        return distinct;
    }

    public void calculateScore() {
        Scorer scorer = new Scorer(this);
        this.score = scorer.getScore();

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.eventsCount.forEach((k, v) -> {

            sb.append(this.season.getSeasonID() + ",");
            sb.append(this.eventType + ",");
            sb.append(this.subEventType + ",");
            sb.append(k + ",");
            sb.append(this.player + ",");
            sb.append(v + ",");
            sb.append(this.score+"\n");

        });
        return sb.toString();
    }
}
