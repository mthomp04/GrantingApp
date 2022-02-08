package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static model.Charity.addGrant;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CharityTest {

    private Charity testCharity;
    private Charity testCharity2;
    private Charity testCharity3;
    private Grant grant1;
    private Grant grant2;
    private Grant grant3;

    @BeforeEach
    void runBefore() {
        testCharity = new Charity("Micah House");
        testCharity2 = new Charity("Indwell");
        testCharity3 = new Charity("Yonge Street Mission");
        grant1 = new Grant("New Shelter", "Awarded", 10000, "Refugee Housing");
        grant2 = new Grant("Employment Program", "Rejected",
                0, "Youth Programming");
        grant3 = new Grant("Anti-Human Trafficking",
                "Awarded", 50000, "Human Traffickig");
        addGrant(grant1, testCharity3);
        addGrant(grant2, testCharity3);
        addGrant(grant3, testCharity3);

    }

    @Test
    void constructorTest() {
        assertEquals("Micah House", testCharity.getName());

    }

    @Test
    void testAddGrant() {
        addGrant(grant1, testCharity);
        ArrayList<Grant> expected = new ArrayList<>();
        expected.add(grant1);
        assertEquals(expected, testCharity.getGrants());
        addGrant(grant2, testCharity);
        expected.add(grant2);
        assertEquals(expected, testCharity.getGrants());
        addGrant(grant3, testCharity2);

    }

    @Test
    void totalFundedTest() {
        assertEquals(0,testCharity.totalFunded(testCharity));
        addGrant(grant1, testCharity);
        assertEquals(10000, testCharity.totalFunded(testCharity));
        addGrant(grant2, testCharity);
        addGrant(grant3, testCharity);
        assertEquals(60000, testCharity.totalFunded(testCharity));

    }

    @Test
    void largestGrantReceivedTest() {
        assertEquals(50000, testCharity3.largestGrantReceived(testCharity3));
        addGrant(grant1, testCharity);
        assertEquals(10000, testCharity.largestGrantReceived(testCharity));
        addGrant(grant3, testCharity);
        assertEquals(50000, testCharity.largestGrantReceived(testCharity));
        assertEquals(0, testCharity2.largestGrantReceived(testCharity2));

    }

    @Test
    void listAllGrantsTest() {
        ArrayList<Grant> expected = new ArrayList<>();
        expected.add(grant1);
        expected.add(grant3);
        assertEquals("List of grant applications :" + expected,
                     "List of grant applications :" + testCharity3.listAwardedGrants(testCharity3));
    }

}
