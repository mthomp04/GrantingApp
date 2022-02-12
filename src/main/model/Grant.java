package model;

// represents a grant proposal
public class Grant {

    private String grantName;
    private String status;
    private int amountGranted;

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: constructs a grant which includes the status, granted amount, description of the project
    //          & the area of focus (theme) of the project
    public Grant(String grantName, String status, int amountGranted) {
        this.grantName = grantName;
        this.status = status; // REQUIRES "Awarded", "Pending", "Rejected"
        this.amountGranted = amountGranted;
//        updateFundAmount(grantName, status, amountGranted);
    }

//    public int updateFundAmount(String grantName, String status, int amountGranted) {
//        if (status == "Awarded") {
//            return fundsAvailable = fundsAvailable - amountGranted;
//        }
//        return fundsAvailable;
//    }

    public String getGrantName() {
        return grantName;
    }

    public String getStatus() {
        return status;
    }

    public int getAmountGranted() {
        return amountGranted;
    }

}
