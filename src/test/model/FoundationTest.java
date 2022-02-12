package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class FoundationTest {

    private Foundation foundation;
    private Charity charity1;
    private Charity charity3;
    List<Charity> expectedCharity;

    @BeforeEach
    void runBefore() {
        foundation = new Foundation();
        charity1 = new Charity("Micah House");
        charity3 = new Charity("Rising Angels");
        expectedCharity = new ArrayList<>();
    }

    @Test
    void constructorTest() {
        List<Grant> expectedGrant = new ArrayList<>();
        assertEquals(expectedCharity, foundation.getCharityList());
        assertEquals(expectedGrant, foundation.getGrants());
        assertEquals(0, foundation.getFundsAvailable());
    }

    @Test
    void addCharityTest() {
        assertEquals(expectedCharity, foundation.getCharityList());
        foundation.addCharity(charity1);
        expectedCharity.add(charity1);
        assertEquals(expectedCharity, foundation.getCharityList());
        foundation.addCharity(charity3);
        expectedCharity.add(charity3);
        assertEquals(expectedCharity, foundation.getCharityList());
    }

    @Test
    void addGrantTest() {
        Grant grant = new Grant("Micah House", "Awarded", 40);
        Grant grant2 = new Grant("Indwell", "Rejected", 100);
        assertEquals(0, foundation.getFundsAvailable());
        foundation.addOrRemoveFunds(100);
        foundation.addGrant(grant);
        assertEquals(60, foundation.getFundsAvailable());
        foundation.addGrant(grant2);
        assertEquals(60, foundation.getFundsAvailable());
    }

    @Test
    void addOrRemoveFundsTest() {
        assertEquals(0, foundation.getFundsAvailable());
        foundation.addOrRemoveFunds(50000);
        assertEquals(50000, foundation.getFundsAvailable());
        foundation.addOrRemoveFunds(-20000);
        assertEquals(30000, foundation.getFundsAvailable());
        foundation.addOrRemoveFunds(-40000);
        assertEquals(-10000, foundation.getFundsAvailable());
    }

    @Test
    void addFundsTest() {
        assertEquals(0, foundation.getFundsAvailable());
        foundation.addFunds(75000);
        assertEquals(75000, foundation.getFundsAvailable());
    }

    @Test
    void removeFundsTest() {
        assertEquals(0, foundation.getFundsAvailable());
        foundation.addFunds(10000);
        assertEquals(10000, foundation.getFundsAvailable());
        foundation.removeFunds(-5000);
        assertEquals(5000, foundation.getFundsAvailable());
    }

}
