package persistence;

import model.Charity;
import model.Foundation;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Foundation foundation = new Foundation();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyFoundation() {
        try {
            Foundation foundation = new Foundation();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyFoundation.json");
            writer.open();
            writer.write(foundation);
            writer.close();
        }
        catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Foundation foundation = new Foundation();
            foundation.addCharity(new Charity("Micah House"));
            foundation.addCharity(new Charity("UBC Foundation"));
            foundation.addOrRemoveFunds(1000);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralFoundation.json");
            writer.open();
            writer.write(foundation);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralFoundation.json");
            foundation = reader.read();
            List<Charity> charities = foundation.getCharityList();
            assertEquals(2, charities.size());
            assertEquals(1000, foundation.getFundsAvailable());
            checkCharity("Micah House", grants, charities.get(0));
            checkCharity("UBC Foundation", grants, charities.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
