package model;

import org.json.JSONObject;

// represents a grant proposal
public class Grant {

    public enum Status {
        AWARDED,
        REJECTED
    }

    private String grantName;
    private Status status;
    private int amountGranted;

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: constructs a grant which includes the status, granted amount, description of the project
    //          & the area of focus (theme) of the project
    public Grant(String grantName, Status status, int amountGranted) {
        this.grantName = grantName;
        this.status = status;
        this.amountGranted = amountGranted;
    }

    public String getGrantName() {
        return grantName;
    }

    public int getAmountGranted() {
        return amountGranted;
    }

    public Status getStatus() {
        return status;
    }

    // JSON
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("grantName", grantName);
        json.put("status", status);
        json.put("amountGranted", amountGranted);
        return json;
    }
}
