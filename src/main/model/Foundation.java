package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// represents a foundation which has a list of charities to which it can give funds
// includes a total amount of funds available to be distributed to charities through grants
public class Foundation implements Writable {
    private List<Charity> charityList;
    private List<Grant> grants;
    protected int fundsAvailable;

    public Foundation() {
        charityList = new ArrayList<>();
        grants = new ArrayList<>();
        fundsAvailable = 0;
    }

    // MODIFIES: this
    // EFFECTS: adds charity to the foundation
    public void addCharity(Charity charity) {
        charityList.add(charity);
    }

    // MODIFIES: this
    // EFFECTS: removes charity to the foundation
    public void removeCharity(Charity charity) {
        charityList.remove(charity);
    }

    // REQUIRES: amount granted > 0 & there are sufficient funds in foundation
    // MODIFIED: this
    // EFFECTS: removes amount granted to organization from total funds available
    public int addGrant(Grant grant) {
        if (grant.getStatus().equals(Grant.Status.AWARDED)) {
            fundsAvailable = fundsAvailable - grant.getAmountGranted();
        }
        return fundsAvailable;
    }

    // MODIFIES: this
    // EFFECTS: Adds or removes amount to the total funds available to grant by the foundation
    public int addOrRemoveFunds(int amount) {
        if (amount >= 0) {
            return addFunds(amount);
        }
        return removeFunds(amount);
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: adds amount to the total funds available to grant by the foundation
    public int addFunds(int amount) {
        return fundsAvailable = fundsAvailable + amount;
    }

    // REQUIRES: amount < 0
    // MODIFIES: this
    // EFFECTS: removes amount to the total funds available to grant by the foundation
    public int removeFunds(int amount) {
        amount = amount * -1;
        return fundsAvailable = fundsAvailable - amount;
    }

    // EFFECTS: displays list of charities and the total amount of funds each has received
    public ArrayList<String> listOfCharitiesAndAmountFunded() {
        ArrayList<String> charities = new ArrayList<>();
        for (Charity charity : charityList) {
            charities.add(charity.getName() + " has received $" + charity.totalFunded());
        }
        return charities;
    }

    // getters
    public List<Charity> getCharityList() {
        return charityList;
    }

    public List<Grant> getGrants() {
        return grants;
    }

    public int getFundsAvailable() {
        return fundsAvailable;
    }

    // JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("charityList", charitiesToJson());
        json.put("grants", grantsToJson());
        json.put("fundsAvailable", fundsAvailable);
        return json;
    }

    // EFFECTS: returns charities in this foundation as a JSON array
    private JSONArray charitiesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Charity c : charityList) {
            jsonArray.put(c.toJson());
        }

        return  jsonArray;
    }

    // EFFECTS: returns grants in this foundation as a JSON array
    private JSONArray grantsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Grant g : grants) {
            jsonArray.put(g.toJson());
        }

        return  jsonArray;
    }
}