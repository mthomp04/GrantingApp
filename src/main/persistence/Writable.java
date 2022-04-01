package persistence;

import org.json.JSONObject;

// Represents the writable interface
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();

}
