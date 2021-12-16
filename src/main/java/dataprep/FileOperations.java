package dataprep;

import analysis.EventCounter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class FileOperations {

    public static Object readFile(FileReader fileReader) throws IOException, ParseException {

        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(fileReader);

        return obj;

    }

    public static void saveCSVFILE(String path, ArrayList<EventCounter> ec) {

        StringBuilder sb = new StringBuilder();
        for (EventCounter e : ec) {
            sb.append(e.toString());
        }
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.write(sb.toString());
            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }


    }

}
