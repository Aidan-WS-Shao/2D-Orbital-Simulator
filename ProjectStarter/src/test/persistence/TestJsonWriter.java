package persistence;

import model.Earth;
import model.Rocket;
import model.Vector;

import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Source: CPSC 210 Json sample
//         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
@ExcludeFromJacocoGeneratedReport
public class TestJsonWriter extends JsonTest {
    // NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter
    // is to write data to a file and then use the reader to read it back in and check
    // that we read in a copy of what was written out.
    List<Rocket> rockets;
    double thrustVal;
    double mass;

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyEarth() {
        try {
            Earth earth = new Earth("Write Earth Empty");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyEarth.json");
            writer.open();
            writer.write(earth);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyEarth.json");
            earth = reader.read();
            assertEquals("Write Earth Empty", earth.getName());
            assertTrue(earth.getCurrentRocket() == null);
            assertTrue(earth.getRockets().isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralEarth() {
        try {
            Earth earth = new Earth("Write Earth General");
            setUpForGeneralWrite(earth);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralEarth.json");
            writer.open();
            writer.write(earth);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralEarth.json");
            earth = reader.read();
            rockets = earth.getRockets();
            assertEquals("Write Earth General", earth.getName());
            assertEquals("N1 Rocket", earth.getCurrentRocket().getName());
            assertEquals(2, rockets.size());
            testBothRockets();
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private void setUpForGeneralWrite(Earth earth) {
        thrustVal = 10000.0;
        mass = 1000.0;
        try {
            earth.addRocket("Saturn V");
            earth.getRockets().get(0).setThrust(thrustVal);
            earth.increment();
            earth.addRocket("N1 Rocket");
        } catch (Exception e) {
            fail("Exception should not have been caught");
        }
        earth.getRockets().get(1).setThrust(thrustVal);
        earth.increment();
    }

    // Assumes thrust is large enough for rocket to take off
    private void testBothRockets() {
        Vector expectedDisp0 = new Vector(0.0, Earth.RADIUS, 0.0);
        Vector expectedVel0 = new Vector(0.0, 0.0, 0.0);
        double r = expectedDisp0.getNorm();
        double forceOfGrav = Earth.GRAV_CONSTANT_TIMES_MASS * mass / Math.pow(r, 2);

        Vector expectedThrust = new Vector(0, thrustVal, 0.0);

        // Expected data after 1 increment
        Vector expectedAccel1 = expectedThrust.subtractVector(expectedDisp0.unit().scale(forceOfGrav)).scale(1 / mass);
        Vector expectedVel1 = expectedVel0.addVector(expectedAccel1.scale(Earth.DT));
        Vector expectedDisp1 = expectedDisp0.addVector(expectedVel1.scale(Earth.DT));

        // Expected data after 2 increments
        r = expectedDisp1.getNorm();
        forceOfGrav = Earth.GRAV_CONSTANT_TIMES_MASS * mass / Math.pow(r, 2);
        Vector expectedAccel2 = expectedThrust.subtractVector(expectedDisp1.unit().scale(forceOfGrav)).scale(1 / mass);
        Vector expectedVel2 = expectedVel1.addVector(expectedAccel2.scale(Earth.DT));
        Vector expectedDisp2 = expectedDisp1.addVector(expectedVel2.scale(Earth.DT));

        checkRocket("Saturn V", mass, 0.0, expectedThrust, expectedDisp2, expectedVel2, expectedAccel2,
                rockets.get(0));
        checkRocket("N1 Rocket", mass, 0.0, expectedThrust, expectedDisp1, expectedVel1, expectedAccel1,
                rockets.get(1));
    }
}
