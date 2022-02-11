package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CharityTest {

    private Charity testCharity;
    private Charity testCharity2;
    private Charity testCharity3;
    private ArrayList<Grant> expected;
    private Grant grant1;
    private Grant grant2;
    private Grant grant3;

    @BeforeEach
    void runBefore() {
        testCharity = new Charity("Micah House");
        testCharity2 = new Charity("Indwell");
        testCharity3 = new Charity("Yonge Street Mission");
        expected = new ArrayList<>();
        grant1 = new Grant("New Shelter", "Awarded", 10000);
        grant2 = new Grant("Employment Program", "Rejected", 0);
        grant3 = new Grant("Anti-Human Trafficking", "Awarded", 50000);
        testCharity3.addGrant(grant1);
        testCharity3.addGrant(grant2);
        testCharity3.addGrant(grant3);

    }

    @Test
    void constructorTest() {
        assertEquals("Micah House", testCharity.getName());

    }

    @Test
    void testAddGrant() {
        testCharity.addGrant(grant1);
        expected.add(grant1);
        assertEquals(expected, testCharity.getGrants());
        testCharity.addGrant(grant2);
        expected.add(grant2);
        assertEquals(expected, testCharity.getGrants());
        testCharity2.addGrant(grant3);
        expected.remove(grant1);
        expected.remove(grant2);
        expected.add(grant3);
        assertEquals(expected, testCharity2.getGrants());

    }

    @Test
    void totalFundedTest() {
        assertEquals(0,testCharity.totalFunded());
        testCharity.addGrant(grant1);
        assertEquals(10000, testCharity.totalFunded());
        testCharity.addGrant(grant2);
        testCharity.addGrant(grant3);
        assertEquals(60000, testCharity.totalFunded());

    }

    @Test
    void largestGrantReceivedTest() {
        assertEquals(50000, testCharity3.largestGrantReceived());
        testCharity.addGrant(grant1);
        assertEquals(10000, testCharity.largestGrantReceived());
        testCharity.addGrant(grant3);
        assertEquals(50000, testCharity.largestGrantReceived());
        assertEquals(0, testCharity2.largestGrantReceived());

    }

    @Test
    void listAwardedGrantsTest() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add(grant1.getGrantName());
        expected.add(grant3.getGrantName());
        assertEquals("List of grant applications :" + expected,
                     "List of grant applications :" + testCharity3.listAwardedGrants());
    }

}
