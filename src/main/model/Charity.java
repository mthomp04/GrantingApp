package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// represents an organization that has applied for funding
// and has a name and list of grants applied for
public class Charity implements Writable {

    private String name;
    protected List<Grant> grants;

    // EFFECTS: constructs an organization with a given name
    //          and without any associated grants
    public Charity(String name) {
        this.name = name;
        grants = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a grant to an applying organization and adds event to log
    public void addGrant(Grant grant) {
        grants.add(grant);
        EventLog.getInstance().logEvent(new Event("Grant " + grant.getGrantName()
                + " was added to charity " + name));
    }

    // EFFECTS: produces the total amount of funds awarded to a given charity
    public int totalFunded() {
        int result = 0;
        if (grants.isEmpty()) {
            return 0;
        } else {
            for (Grant grants : grants) {
                if (grants.getStatus().equals(Grant.Status.AWARDED)) {
                    result += grants.getAmountGranted();
                }
            }
        }
        return result;
    }

    // EFFECTS: looks through list of grants from organization and displays
    //          largest grant
    public int largestGrantReceived() {
        int max = 0;
        if (grants.isEmpty()) {
            return 0;
        } else {
            for (Grant grants : grants) {
                if (grants.getAmountGranted() > max) {
                    max = grants.getAmountGranted();
                }
            }
        }
        return max;
    }

    // EFFECTS: displays a list of grants that have been awarded
    public ArrayList<String> listAwardedGrants() {
        ArrayList<String> awardedGrants = new ArrayList<>();
        for (Grant grants : grants) {
            if (grants.getStatus().equals(Grant.Status.AWARDED)) {
                awardedGrants.add(grants.getGrantName());
            }
        }
        return awardedGrants;
    }

    // MODIFIES: this
    // EFFECTS: removes a grant from the list of associated grant and adds event to log
    public void removeGrant(String grantName) {
        Iterator<Grant> itr = grants.iterator();

        while (itr.hasNext()) {
            Grant grant = itr.next();

            if (grant.getGrantName().equals(grantName)) {
                itr.remove();
                EventLog.getInstance().logEvent(new Event("Grant " + grant.getGrantName()
                        + " was removed from charity " + name));
            }
        }
    }

    // getters
    public List<Grant> getGrants() {
        return grants;
    }

    public String getName() {
        return name;
    }

    // JSON
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("grants", grants);
        return json;
    }

}