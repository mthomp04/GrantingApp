package ui;

import model.Charity;
import model.Foundation;
import model.Grant;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private Foundation ubcFoundation;
    private Charity charity;
    private Grant grant;
    private Scanner scanner;

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
    //         - quiting the application
    private void processOperations() {
        @SuppressWarnings("methodlength")
        String operation;
        scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please select an option (Add charity, Add grant, Reports, Review foundation, Quit)");
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

            if (operation.equals("Quit")) {
                break;
            }
        }

        System.out.println("Thank you for using the Grant Tracking Application");
    }

    // MODIFIES: this
    // EFFECTS: interface for constructing a new charity
    private void addCharity() {
        System.out.println("Enter the name of the charity");
        String charityName = scanner.nextLine();
        charity = new Charity(charityName);
        ubcFoundation.addCharity(charity);

        System.out.println("This is your charity: " + charityName);
    }

    // MODIFIES: this
    // EFFECTS: interface for constructing a new grant
    //          if no charities have been created prompts user to first create one before creating a grant
    private void addGrant() {
        if (ubcFoundation.getCharityList().isEmpty()) {
            System.out.println("Add a charity before you add any grants");
        } else if (!ubcFoundation.getCharityList().isEmpty()) {

            System.out.println("Enter the name of the grant");
            String grantName = scanner.nextLine();

            System.out.println("Enter the status of the grant (Awarded or Rejected)");
            String grantStatus = scanner.nextLine();

            System.out.println("Enter the amount of funding approved for the grant");
            int amountFunded = scanner.nextInt();

            grant = new Grant(grantName, grantStatus, amountFunded);
            System.out.println("This is your grant: " + grantName + " " + grantStatus + " " + "$" + amountFunded);

            assignGrant(grant);
        }
    }

    // MODIFIES: Charity
    // EFFECTS: interface for assigning a newly created grant to a given charity already in the database
    //          if the charity does not exist in the database, prompts user to select a new charity
    private void assignGrant(Grant grant) {
        System.out.println("Enter a charity to assign the grant to from the following list:");
        listAllCharities();
        scanner = new Scanner(System.in);
        String charityName = scanner.nextLine();
        String notFound = "not found";

        for (Charity charity : ubcFoundation.getCharityList()) {
            if (charity.getName().equals(charityName)) {
                charity.addGrant(grant);
                notFound = "found";
            }
        }
        if (notFound.equals("not found")) {
            System.out.println("The charity " + charityName + " is not the database");
            assignGrant(grant);
        }
    }

    // EFFECTS: prints a secondary menu for users to pull pre-defined reports about grants including:
    //          - total amount of funding received by a specified charity
    //          - largest grant received by a specified charity
    //          - a list of all grants awarded for a specified charity
    //          - return back to main menu
    private void reviewCharity() {
        String operation;
        scanner = new Scanner(System.in);

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
                processOperations();
            }
        }
    }

    // EFFECTS: displays total amount of funding for a given charity
    private void totalFunded() {
        System.out.println("Enter the name of charity you want to review total funds received");
        String charityName = scanner.nextLine();
        for (Charity charity : ubcFoundation.getCharityList()) {
            if (charity.getName().equals(charityName)) {
                System.out.println("Total funds give to " + charityName + "is " + charity.totalFunded());
            }
        }
    }

    // EFFECTS: displays the largest grant received by a given charity
    private void largestGrant() {
        System.out.println("Enter the name of charity you want to review total funds received");
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
        System.out.println("Enter the name of charity you see a list of grants");
        String charityName = scanner.nextLine();
        for (Charity charity1 : ubcFoundation.getCharityList()) {
            if (charity1.getName().equals(charityName)) {
                System.out.println(charity1.listAwardedGrants());
            }
        }
    }

    // EFFECTS: prints secondary menu to review information about the foundation including:
    //          - a list of all charities & the total amount of funding received
    //          - return to the main menu
    private void reviewFoundation() {
        String operation;
        scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select the task you would like to complete");
            System.out.println("Type 'View charities' to see a list of all "
                    + "charities in the database with total funded to organization");
            System.out.println("Type 'Return' to return to main menu");
            operation = scanner.nextLine();
            System.out.println("you selected " + operation);

            if (operation.equals("View charities")) {
                listAllCharities();
            }

            if (operation.equals("Return")) {
                processOperations();
            }

        }
    }

    // EFFECTS: list of all charities & the total amount of funding received
    private void listAllCharities() {
        ArrayList<String> charities = new ArrayList<>();
        for (Charity charity : ubcFoundation.getCharityList()) {
            charities.add(charity.getName() + " has received $" + charity.totalFunded());
        }
        System.out.println(charities);
    }

    public static void main(String[] args) {
        new Main();
    }
}