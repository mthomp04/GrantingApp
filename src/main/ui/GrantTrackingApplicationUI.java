package ui;

import model.Charity;
import model.EventLog;
import model.Foundation;
import model.Grant;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
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
    //private JPanel ubcImage;
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
    private JInternalFrame background;
    private JInternalFrame grantWindow;
    private JPanel grantPanel;
    private Charity charity;
    private int rowIndex = 0;

    public GrantTrackingApplicationUI() {

        addTheme();
        desktop = new JDesktopPane();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        desktop.addMouseListener(new DesktopFocusAction());
        setContentPane(desktop);
        setTitle("Grant Tracking Application");
        setSize(WIDTH, HEIGHT);
        addMenu();
        addBackground();
        addGrantWindow();
        addCharityWindow();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new DesktopCloseListener());
        centreOnScreen();
        setVisible(true);
    }

    // EFFECTS: required method for the granttrackingapplicationui class
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    // MODIFIES: this, desktop
    // EFFECTS: adds nimbus theme to all components and if not supported sets to default java metal look and feel
    private void addTheme() {
        try {
            for (UIManager.LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(lafInfo.getName())) {
                    UIManager.setLookAndFeel(lafInfo.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        UIManager.put("nimbusBase", Color.white);
    }

    // MODIFIES: this, desktop
    // EFFECTS: adds charity window to the desktop with a working table
    private void addCharityWindow() {
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
    }

    // MODIFIES: this, desktop
    // EFFECTS: adds grant window to the desktop with a working table
    private void addGrantWindow() {
        grantWindow = new JInternalFrame("Grants", true, true, true, false);
        grantWindow.setPreferredSize(new Dimension(WIDTH - 20, HEIGHT / 4));
        grantWindow.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
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
    }

    // MODIFIES: this, desktop
    // EFFECTS: adds ubc logo as a panel to the background of the application
    private void addBackground() {
        background = new JInternalFrame();
        background.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        background.pack();
        desktop.add(background);
        background.setVisible(true);

//        ubcImage = new ImagePanel();
//        ubcImage.setPreferredSize(new Dimension(WIDTH, HEIGHT));
//        background.add(ubcImage);
    }

    /*
    //EFFECTS: adds image to the background panel
    public static class ImagePanel extends JPanel {
        BufferedImage image;

        public ImagePanel() {
            try {
                image = ImageIO.read(new File("C:\\CPSC210\\project_k6t8k\\data\\ubc.png"));
            } catch (IOException e) {
                System.out.println("issues");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            //Image i = image.getScaledInstance(WIDTH, HEIGHT,Image.SCALE_DEFAULT);
            g.drawImage(image, 0, 0, this);
            repaint();
        }
    }
*/

    // MODIFIES: this, charitywindow
    // EFFECTS: creates table for charities
    class CharityTable extends AbstractTableModel {
        ArrayList<String> cols = new ArrayList<>();

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

    // MODIFIES: this, grantwindow
    // EFFECTS: creates table for grants
    class GrantTable extends AbstractTableModel {
        ArrayList<String> cols = new ArrayList<>();

        public GrantTable() {
            cols.add("Name");
            cols.add("Status");
            cols.add("Amount Awarded");
        }

        @Override
        public Object getValueAt(int r, int c) {
            switch (c) {
                case (0): {
                    return columnOne(r);
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

        // EFFECTS: returns the inputs for column one
        private Object columnOne(int r) {
            if (!foundation.getCharityList().isEmpty()) {
                return charity.getGrants().get(r).getGrantName();
            } else {
                return 0;
            }
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

    // EFFECTS: adds the menu with add charity, add grant, load, save, funds, and review foundation tabs
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu charities = addCharityMenuBar(menuBar);

        addGrantMenuBar(menuBar, charities);

        addReportsMenuBar(menuBar);

        addSettingsMenuBar(menuBar);

        setJMenuBar(menuBar);
    }

    // EFFECTS: adds setting actions to menu bar
    private void addSettingsMenuBar(JMenuBar menuBar) {
        JMenu settings = new JMenu("Settings");
        settings.setMnemonic('S');
        addMenuItem(settings, new SaveAction(),
                KeyStroke.getKeyStroke("control S"));
        addMenuItem(settings, new LoadAction(),
                KeyStroke.getKeyStroke("control L"));
        addMenuItem(settings, new DeleteAllCharitiesAction(),
                KeyStroke.getKeyStroke("control D"));
        menuBar.add(settings);
    }

    // EFFECTS: adds report actions to menu bar
    private void addReportsMenuBar(JMenuBar menuBar) {
        JMenu reports = new JMenu("Funds");
        reports.setMnemonic('F');
        addMenuItem(reports, new AddFundsAction(),
                KeyStroke.getKeyStroke("Add Funds"));
        addMenuItem(reports, new GetFundsAvailable(),
                KeyStroke.getKeyStroke("Total Available Funds"));
        menuBar.add(reports);
    }

    // EFFECTS: adds grant actions to menu bar
    private void addGrantMenuBar(JMenuBar menuBar, JMenu charities) {
        JMenu grants = new JMenu("Grant");
        charities.setMnemonic('G');
        addMenuItem(grants, new AddGrantAction(),
                KeyStroke.getKeyStroke("control G"));
        menuBar.add(grants);
        addMenuItem(grants, new RemoveGrantAction(),
                KeyStroke.getKeyStroke("control R"));
    }

    // EFFECTS: adds charity actions to menu bar
    private JMenu addCharityMenuBar(JMenuBar menuBar) {
        JMenu charities = new JMenu("Charity");
        charities.setMnemonic('C');
        addMenuItem(charities, new AddCharityAction(),
                KeyStroke.getKeyStroke("control C"));
        menuBar.add(charities);
        addMenuItem(charities, new RemoveCharityAction(),
                KeyStroke.getKeyStroke("control R"));
        return charities;
    }


    // EFFECTS: adds an item with given handler to the given menu
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic((menuItem.getText().charAt(0)));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    // EFFECTS: helper to centre main application window on desktop
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    // MODIFIES: this, foundation
    // EFFECTS: action to allow user to add funds to the foundation
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
            }
        }
    }

    // EFFECTS: action to allow user to get remaining funds available to grant
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

    // MODIFIES: this, foundation
    // EFFECTS: action to allow user to add charity to the foundation
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

            //  try {
            if (charityLoc != null) {
                Charity c = new Charity(charityLoc);
                foundation.addCharity(c);
                charityTable.getValueAt(rowIndex, 0);
                rowIndex++;
                charityTableModel.fireTableDataChanged();
            }
        }
    }

    // MODIFIES: this, foundation
    // EFFECTS: action to allow user to remove charity from the foundation or provides error if not possible
    private class RemoveCharityAction extends AbstractAction {

        RemoveCharityAction() {
            super("Remove Charity");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            ArrayList<String> charityNames = new ArrayList<>();

            if (foundation.getCharityList().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please add a charity", "Error",
                        JOptionPane.ERROR_MESSAGE);

            } else {
                for (Charity c : foundation.getCharityList()) {
                    charityNames.add(c.getName());
                }

                removeCharity(charityNames);
            }
        }

        // EFFECTS: removes charity from the foundation
        private void removeCharity(ArrayList<String> charityNames) {
            String selectedCharity;
            JComboBox cb = new JComboBox(charityNames.toArray());


            JOptionPane.showMessageDialog(null, cb, "Remove Charity",
                    JOptionPane.QUESTION_MESSAGE);

            selectedCharity = cb.getSelectedItem().toString();
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
    }

    // EFFECTS: delete all charities and grants in the charity
    private class DeleteAllCharitiesAction extends AbstractAction {

        DeleteAllCharitiesAction() {
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

    // EFFECTS: represents action to be taken when user clicks desktop to switch focus. (Needed for key handling.)
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            GrantTrackingApplicationUI.this.requestFocusInWindow();
        }
    }

    // MODIFIES: this, charity
    // EFFECTS: action to allow user to add a grant to the given charity
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
                addGrant(grantName, grantAmount, cb, cb2);

            }
        }

        // EFFECTS: constructs grant to add to a given charity
        private void addGrant(JTextField grantName, JTextField grantAmount,
                              JComboBox<Grant.Status> cb, JComboBox cb2) {

            constructAddCharityPopUpWindow(grantName, grantAmount, cb, cb2);
            String grantNameString = grantName.getText();
            String grantAmountString = grantAmount.getText();
            int grantAmountInt = Integer.parseInt(grantAmountString);

            Grant.Status statusSelected = (Grant.Status) cb.getSelectedItem();
            String charitySelected = cb2.getSelectedItem().toString();

            if (statusSelected == Grant.Status.AWARDED
                    && foundation.getFundsAvailable() - grantAmountInt < 0) {

                insufficientFundsAddGrant("Please add funding before you add grants", "Insufficient Funds");

            } else if (foundation.getFundsAvailable() - grantAmountInt >= 0 && grantPanel != null) {
                grant = new Grant(grantNameString, statusSelected, grantAmountInt);
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
        }

        // EFFECTS: shows error pop up window if there are insufficient funds to add a given grant
        private void insufficientFundsAddGrant(String s, String s2) {
            JOptionPane.showMessageDialog(null, s,
                    s2, JOptionPane.ERROR_MESSAGE);

            skip = "yes";
        }

        // EFFECTS: creates add charity pop up window
        private void constructAddCharityPopUpWindow(JTextField grantName, JTextField grantAmount,
                                                    JComboBox<Grant.Status> cb, JComboBox cb2) {
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
        }
    }

    // MODIFIES: this, charity
    // EFFECTS: action to allow user to remove a grant from the given charity
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
            JPanel removeGrantPanel = new JPanel();
            JComboBox cb;
            JLabel cbLabel = new JLabel("Select grant to remove");
            JLabel cbLabel2 = new JLabel("Select charity to remove");

            new CharityTableListener();

            if (foundation.getCharityList().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please add a charity & grant",
                        "Error", JOptionPane.ERROR_MESSAGE);

            } else if (charity != null && charity.getGrants().isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are no grants associated with this "
                        + "charity", "Error", JOptionPane.ERROR_MESSAGE);

            } else if (charity != null) {

                removeGrantMouseSelection(grantNames, removeGrantPanel, cbLabel);

            } else {

                noGrantsError(grantNames, charityNames);
            }
        }

        // EFFECTS: shows error if there are no grants for given charity to remove. Otherwise, calls
        //          removeGrantNoMouseSelection method
        private void noGrantsError(ArrayList<String> grantNames, ArrayList<String> charityNames) {
            for (Charity c : foundation.getCharityList()) {
                if (!c.getGrants().isEmpty()) {
                    charityNames.add(c.getName());
                }
                if (charityNames.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please add a charity & grant",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    removeGrantNoMouseSelection(grantNames, charityNames);
                }
            }
        }

        // EFFECTS: removes a grant from a charity in the event a charity has been initially selected in the
        //          charitytable
        private void removeGrantMouseSelection(ArrayList<String> grantNames, JPanel removeGrantPanel, JLabel cbLabel) {
            String selectedGrant;
            JComboBox cb;
            for (Grant g : charity.getGrants()) {
                grantNames.add(g.getGrantName());
            }

            cb = new JComboBox(grantNames.toArray());
            removeGrantPanel.add(cbLabel);
            removeGrantPanel.add(cb);

            JOptionPane.showMessageDialog(null, removeGrantPanel, "Remove Grant",
                    JOptionPane.QUESTION_MESSAGE);

            selectedGrant = cb.getSelectedItem().toString();

            if (selectedGrant != null) {
                charity.removeGrant(selectedGrant);
            }

            grantTableModel.fireTableDataChanged();
            charityTableModel.fireTableDataChanged();
        }

        // EFFECTS: removes a grant from a charity in the event that no charity has been initially selected in the
        //          charitytable
        private void removeGrantNoMouseSelection(ArrayList<String> grantNames, ArrayList<String> charityNames) {
            String selectedGrant;
            String selectedCharity;
            JComboBox cb2 = new JComboBox(charityNames.toArray());

            JOptionPane.showMessageDialog(null, cb2, "Select Charity",
                    JOptionPane.QUESTION_MESSAGE);

            selectedCharity = cb2.getSelectedItem().toString();

            for (Charity c1 : foundation.getCharityList()) {
                if (c1.getName().equals(selectedCharity)) {
                    charity = c1;
                }
            }

            for (Grant g : charity.getGrants()) {
                grantNames.add(g.getGrantName());
            }

            JComboBox cb3 = new JComboBox(grantNames.toArray());

            JOptionPane.showMessageDialog(null, cb3, "Remove Grant",
                    JOptionPane.QUESTION_MESSAGE);

            selectedGrant = cb3.getSelectedItem().toString();

            if (selectedGrant != null) {
                charity.removeGrant(selectedGrant);
            }

            grantTableModel.fireTableDataChanged();
            charityTableModel.fireTableDataChanged();
        }
    }

    // EFFECTS: identifies where a user has clicked on the charity table
    public class CharityTableListener extends MouseAdapter {
        String charityName;

        public void mouseClicked(MouseEvent e) {
            Point point = e.getPoint();

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

    // starts the application
    public static void main(String[] args) {
        new GrantTrackingApplicationUI();
    }

    private class DesktopCloseListener extends JDesktopPane implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            if (desktop.isDisplayable()) {
                charityWindow.dispose();
                grantWindow.dispose();
                desktop.setVisible(false);
                foundation.printLog(EventLog.getInstance());
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }
}

