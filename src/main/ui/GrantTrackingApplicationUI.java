package ui;

import model.Charity;
import model.Foundation;
import model.Grant;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Represents application's main window frame.
 */
class GrantTrackingApplicationUI extends JFrame implements ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String JSON_STORE = "./data/workroom.json";

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private CharityTable charityTableModel;
    private GrantTable grantTableModel;
    private Foundation foundation;
    private JDesktopPane desktop;
    private Grant grant;
    private JTable charityTable;
    private JTable grantTable;
    private JScrollPane charityWindowSP;
    private JScrollPane grantWindowSP;
    private JInternalFrame charityWindow;
    private JInternalFrame grantWindow;
    private JPanel grantPanel;
    Charity charity;
    private int rowIndex = 0;

    public GrantTrackingApplicationUI() {

        try {
            // Check if Nimbus is supported and get its classname
            for (UIManager.LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(lafInfo.getName())) {
                    UIManager.setLookAndFeel(lafInfo.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                // If Nimbus is not available, set to the default Java (metal) look and feel
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        UIManager.put("nimbusBase", Color.white);

        ImageIcon icon = new ImageIcon("/src/Images/ubc.png");
        Image image = icon.getImage();

        desktop = new JDesktopPane();
//        {
//            public void paintComponent(Graphics g) {
//                Graphics2D g2d = (Graphics2D) g;
//                g.drawImage(image, 0, 0, WIDTH, HEIGHT,charityWindow);
//
//            }
//        };

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

//        class ImageDesktopPane extends JDesktopPane {
//        protected void paintComponent(Graphics g) {
//            g.drawImage(image);
//        }

        desktop.addMouseListener(new DesktopFocusAction());
        setContentPane(desktop);
        setTitle("Grant Tracking Application");
        setSize(WIDTH, HEIGHT);
        addMenu();

        grantWindow = new JInternalFrame("Grants", true, true, true, false);
        grantWindow.setPreferredSize(new Dimension(WIDTH - 20, HEIGHT / 4));
        grantWindow.setLayout(new FlowLayout(WIDTH, 0, 0));
        grantWindow.setLocation(0, HEIGHT / 2);
        grantTableModel = new GrantTable();
        grantTable = new JTable(grantTableModel);
        grantTable.setPreferredScrollableViewportSize(new Dimension(WIDTH - 200, HEIGHT / 3));
        grantTable.setFillsViewportHeight(true);

        grantWindowSP = new JScrollPane(grantTable);
        grantWindow.add(grantTable);
        grantWindow.add(grantWindowSP);
        grantWindow.pack();
        desktop.add(grantWindow);
        grantWindow.setVisible(true);

        charityWindow = new JInternalFrame("Charities", true, true, true, false);

        charityWindow.setPreferredSize(new Dimension(WIDTH - 20, HEIGHT / 2));
        charityWindow.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        charityTableModel = new CharityTable();
        charityTable = new JTable(charityTableModel);
        charityTable.setPreferredScrollableViewportSize(new Dimension(WIDTH - 200, HEIGHT / 2));
        charityTable.setFillsViewportHeight(true);
        charityTable.addMouseListener(new CharityTableListener());

        charityWindowSP = new JScrollPane(charityTable);
        charityWindow.add(charityTable);
        charityWindow.add(charityWindowSP);
        charityWindow.pack();
        desktop.add(charityWindow);
        charityWindow.setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centreOnScreen();
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }


    class CharityTable extends AbstractTableModel {
        ArrayList<String> cols = new ArrayList<String>();

        public CharityTable() {
            foundation = new Foundation();
            cols.add("Charity");
            cols.add("Amount Awarded");
        }

        @Override
        public Object getValueAt(int r, int c) {
            if (c == 0) {
                return foundation.getCharityList().get(r).getName();
            } else {
                return foundation.getCharityList().get(r).totalFunded();
            }
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return foundation.getCharityList().size();
        }

        @Override
        public String getColumnName(int c) {
            return cols.get(c);
        }

        public void setValueAt(Object value, int r, int c) {
            fireTableCellUpdated(r, c);
        }

        public boolean isCellEditable(int r, int c) {
            return false;
        }
    }


    class GrantTable extends AbstractTableModel {
        ArrayList<String> cols = new ArrayList<String>();

        public GrantTable() {
            cols.add("Name");
            cols.add("Status");
            cols.add("Amount Awarded");
        }

        @Override
        public Object getValueAt(int r, int c) {
            switch (c) {
                case (0): {
                    if (!foundation.getCharityList().isEmpty()) {
                        return charity.getGrants().get(r).getGrantName();
                    } else {
                        return 0;
                    }
                }
                case (1): {
                    if (!foundation.getCharityList().isEmpty()) {
                        return charity.getGrants().get(r).getStatus();
                    } else {
                        return 0;
                    }
                }
                case (2): {
                    if (!foundation.getCharityList().isEmpty()) {
                        return charity.getGrants().get(r).getAmountGranted();
                    } else {
                        return 0;
                    }
                }
            }
            return 0;
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public int getRowCount() {
            if (!foundation.getCharityList().isEmpty()) {
                return charity.getGrants().size();
            } else {
                return 0;
            }
        }

        @Override
        public String getColumnName(int c) {
            return cols.get(c);
        }

        public void setValueAt(Object value, int r, int c) {
            fireTableCellUpdated(r, c);
        }

        public boolean isCellEditable(int r, int c) {
            return false;
        }

    }

    // EFFECTS: adds the menu with add charity, add grant, load, save, reports, and review foundation tabs
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu charities = new JMenu("Charity");
        charities.setMnemonic('C');
        addMenuItem(charities, new AddCharityAction(),
                KeyStroke.getKeyStroke("control C"));
        menuBar.add(charities);
        addMenuItem(charities, new RemoveCharityAction(),
                KeyStroke.getKeyStroke("control R"));

        JMenu grants = new JMenu("Grant");
        charities.setMnemonic('G');
        addMenuItem(grants, new AddGrantAction(),
                KeyStroke.getKeyStroke("control G"));
        menuBar.add(grants);
        addMenuItem(grants, new RemoveGrantAction(),
                KeyStroke.getKeyStroke("control R"));

        JMenu reports = new JMenu("Funds");
        reports.setMnemonic('F');
        addMenuItem(reports, new AddFundsAction(),
                KeyStroke.getKeyStroke("Add Funds"));
        addMenuItem(reports, new GetFundsAvailable(),
                KeyStroke.getKeyStroke("Total Available Funds"));
        menuBar.add(reports);

        JMenu settings = new JMenu("Settings");
        settings.setMnemonic('S');
        addMenuItem(settings, new SaveAction(),
                KeyStroke.getKeyStroke("control S"));
        addMenuItem(settings, new LoadAction(),
                KeyStroke.getKeyStroke("control L"));
        addMenuItem(settings, new DeleteAllAction(),
                KeyStroke.getKeyStroke("control D"));
        menuBar.add(settings);

        setJMenuBar(menuBar);
    }

    /**
     * Adds an item with given handler to the given menu
     *
     * @param theMenu     menu to which new item is added
     * @param action      handler for new menu item
     * @param accelerator keystroke accelerator for this menu item
     */
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic((menuItem.getText().charAt(0)));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    /**
     * Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    private class AddFundsAction extends AbstractAction {

        AddFundsAction() {
            super("Add Funds");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String addFunds = JOptionPane.showInputDialog(null,
                    "Enter the amount of funds you want to add",
                    "Add Funds",
                    JOptionPane.QUESTION_MESSAGE);

            if (addFunds != null && Integer.parseInt(addFunds) < 0) {

                JOptionPane.showMessageDialog(null, "You cannot have a negative fund balance",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                foundation.addOrRemoveFunds(Integer.parseInt(addFunds));
                JOptionPane.showMessageDialog(null, "$"
                        + addFunds + " has been added to your foundation");
            } //TODO
        }
    }

    private class GetFundsAvailable extends AbstractAction {

        GetFundsAvailable() {
            super("Remaining Funds");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "There is $"
                    + foundation.getFundsAvailable() + " available to grant");//TODO
        }
    }

    private class AddCharityAction extends AbstractAction {

        AddCharityAction() {
            super("Add Charity");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String charityLoc = JOptionPane.showInputDialog(null,
                    "Enter the charity you want to add",
                    "Add Charity",
                    JOptionPane.QUESTION_MESSAGE);
//            try {
            if (charityLoc != null) {
                Charity c = new Charity(charityLoc);
                foundation.addCharity(c);
                charityTable.getValueAt(rowIndex, 0);
                rowIndex++;
                charityTableModel.fireTableDataChanged();
            }
//            } catch (DuplicateCharityException e) {
//                JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
//                        JOptionPane.ERROR_MESSAGE);
//            }
        }
    }

    private class RemoveCharityAction extends AbstractAction {

        RemoveCharityAction() {
            super("Remove Charity");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            ArrayList<String> charityNames = new ArrayList<>();
            String selectedCharity;

            if (foundation.getCharityList().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please add a charity", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                for (Charity c : foundation.getCharityList()) {
                    charityNames.add(c.getName());
                }

                JComboBox cb = new JComboBox(charityNames.toArray());


                JOptionPane.showMessageDialog(null, cb, "Remove Charity",
                        JOptionPane.QUESTION_MESSAGE);

                selectedCharity = cb.getSelectedItem().toString();
//            try {
                if (selectedCharity != null) {
                    Charity c = new Charity(selectedCharity);
                    for (Charity c2 : foundation.getCharityList()) {
                        if (c2.getName().equals(selectedCharity)) {
                            charity = c2;
                        }
                    }
                }
                foundation.removeCharity(charity);
                rowIndex--;
                charityTableModel.fireTableDataChanged();
            }
//            } catch (DuplicateCharityException e) {
//                JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
//                        JOptionPane.ERROR_MESSAGE);
//            }
        }
    }


    /**
     * Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     */
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            GrantTrackingApplicationUI.this.requestFocusInWindow();
        }

    }


    private class AddGrantAction extends AbstractAction {
        String skip = ""; //todo is there a better way to do this?

        AddGrantAction() {
            super("Add Grant");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JTextField grantName = new JTextField();
            JTextField grantAmount = new JTextField();
            ArrayList<String> charityNames = new ArrayList<>();
            JComboBox<Grant.Status> cb = new JComboBox<>(Grant.Status.values());

            for (Charity c : foundation.getCharityList()) {
                charityNames.add(c.getName());
            }

            JComboBox cb2 = new JComboBox(charityNames.toArray());

            if (foundation.getCharityList().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Please add a charity before you add a grant",
                        "No charity to assign grant", JOptionPane.ERROR_MESSAGE);

                skip = "yes";

            } else {
                grantPanel = new JPanel(new GridLayout(4, 0, 0, 0));
                grantPanel.add(new JLabel("Grant Name"));
                grantPanel.add(grantName);
                grantPanel.add(new JLabel("Status"));
                grantPanel.add(cb);
                grantPanel.add(new JLabel("Amount Granted"));
                grantPanel.add(grantAmount);
                grantPanel.add(new JLabel("Associated Charity"));
                grantPanel.add(cb2);

                JOptionPane.showMessageDialog(null, grantPanel, "Add Grant", JOptionPane.QUESTION_MESSAGE);

                // try { //TODO get rid of extra box
                // TODO throw error is not given int or statusSelected

                String grantNameString = grantName.getText();
                String grantAmountString = grantAmount.getText();
                int grantAmountInt = Integer.parseInt(grantAmountString); // todo non-int exception?

                Grant.Status statusSelected = (Grant.Status) cb.getSelectedItem();
                String charitySelected = (String) cb2.getSelectedItem().toString();

                if (statusSelected == Grant.Status.AWARDED
                        && foundation.getFundsAvailable() - grantAmountInt < 0) {

                    JOptionPane.showMessageDialog(null, "Please add funding before you add grants",
                            "Insufficient Funds", JOptionPane.ERROR_MESSAGE);

                    skip = "yes";

                    // todo is there a way to have the add funds action pop up automatically

                } else if (foundation.getFundsAvailable() - grantAmountInt >= 0 && grantPanel != null) {

                    grant = new Grant(
                            grantNameString,
                            statusSelected,
                            grantAmountInt);

                    skip = "no";
                }

                if (skip.equals("no")) {
                    for (Charity c : foundation.getCharityList()) {
                        if (c.getName().equals(charitySelected)) {
                            c.addGrant(grant);
                            foundation.addGrant(grant);
                        }
                    }
                }
                charityTableModel.fireTableDataChanged();
                // todo issue with rejected grants

            }
//            } catch (DuplicateGrantException e) {
//                JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
//                        JOptionPane.ERROR_MESSAGE);
//            }

        }
    }

    public class RemoveGrantAction extends AbstractAction {

        RemoveGrantAction() {
            super("Remove Grant");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            ArrayList<String> grantNames = new ArrayList<>();
            ArrayList<String> charityNames = new ArrayList<>();
            String selectedCharity;
            String selectedGrant;
            JPanel removeGrantPanel = null;
            JLabel cbLabel;
            JLabel cb2Label;
            JComboBox cb;
            JComboBox cb2;

            new CharityTableListener();

            if (foundation.getCharityList().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please add a charity & grant",
                        "Error", JOptionPane.ERROR_MESSAGE);

            } else if (charity.getGrants().isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are no grants associated with this "
                        + "charity", "Error", JOptionPane.ERROR_MESSAGE);
            } else {

                for (Grant g : charity.getGrants()) {
                    grantNames.add(g.getGrantName());
                }

                cb = new JComboBox(grantNames.toArray());

                JOptionPane.showMessageDialog(null, cb, "Remove Grant",
                        JOptionPane.QUESTION_MESSAGE);

                selectedGrant = cb.getSelectedItem().toString();

                if (selectedGrant != null) {
                    charity.removeGrant(selectedGrant);
                }
                grantTableModel.fireTableDataChanged();

            }
