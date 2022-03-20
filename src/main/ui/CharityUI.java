package ui;

import model.Charity;

import javax.swing.*;
import java.awt.*;

/**
 * Represents user interface for charities.
 */
public class CharityUI extends JInternalFrame {
    private Charity theCharity;
    private String charityName;

    public CharityUI(Charity c, Component parent) {
        super(c.getName(), false, false, false, false);
        theCharity = c;
        charityName = c.getName();


    }

}

