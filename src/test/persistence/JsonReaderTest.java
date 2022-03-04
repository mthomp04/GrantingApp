package persistence;

import model.Charity;
import model.Foundation;
import model.Grant;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("/.data/noSuchFile.json");
        try {
            Foundation foundation = reader.read();
            fail("IOException expected");

        } catch (IOException e) {
           // pass;
        }
    }

    @Test
    void testReaderEmptyFoundation() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFoundation.json");
        try {
            Foundation foundation = reader.read();
            assertEquals(charities, foundation.getCharityList());
            assertEquals(grants, foundation.getGrants());
            assertEquals(0, foundation.getFundsAvailable());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }

    }

    @Test
    void testReaderGeneralFoundation() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralFoundation.json");
        try {
            Foundation foundation = reader.read();
            charities.add(new Charity("Micah House"));
            charities.add(new Charity("UBC Foundation"));
            grants2.add(new Grant("Housing", Grant.Status.AWARDED, 60));
            grants2.add(new Grant("Employment", Grant.Status.REJECTED, 800));
            assertEquals(1000, foundation.getFundsAvailable());
            checkCharity("Micah House", grants, charities.get(0));
            checkCharity("UBC Foundation", grants2, charities.get(1));
            assertEquals(2, charities.size());
            checkGrant("Housing", Grant.Status.AWARDED, 60, grants2.get(0));
            checkGrant("Employment", Grant.Status.REJECTED, 800, grants2.get(1));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
