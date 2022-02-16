package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrantTest {

    private Grant testGrant;
    private Foundation foundation;

    @BeforeEach
    void runBefore() {
        testGrant = new Grant("Grant1", Grant.Status.AWARDED,75000);
    }

    @Test
    void grantConstructorTest() {
        assertEquals("Grant1", testGrant.getGrantName());
        assertEquals("AWARDED", testGrant.getStatus().toString());
        assertEquals(75000, testGrant.getAmountGranted());
    }

}
