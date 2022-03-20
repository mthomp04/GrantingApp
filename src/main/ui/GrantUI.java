package ui;


import model.Grant;

import javax.swing.*;
import java.awt.*;

/**
 * Represents user interface for grants.
 */
public class GrantUI extends JInternalFrame {
    private Grant theGrant;
    private String grantName;
    private Grant.Status status;
    private int amountGranted;

    public GrantUI(Grant g, Component parent) {
        super(g.getGrantName(), false, false, false, false);
        theGrant = g;
        grantName = g.getGrantName();
        status = g.getStatus();
        amountGranted = g.getAmountGranted();
    }
}