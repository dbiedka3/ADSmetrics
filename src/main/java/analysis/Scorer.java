package analysis;

import lombok.Getter;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class Scorer {

    private double score;

    private static double BLOCKED = 0.2;
    private static double GOAL = 2;
    private static double OFF_T = -0.2;
    private static double POST = 1;
    private static double SAVED = 0.5;
    private static double SAVED_OFF_TARGET = 0;
    private static double SAVED_TO_POST = 0.8;
    private static double WAYWARD = -0.5;

    public Scorer(EventCounter eventCounter) {
        calculateScore(eventCounter);
    }


    private void calculateScore(EventCounter ec) {


        HashMap<String, Integer> events = ec.getEventsCount();
        AtomicReference<Double> s = new AtomicReference<>((double) 0);
        double sumV = events.values().stream().mapToDouble(d -> d).sum();
        if (sumV != 0) {
            events.forEach((k, v) -> {
                String event = k.replaceAll(" ", "_").toUpperCase();

                switch (event) {
                    case "BLOCKED":
                        s.updateAndGet(v1 -> new Double((double) (v1 + (v / sumV) * BLOCKED)));
                        break;
                    case "GOAL":
                        s.updateAndGet(v1 -> new Double((double) (v1 + (v / sumV) * GOAL)));
                        break;
                    case "OFF_T":
                        s.updateAndGet(v1 -> new Double((double) (v1 + (v / sumV) * OFF_T)));
                        break;
                    case "POST":
                        s.updateAndGet(v1 -> new Double((double) (v1 + (v / sumV) * POST)));
                        break;
                    case "SAVED":
                        s.updateAndGet(v1 -> new Double((double) (v1 + (v / sumV) * SAVED)));
                        break;
                    case "SAVED_OFF_TARGET":
                        s.updateAndGet(v1 -> new Double((double) (v1 + (v / sumV) * SAVED_OFF_TARGET)));
                        break;
                    case "SAVED_TO_POST":
                        s.updateAndGet(v1 -> new Double((double) (v1 + (v / sumV) * SAVED_TO_POST)));
                        break;
                    case "WAYWARD":
                        s.updateAndGet(v1 -> new Double((double) (v1 + (v / sumV) * WAYWARD)));
                        break;

                }


            });
            this.score = s.get();
        }

    }
}
