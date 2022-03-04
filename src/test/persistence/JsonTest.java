package persistence;


import model.Charity;
import model.Grant;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    ArrayList<Charity> charities = new ArrayList<>();
    ArrayList<Grant> grants = new ArrayList<>();
    ArrayList<Grant> grants2 = new ArrayList<>();


    protected void checkCharity(String name, List<Grant> grants, Charity charity) {
        assertEquals(name, charity.getName());
        assertEquals(grants, charity.getGrants());
    }

    protected void checkGrant(String name, Grant.Status status, int amountGranted, Grant grant) {
        assertEquals(name, grant.getGrantName());
        assertEquals(status, grant.getStatus());
        assertEquals(amountGranted, grant.getAmountGranted());
    }

}
