package persistence;

// Represents a reader that reads foundation from JSON data stored in file

import model.Charity;
import model.Foundation;
import model.Grant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonReader {
    private String source;

    //EFFECTS: constructs read to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Foundation read() throws IOException {
        Foundation foundation = new Foundation();
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseFoundation(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private  String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses foundation from JSON object and returns it
    private Foundation parseFoundation(JSONObject jsonObject) {
        Foundation foundation = new Foundation();
        addCharities(foundation, jsonObject);
        addGrantsFoundation(foundation, jsonObject);
        addFundsAvailable(foundation, jsonObject);
        return foundation;
    }

    private void addFundsAvailable(Foundation foundation, JSONObject jsonObject) {
        int fundsAvailable = jsonObject.getInt("fundsAvailable");
        foundation.addOrRemoveFunds(fundsAvailable);
    }

    //MODIFIES: foundation
    //EFFECTS: parses grants from JSON object and adds them to foundation
    private void addGrantsFoundation(Foundation foundation, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("grants");
        for (Object json : jsonArray) {
            JSONObject nextGrant = (JSONObject) json;
            addGrantFoundation(foundation, nextGrant);
        }
    }

    //MODIFIES: foundation
    //EFFECTS: parses grant from JSON object and adds them to foundation
    private void addGrantFoundation(Foundation foundation, JSONObject jsonObject) {
        String grantName = jsonObject.getString("grantName");
        Grant.Status status = Grant.Status.valueOf(jsonObject.getString("status"));
        int amountGranted = jsonObject.getInt("amountGranted");
        Grant grant = new Grant(grantName, status, amountGranted);
        foundation.addGrant(grant);
    }

    // MODIFIES: foundation
    // EFFECTS: parses charities from JSON object and adds them to foundation
    private void addCharities(Foundation foundation, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("charityList");
        for (Object json : jsonArray) {
            JSONObject nextCharity = (JSONObject) json;
            addCharityFoundation(foundation, nextCharity);
        }
    }

    // MODIFIES: foundation
    // EFFECTS: parses charity from JSON object and adds it to foundation
    private void addCharityFoundation(Foundation foundation, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Charity charity = new Charity(name);

        JSONArray jsonArray = jsonObject.getJSONArray("grants");
        for (Object json : jsonArray) {
            JSONObject nextGrant = (JSONObject) json;
            addGrant(charity, nextGrant);
        }

        foundation.addCharity(charity);
    }

    // MODIFIES: charity
    // EFFECTS: parses grants from JSON object and adds it to charity
    private void addGrantsCharity(Charity charity, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("grants");
        for (Object json : jsonArray) {
            JSONObject nextGrant = (JSONObject) json;
            addGrant(charity, nextGrant);
        }
    }

    // MODIFIES: charity
    // EFFECTS: parses grant from JSON object and adds it to charity
    private void addGrant(Charity charity, JSONObject jsonObject) {
        String name = jsonObject.getString("grantName");
        Grant.Status status = Grant.Status.valueOf(jsonObject.getString("status"));
        int amountGranted = jsonObject.getInt("amountGranted");
        Grant grant = new Grant(name, status, amountGranted);
        charity.addGrant(grant);
    }

}