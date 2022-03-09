package ui;

import model.Charity;
import model.Foundation;
import model.Grant;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String JSON_STORE = "./data/workroom.json";
    private Foundation ubcFoundation;
    private Scanner scanner;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs a foundation and starts process operations for the application
    public Main() {
        System.out.println("Welcome to the Grant Tracking Application");
        System.out.println("Our goal is to assist you in managing your funding distribution");
        ubcFoundation = new Foundation();
        processOperations();
    }

    //EFFECTS: prints the main menu options for user to select from which include:
    //         - adding a charity to the foundation
    //         - adding and assigning a grant to a charity
    //         - pulling pre-defined reports of funds distributed
    //         - review information about the foundation
    //         - save or load a file
    //         - quiting the application

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void processOperations() {
        @SuppressWarnings("methodlength")
        String operation;
        scanner = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        while (true) {
            System.out.println("Please select an option (Add charity, Add grant, Reports, "
                    + "Review foundation, Save, Load, Quit)");
            operation = scanner.nextLine();
            System.out.println("you selected " + operation);

            if (operation.equals("Add charity")) {
                addCharity();
            }

            if (operation.equals("Add grant")) {
                addGrant();
            }

            if (operation.equals("Reports")) {
                reviewCharity();
            }

            if (operation.equals("Review foundation")) {
                reviewFoundation();
            }

            if (operation.equals("Save")) {
                saveFoundation();
            }

            if (operation.equals("Load")) {
                loadFoundation();
            }

            if (operation.equals("Quit")) {
                break;
            }
        }
        System.out.println("Thank you for using the Grant Tracking Application");
    }

    // MODIFIES: this
    // EFFECTS: interface for constructing a new charity
    private void addCharity() {
        Charity charity;
        scanner = new Scanner(System.in);
        System.out.println("Enter the name of the charity");
        String charityName = scanner.nextLine();
        charity = new Charity(charityName);
        ubcFoundation.addCharity(charity);

        System.out.println("This is your charity: " + charityName);
    }

    // MODIFIES: this
    // EFFECTS: interface for constructing a new grant
    //          if no charities have been created prompts user to first create one before creating a grant
    //          if there are not enough funds in the foundation prompts user to add funds
    //          otherwise greats new grant with name, status, and amount
    //          allocates grant to a given charity and removes funds of grant from total foundation's funds
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void addGrant() {
        @SuppressWarnings("methodlength")
        Grant grant;
        Grant.Status grantStatus;
        String grantStatusString;
        int amountFunded;
        scanner = new Scanner(System.in);

        if (ubcFoundation.getCharityList().isEmpty()) {
            System.out.println("Add a charity before you add any grants");
        } else if (!ubcFoundation.getCharityList().isEmpty()) {

            System.out.println("Enter the name of the grant");
            String grantName = scanner.nextLine();

            System.out.println("Enter the status of the grant (Awarded or Rejected)");
            grantStatusString = scanner.nextLine();

            while (!grantStatusString.equals("Awarded") && !grantStatusString.equals("Rejected")) {
                System.out.println("Invalid entry");
                System.out.println("Enter the status of the grant (Awarded or Rejected)");
                grantStatusString = scanner.nextLine();
            }
            grantStatus = statusStringConverter(grantStatusString);

            System.out.println("Enter the amount of funding requested in the grant");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid entry");
                System.out.println("Enter the amount of funding requested in the grant");
                scanner = new Scanner(System.in);
            }
            amountFunded = scanner.nextInt();

            while (grantStatus == Grant.Status.AWARDED && ubcFoundation.getFundsAvailable() - amountFunded < 0) {
                String operations;
                System.out.println("Insufficient funds. Please add funding before you add grants");
                scanner = new Scanner(System.in);
                System.out.println("Type 'Add funds' to add more funding or type 'Quit'");
                operations = scanner.nextLine();

                if (operations.equals("Add funds")) {
                    adjustFunds();
                }

                if (operations.equals("Quit")) {
                    processOperations();
                    break;
                }
                scanner = new Scanner(System.in);
            }

            grant = new Grant(grantName, grantStatus, amountFunded);
            System.out.println("This is your grant: " + grantName + " " + grantStatusString + " " + "$" + amountFunded);
            assignGrant(grant);
            removeFunds(grant);
        }
    }

    // MODIFIED: this
    // EFFECTS: converts string into a status
    private Grant.Status statusStringConverter(String status) {
        Grant.Status grantStatus;
        if (status.equals("Awarded")) {
            grantStatus = Grant.Status.AWARDED;
        } else {
            grantStatus = Grant.Status.REJECTED;
        }
        return grantStatus;
    }

    // MODIFIES: Charity
    // EFFECTS: interface for assigning a newly created grant to a given charity already in the database
    //          if the charity does not exist in the database, prompts user to select a new charity
    private void assignGrant(Grant grant) {
        System.out.println("Enter a charity to assign the grant to from the following list:");
        scanner = new Scanner(System.in);
        System.out.println(ubcFoundation.listOfCharitiesAndAmountFunded());
        String charityName = scanner.nextLine();
        String notFound = "not found";

        for (Charity charity : ubcFoundation.getCharityList()) {
            if (charity.getName().equals(charityName)) {
                charity.addGrant(grant);
                notFound = "found";
            }
        }
        if (notFound.equals("not found")) {
            System.out.println("The charity " + charityName + " is not in the database");
            assignGrant(grant);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes grant funds from total amount funds in the foundation
    private void removeFunds(Grant grant) {
        scanner = new Scanner(System.in);
        ubcFoundation.addGrant(grant);
        System.out.println("There is $" + ubcFoundation.getFundsAvailable() + " available to grant");
    }

    // EFFECTS: prints a secondary menu for users to pull pre-defined reports about grants including:
    //          - total amount of funding received by a specified charity
    //          - the largest grant received by a specified charity
    //          - a list of all grants awarded for a specified charity
    //          - returns user back to main menu
    private void reviewCharity() {
        String operation;

        while (true) {
            System.out.println("Select the task you would like to complete");
            System.out.println("Type 'Total funded' to review how much a charity has received funding");
            System.out.println("Type 'Largest grant' to review the largest grant given to a charity");
            System.out.println("Type 'List grants' to review a list of all of the grants awarded to a charity");
            System.out.println("Type 'Return' to return to main menu");
            operation = scanner.nextLine();
            System.out.println("you selected " + operation);

            if (operation.equals("Total funded")) {
                totalFunded();
            }

            if (operation.equals("Largest grant")) {
                largestGrant();
            }

            if (operation.equals("List grants")) {
                listGrants();
            }

            if (operation.equals("Return")) {
                break;
            }
        }
    }

    // EFFECTS: displays total amount of funding for a given charity
    private void totalFunded() {
        scanner = new Scanner(System.in);
        System.out.println("Enter the name of the charity you want to review total funds received");
        String charityName = scanner.nextLine();
        for (Charity charity : ubcFoundation.getCharityList()) {
            if (charity.getName().equals(charityName)) {
                System.out.println("Total funds granted to " + charityName + " is $" + charity.totalFunded());
            }
        }
    }

    // EFFECTS: displays the largest grant received by a given charity
    private void largestGrant() {
        scanner = new Scanner(System.in);
        System.out.println("Enter the name of the charity you want to review the largest grant received");
        String charityName = scanner.nextLine();
        for (Charity charity : ubcFoundation.getCharityList()) {
            if (charity.getName().equals(charityName)) {
                System.out.println("Largest grant received by "
                        + charityName + " is $" + charity.largestGrantReceived());
            }
        }
    }

    // EFFECTS: lists all "Awarded" grants received by a given charity
    private void listGrants() {
        Charity passCharity = null;
        scanner = new Scanner(System.in);
        System.out.println("Enter the name of charity you want to see a list of all awarded grants received");
        String charityName = scanner.nextLine();
        for (Charity charity1 : ubcFoundation.getCharityList()) {
            if (charity1.getName().equals(charityName)) {
                System.out.println(charity1.listAwardedGrants());
                passCharity = charity1;
            }
        }
        if (passCharity == null) {
            System.out.println("Charity cannot be found");
        } else {
            System.out.println("Would you like to remove a grant from the list? (Yes or No)");
            String operation = scanner.nextLine();
            System.out.println("you selected " + operation);

            if (operation.equals("Yes")) {
                removeGrant(passCharity);
            }
        }
    }

    // MODIFIES: charity
    // EFFECTS: removes grant from a given charity
    private void removeGrant(Charity passCharity) {
        scanner = new Scanner(System.in);
        System.out.println("Enter the name of a grant to remove it");
        String grantName = scanner.nextLine();

        passCharity.removeGrant(grantName);
        System.out.println(grantName + " has been removed from ");
    }

    // EFFECTS: prints secondary menu to review information about the foundation including:
    //          - a list of all charities & the total amount of funding received
    //          - add funds to the foundation
    //          - return to the main menu
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void reviewFoundation() {
        @SuppressWarnings("methodlength")
        String operations;

        while (true) {
            scanner = new Scanner(System.in);
            System.out.println("Select the task you would like to complete");
            System.out.println("Type 'View charities' to see a list of all "
                    + "charities in the database with total funded to organization");
            System.out.println("Type 'Add funds' to add more funds available to your foundation to grant");
            System.out.println("Type 'View total funds' to view current balance of funds");
            System.out.println("Type 'Return' to return to main menu");
            operations = scanner.nextLine();
            System.out.println("you selected " + operations);

            if (operations.equals("View charities")) {
                System.out.println(ubcFoundation.listOfCharitiesAndAmountFunded());
            }

            if (operations.equals("Add funds")) {
                adjustFunds();
            }

            if (operations.equals("View total funds")) {
                System.out.println("There is $" + ubcFoundation.getFundsAvailable() + " available to grant");
            }

            if (operations.equals("Return")) {
                break;
            }
        }
    }

    // EFFECTS: updates total amount of funds available for the organization to grant
    private void adjustFunds() {
        scanner = new Scanner(System.in);
        int funds;
        System.out.println("Enter amount of funds you want to add or remove");
        funds = scanner.nextInt();
        ubcFoundation.addOrRemoveFunds(funds);
        System.out.println("$" + funds + " has been add to the foundation");
        System.out.println("$" + ubcFoundation.getFundsAvailable() + " is now available to grant");
    }

    // EFFECTS: saves the foundation to file
    private void saveFoundation() {
        try {
            jsonWriter.open();
            jsonWriter.write(ubcFoundation);
            jsonWriter.close();
            System.out.println("Saved file to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: load the foundation from file
    private void loadFoundation() {
        try {
            ubcFoundation = jsonReader.read();
            System.out.println("Loaded file " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file " + JSON_STORE);
        }

    }


    public static void main(String[] args) {
        new Main();
    }
}