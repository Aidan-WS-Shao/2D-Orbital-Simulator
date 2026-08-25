package persistence;

import model.Earth;
import model.Rocket;
import model.Vector;

import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport
// Source: CPSC 210 Json sample
//         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
public class TestJsonReader extends JsonTest {
    List<Rocket> rockets;
    double thrustVal;
    double mass;

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyEarth.json");
        try {
            Earth earth = reader.read();
            assertEquals("Read Earth Empty", earth.getName());
            assertTrue(earth.getCurrentRocket() == null);
            assertTrue(earth.getRockets().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralEarth.json");
        try {
            Earth earth = reader.read();
            thrustVal = 50000.0;
            mass = 1000.0;
            rockets = earth.getRockets();
            assertEquals("Read Earth General", earth.getName());
            assertEquals("Discovery", earth.getCurrentRocket().getName());
            assertEquals(2, rockets.size());
            testBothRockets();
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    // Assumes thrust is large enough for rocket to take off
    private void testBothRockets() {
        Vector expectedThrust1 = new Vector(2000,50000,0);
        Vector expectedDisp1 = new Vector(32341.2415, 6824951.5219,0);
        Vector expectedVel1 = new Vector(13.52525343,4.020155042308783,0);
        Vector expectedAccel1 = new Vector(41.1241241, 40.20155150390854,0);

        Vector expectedThrust2 = new Vector(4000,50000,0);
        Vector expectedDisp2 = new Vector(-2352.11235132, 3185490.23515,0);
        Vector expectedVel2 = new Vector(23.2353446,57.3542535,0);
        Vector expectedAccel2 = new Vector(3.235123, 14.235462,0);
        checkRocket("Atlas V", 1000, 22, expectedThrust1, expectedDisp1, expectedVel1, expectedAccel1,
                rockets.get(0));
        checkRocket("Discovery", mass, -34, expectedThrust2, expectedDisp2, expectedVel2, expectedAccel2,
                rockets.get(1));
    }
}