//            else if (foundation.getCharityList() != null) {
//                for (Charity c : foundation.getCharityList()) {
//                    charityNames.add(c.getName());
//                }
//            }
//            cb = new JComboBox(charityNames.toArray());
//            cbLabel = new JLabel("Select Charity");
//            for (Grant g : charity.getGrants()) {
//                grantNames.add(g.getGrantName());
//            }
//            cb2 = new JComboBox(grantNames.toArray());
//            cb2Label = new JLabel("Select Grant");
//            removeGrantPanel.add(cbLabel);
//            removeGrantPanel.add(cb);
//            removeGrantPanel.add(cb2Label);
//            removeGrantPanel.add(cb2);
//
//            JOptionPane.showMessageDialog(null, removeGrantPanel, "Remove Grant",
//                    JOptionPane.QUESTION_MESSAGE);
//
//            selectedGrant = cb2.getSelectedItem().toString();
//
//            if (selectedGrant != null) {
//                charity.removeGrant(selectedGrant);
        }
    }
//            try {
////            } catch (DuplicateCharityException e) {
////                JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
////                        JOptionPane.ERROR_MESSAGE);
////            }


    public class CharityTableListener extends MouseAdapter {
        String charityName;

        public void mouseClicked(MouseEvent e) {
            Point point = e.getPoint();

            if (e != null) {
                int row = charityTable.rowAtPoint(point);
                charityName = charityTable.getValueAt(row, 0).toString();

                for (Charity c : foundation.getCharityList()) {
                    if (c.getName().equals(charityName)) {
                        charity = c;
                    }
                }
                grantTableModel.fireTableDataChanged();
            }
        }
    }

    // EFFECTS: saves the foundation to file
    private class SaveAction extends AbstractAction {

        SaveAction() {
            super("Save");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                jsonWriter.open();
                jsonWriter.write(foundation);
                jsonWriter.close();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "There was an error with save the file to "
                        + JSON_STORE);
            }

        }
    }

    // MODIFIES: this
    // EFFECTS: load the foundation from file
    private class LoadAction extends AbstractAction {

        LoadAction() {
            super("Load");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                foundation = jsonReader.read();
                charityTableModel.fireTableDataChanged();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "There was an error with loading the file "
                        + JSON_STORE);
            }
        }
    }

    // EFFECTS: delete all charities and grants in the charity
    private class DeleteAllAction extends AbstractAction {

        DeleteAllAction() {
            super("Delete");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            foundation.getCharityList().clear();
            rowIndex = 0;
            foundation.addOrRemoveFunds(-foundation.getFundsAvailable());
            charityTableModel.fireTableDataChanged();
        }
    }

    // starts the application
    public static void main(String[] args) {
        new GrantTrackingApplicationUI();
    }
}
