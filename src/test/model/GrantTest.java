package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrantTest {

    private Grant testGrant;

    @BeforeEach
    void runBefore() {
        testGrant = new Grant("Grant1", "Awarded",75000, "Shelter/Housing");
    }


    @Test
    void grantConstructorTest() {
        assertEquals("Grant1", testGrant.getGrantName());
        assertEquals("Awarded", testGrant.getStatus());
        assertEquals(75000, testGrant.getAmountGranted());
        assertEquals("Shelter/Housing", testGrant.getProjectTheme());

    }


}
