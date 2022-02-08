package model;

public class Grant {

    // Represents a grant proposal

    private String grantName;
    private String status;
    private int amountGranted;
    private String projectTheme;

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: constructs a grant which includes the status, granted amount, description of the project
    //          & the area of focus (theme) of the project
    public Grant(String grantName, String status, int amountGranted, String projectTheme) {
        this.grantName = grantName;
        this.status = status; // REQUIRES "Awarded", "Pending", "Rejected"
        this.amountGranted = amountGranted;
        this.projectTheme = projectTheme;

    }

    public String getGrantName() {
        return grantName;
    }

    public String getStatus() {
        return status;
    }

    public int getAmountGranted() {
        return amountGranted;
    }

    public String getProjectTheme() {
        return projectTheme;
    }
}
