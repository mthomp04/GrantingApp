package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrantTest {

    private Grant testGrant;
    private Foundation foundation;

    @BeforeEach
    void runBefore() {
        testGrant = new Grant("Grant1", "Awarded",75000);
    }


    @Test
    void grantConstructorTest() {
        assertEquals("Grant1", testGrant.getGrantName());
        assertEquals("Awarded", testGrant.getStatus());
        assertEquals(75000, testGrant.getAmountGranted());
    }

//    @Test
//    void updateFundAmountTest() {
//        foundation = new Foundation();
//        foundation.addFunds(100000);
//        assertEquals(100000, foundation.getFundsAvailable());
//        testGrant.updateFundAmount(testGrant.getGrantName(), testGrant.getStatus(), testGrant.getAmountGranted());
//        assertEquals(25000,foundation.getFundsAvailable());
//    }



}
