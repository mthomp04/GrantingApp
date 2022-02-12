package model;

import java.util.ArrayList;
import java.util.List;

// represents an organization that has applied for funding
// and has a name and list of grants applied for
public class Charity {

    private String name;
    protected List<Grant> grants;

    // EFFECTS: constructs an organization with a given name
    //          and without any associated grants
    public Charity(String name) {
        this.name = name;
        grants = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a grant to an applying organization
    public void addGrant(Grant grant) {
        grants.add(grant);

    }

    // EFFECTS: produces the total amount of funds awarded to a given charity
    public int totalFunded() {
        int result = 0;
        if (grants.isEmpty()) {
            return 0;
        } else {
            for (Grant grants : grants) {
                result += grants.getAmountGranted();
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
            if (grants.getStatus().equals("Awarded")) {
                awardedGrants.add(grants.getGrantName());
            }
        }
        return awardedGrants;
    }

    public List<Grant> getGrants() {
        return grants;
    }

    public String getName() {
        return name;
    }


}