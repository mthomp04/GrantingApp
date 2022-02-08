package model;

import java.util.ArrayList;
import java.util.List;


// Represents an organization that has applied for funding
// and has a name and list of grants applied for
public class Charity {

    private String name;
    private List<Grant> grants;

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: constructs an organization with a given name
    //          and without any associated grants
    public Charity(String name) {
        this.name = name;
        grants = new ArrayList<>();

    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: adds a grant to an applying organization
    public static void addGrant(Grant grant, Charity charity) {
        charity.grants.add(grant);

    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: produces the total amount of funds awarded to a given charity
    public int totalFunded(Charity charity) {
        int result = 0;
        if (charity.grants.isEmpty()) {
            return 0;
        } else {
            for (Grant grants : charity.grants) {
                result += grants.getAmountGranted();
            }
        }
        return result;
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: looks through list of grants from organization and displays
    //          largest grant
    public int largestGrantReceived(Charity charity) {
        int max = 0;
        if (charity.grants.isEmpty()) {
            return 0;
        } else {
            for (Grant grants : charity.grants) {
                if (grants.getAmountGranted() > max) {
                    max = grants.getAmountGranted();
                }
            }
        }
        return max;
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: displays only grant that have been awarded
    public ArrayList<Grant> listAwardedGrants(Charity charity) {
        ArrayList<Grant> awardedGrants = new ArrayList<>();
        for (Grant grants : charity.grants) {
            if (grants.getStatus().equals("Awarded")) {
                awardedGrants.add(grants);
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