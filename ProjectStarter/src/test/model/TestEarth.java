package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.exceptions.NameTakenException;
import model.exceptions.RocketOnLaunchPadException;

@ExcludeFromJacocoGeneratedReport
public class TestEarth {
    Earth testEarth;
    Rocket rocket1;
    Rocket rocket2;
    Vector displacement;
    Vector velocity;
    Vector acceleration;
    Vector thrust;

    @BeforeEach
    void setup() {
        testEarth = new Earth("Earth");
    }

    @Test
    void testConstructor() {
        assertEquals("Earth", testEarth.getName());
        assertTrue(testEarth.getRockets().isEmpty());
        assertTrue(testEarth.getCurrentRocket() == null);
    }

    @Test
    void testSetName() {
        testEarth.setName("New Earth");
        assertEquals("New Earth", testEarth.getName());
    }

    private void addFirstRocketByName() {
        try {
            testEarth.addRocket("Saturn V");
        } catch (Exception e) {
            fail("Exception should not have been caught");
        }
        assertEquals(1, testEarth.getRockets().size());
        rocket1 = testEarth.getRockets().get(0);
        assertEquals("Saturn V", rocket1.getName());
        assertEquals("Saturn V", testEarth.getCurrentRocket().getName());
        displacement = rocket1.getDisplacement();
        velocity = rocket1.getVel();
        acceleration = rocket1.getAccel();
        thrust = rocket1.getThrust();
        checkInitialConds();
    }

    private void addSecondRocketByNameSuccess() {
        try {
            testEarth.addRocket(new Rocket("N1 Rocket"));
        } catch (Exception e) {
            fail("Exception should not have been caught");
        }
        assertEquals("N1 Rocket", testEarth.getCurrentRocket().getName());
        assertEquals(2, testEarth.getRockets().size());
        rocket1 = testEarth.getRockets().get(0);
        assertEquals("Saturn V", rocket1.getName());
        rocket2 = testEarth.getRockets().get(1);
        assertEquals("N1 Rocket", rocket2.getName());
        displacement = rocket2.getDisplacement();
        velocity = rocket2.getVel();
        acceleration = rocket2.getAccel();
        thrust = rocket2.getThrust();
        checkInitialConds();
    }

    @Test
    void testAddOneRocketByName() {
        addFirstRocketByName();
        assertEquals(rocket1, testEarth.getCurrentRocket());
        assertTrue(testEarth.setCurrentRocket("Saturn V"));
        assertEquals(rocket1, testEarth.getCurrentRocket());
        assertFalse(testEarth.setCurrentRocket("N1 Rocket"));
    }

    @Test
    void testAddAndRemoveOneRocketByName() {
        addFirstRocketByName();
        assertEquals(rocket1, testEarth.getCurrentRocket());
        assertTrue(testEarth.setCurrentRocket("Saturn V"));
        assertEquals(rocket1, testEarth.getCurrentRocket());
        assertFalse(testEarth.setCurrentRocket("N1 Rocket"));
        assertTrue(testEarth.removeRocket("Saturn V"));
        assertTrue(testEarth.getRockets().isEmpty());
        assertTrue(testEarth.getCurrentRocket() == null);
        assertFalse(testEarth.removeRocket("N1 Rocket"));
    }

    @Test
    void testRemoveRocketFromEmptyEarth() {
        assertFalse(testEarth.removeRocket("Saturn V"));
        assertFalse(testEarth.removeRocket("N1 Rocket"));
    }

    @Test
    void testAddOneRocketAlreadyIncremented() {
        rocket1 = new Rocket("Saturn V");
        rocket1.setDisp(new Vector(1234.5,123456.8,0.0));
        rocket1.setVel(new Vector(12.345,67.890,0.0));
        rocket1.setAccel(new Vector(1.2345,6.789,0.0));
        rocket1.setThrust(new Vector(1234.5,6789.0,0.0));
        try {
            testEarth.addRocket(rocket1);
        } catch (Exception e) {
            fail("Exception should not have been caught");
        }
        assertEquals(1, testEarth.getRockets().size());
        assertEquals("Saturn V", testEarth.getRockets().get(0).getName());

        assertEquals(1234.5, rocket1.getDisplacement().getX());
        assertEquals(123456.8, rocket1.getDisplacement().getY());
        assertEquals(12.345, rocket1.getVel().getX());
        assertEquals(67.890, rocket1.getVel().getY());
        assertEquals(1.2345, rocket1.getAccel().getX());
        assertEquals(6.789, rocket1.getAccel().getY());
        assertEquals(1234.5, rocket1.getThrust().getX());
        assertEquals(6789.0, rocket1.getThrust().getY());
    }

    @Test
    void testAddTwoRocketsByNameOneFailRocketOnLaunchException() {
        addFirstRocketByName();
        try {
            testEarth.addRocket("N1 Rocket");
            fail("Exception should have been caught");
        } catch (NameTakenException nte) {
            fail("This exception should not have been caught");
        } catch (RocketOnLaunchPadException rolpe) {
            assertEquals(1, testEarth.getRockets().size());
            rocket1 = testEarth.getRockets().get(0);
            assertEquals("Saturn V", rocket1.getName());
            assertEquals(rocket1, testEarth.getCurrentRocket());
            assertTrue(testEarth.setCurrentRocket("Saturn V"));
            assertEquals(rocket1, testEarth.getCurrentRocket());
            assertFalse(testEarth.setCurrentRocket("N1 Rocket"));
        }
    }

    @Test
    void testAddTwoRocketsByRocketOneFailRocketOnLaunchException() {
        try {
            testEarth.addRocket(new Rocket("Saturn V"));
        } catch (Exception e) {
            fail("Exception should not have been caught");
        }
        try {
            testEarth.addRocket(new Rocket("N1 Rocket"));
            fail("Exception should have been caught");
        } catch (NameTakenException nte) {
            fail("This exception should not have been caught");
        } catch (RocketOnLaunchPadException rolpe) {
            assertEquals(1, testEarth.getRockets().size());
            rocket1 = testEarth.getRockets().get(0);
            assertEquals("Saturn V", rocket1.getName());
        }
    }

    @Test
    void testAddTwoRocketsByNameOneFailNameTakenException() {
        addFirstRocketByName();
        try {
            testEarth.addRocket("Saturn V");
            fail("Exception should have been caught");
        } catch (RocketOnLaunchPadException rolpe) {
            fail("This exception should not have been caught");
        } catch (NameTakenException nte) {
            assertEquals(1, testEarth.getRockets().size());
            rocket1 = testEarth.getRockets().get(0);
            assertEquals("Saturn V", rocket1.getName());
            assertEquals(rocket1, testEarth.getCurrentRocket());
            assertTrue(testEarth.setCurrentRocket("Saturn V"));
            assertEquals(rocket1, testEarth.getCurrentRocket());
            assertFalse(testEarth.setCurrentRocket("N1 Rocket"));
        }
    }

    @Test
    void testAddTwoRocketsByRocketOneFailNameTakenException() {
        try {
            testEarth.addRocket(new Rocket("Saturn V"));
        } catch (Exception e) {
            fail("Exception should not have been caught");
        }
        try {
            testEarth.addRocket(new Rocket("Saturn V"));
            fail("Exception should have been caught");
        } catch (RocketOnLaunchPadException rolpe) {
            fail("This exception should not have been caught");
        } catch (NameTakenException nte) {
            assertEquals(1, testEarth.getRockets().size());
            rocket1 = testEarth.getRockets().get(0);
            assertEquals("Saturn V", rocket1.getName());
            assertEquals(rocket1, testEarth.getCurrentRocket());
            assertTrue(testEarth.setCurrentRocket("Saturn V"));
            assertEquals(rocket1, testEarth.getCurrentRocket());
            assertFalse(testEarth.setCurrentRocket("N1 Rocket"));
        }
    }

    @Test
    void testAddTwoRocketsByNameNoExceptions() {
        addFirstRocketByName();
        rocket1.setThrust(10000.0);
        rocket1.increment();
        addSecondRocketByNameSuccess();
        assertTrue(testEarth.setCurrentRocket("Saturn V"));
        assertEquals(rocket1, testEarth.getCurrentRocket());
        assertTrue(testEarth.setCurrentRocket("N1 Rocket"));
        assertEquals(rocket2, testEarth.getCurrentRocket());
    }

    @Test
    void testAddTwoRocketsByNameNoExceptionsRemoveFirstRocket() {
        addFirstRocketByName();
        rocket1.setThrust(10000.0);
        rocket1.increment();
        addSecondRocketByNameSuccess();
        assertTrue(testEarth.setCurrentRocket("Saturn V"));
        assertEquals(rocket1, testEarth.getCurrentRocket());
        assertTrue(testEarth.setCurrentRocket("N1 Rocket"));
        assertEquals(rocket2, testEarth.getCurrentRocket());
        assertTrue(testEarth.removeRocket("Saturn V"));
        assertEquals(1, testEarth.getRockets().size());
        assertEquals(rocket2, testEarth.getRockets().get(0));
        assertEquals(rocket2, testEarth.getCurrentRocket());
    }

    @Test
    void testAddTwoRocketsByNameNoExceptionsRemoveSecondRocket() {
        addFirstRocketByName();
        rocket1.setThrust(10000.0);
        rocket1.increment();
        addSecondRocketByNameSuccess();
        assertTrue(testEarth.setCurrentRocket("Saturn V"));
        assertEquals(rocket1, testEarth.getCurrentRocket());
        assertTrue(testEarth.setCurrentRocket("N1 Rocket"));
        assertEquals(rocket2, testEarth.getCurrentRocket());
        assertTrue(testEarth.removeRocket("N1 Rocket"));
        assertEquals(1, testEarth.getRockets().size());
        assertEquals(rocket1, testEarth.getRockets().get(0));
        assertEquals(rocket1, testEarth.getCurrentRocket());
    }
    
    @Test
    void testAddTwoRocketsByNameNoExceptionsRemoveBothRockets() {
        addFirstRocketByName();
        rocket1.setThrust(10000.0);
        rocket1.increment();
        addSecondRocketByNameSuccess();
        assertTrue(testEarth.setCurrentRocket("Saturn V"));
        assertEquals(rocket1, testEarth.getCurrentRocket());
        assertTrue(testEarth.setCurrentRocket("N1 Rocket"));
        assertEquals(rocket2, testEarth.getCurrentRocket());
        assertTrue(testEarth.removeRocket("Saturn V"));
        assertTrue(testEarth.removeRocket("N1 Rocket"));
        assertTrue(testEarth.getRockets().isEmpty());
        assertTrue(testEarth.getCurrentRocket() == null);
    }

    @Test
    void testAddTwoRocketsByRocketNoExceptions() {
        rocket1 = new Rocket("Saturn V");
        rocket1.setDisp(new Vector(1234.5,123456.8,0.0));
        rocket1.setVel(new Vector(12.345,67.890,0.0));
        try {
            testEarth.addRocket(rocket1);
            assertEquals(1, testEarth.getRockets().size());
            assertEquals("Saturn V", testEarth.getRockets().get(0).getName());
            testEarth.addRocket(new Rocket("N1 Rocket"));
        } catch (Exception e) {
            fail("Exception should not have been caught");
        }
        assertEquals(2, testEarth.getRockets().size());
        rocket1 = testEarth.getRockets().get(0);
        assertEquals("Saturn V", rocket1.getName());
        rocket2 = testEarth.getRockets().get(1);
        assertEquals("N1 Rocket", rocket2.getName());
        displacement = rocket2.getDisplacement();
        velocity = rocket2.getVel();
        acceleration = rocket2.getAccel();
        thrust = rocket2.getThrust();
        checkInitialConds();
    }

    @Test
    void testAddTwoRocketsByNameWithXGreaterThanZeroEqualRadiusY() {
        addFirstRocketByName();
        rocket1.setDisp(new Vector(1000.0,Earth.RADIUS,0.0));
        addSecondRocketByNameSuccess();
    }

    @Test
    void testAddTwoRocketsByNameWithXLessThanZeroEqualRadiusY() {
        addFirstRocketByName();
        rocket1.setDisp(new Vector(-1000.0,Earth.RADIUS,0.0));
        addSecondRocketByNameSuccess();
    }

    @Test
    void testAddTwoRocketsByNameWithEqualZeroXGreaterThanRadius() {
        addFirstRocketByName();
        rocket1.setDisp(new Vector(0.0,122345678.9,0.0));
        addSecondRocketByNameSuccess();
    }

    @Test
    void testAddTwoRocketsByNameWithEqualZeroXLessThanRadius() {
        addFirstRocketByName();
        rocket1.setDisp(new Vector(0.0,-658786322.235,0.0));
        addSecondRocketByNameSuccess();
    }

    @Test
    void testAddTwoRocketsByNameWithLessThanZeroXLessThanRadiusY() {
        addFirstRocketByName();
        rocket1.setDisp(new Vector(-238457.0,-36823235,0.0));
        addSecondRocketByNameSuccess();
    }

    @Test
    void testAddTwoRocketsByNameWithLessThanZeroXGreaterThanRadiusY() {
        addFirstRocketByName();
        rocket1.setDisp(new Vector(-785670.0,13252345,0.0));
        addSecondRocketByNameSuccess();
    }

    @Test
    void testAddTwoRocketsByNameWithGreaterThanZeroXLessThanRadiusY() {
        addFirstRocketByName();
        rocket1.setDisp(new Vector(2532556.0,-23243463.1235,0.0));
        addSecondRocketByNameSuccess();
    }
    
    @Test
    void testAddTwoRocketsByNameWithGreaterThanZeroXGreaterThanRadiusY() {
        addFirstRocketByName();
        rocket1.setDisp(new Vector(-785670.0,13252345,0.0));
        addSecondRocketByNameSuccess();
    }

    @Test
    void testRocketOnLaunchPadButNameTaken() {
        addFirstRocketByName();
        try {
            testEarth.addRocket("Saturn V");
            fail("Exception should have been caught");
        } catch (RocketOnLaunchPadException rolpe) {
            fail("Name Exception should have been caught first");
        } catch (NameTakenException nte) {
            assertEquals(1, testEarth.getRockets().size());
            rocket1 = testEarth.getRockets().get(0);
            assertEquals("Saturn V", rocket1.getName());
            assertEquals(rocket1, testEarth.getCurrentRocket());
            assertTrue(testEarth.setCurrentRocket("Saturn V"));
            assertEquals(rocket1, testEarth.getCurrentRocket());
        }
    }

    @Test
    void testAddTwoRocketsByRocketWithEqualsRadiusY() {
        rocket1 = new Rocket("Saturn V");
        rocket1.setDisp(new Vector(1234.5,123456.8,0.0));
        rocket1.setVel(new Vector(12.345,67.890,0.0));
        rocket1.setAccel(new Vector(1.2345,6.789,0.0));
        rocket1.setThrust(new Vector(1234.5,6789.0,0.0));
        try {
            testEarth.addRocket(rocket1);
        } catch (Exception e) {
            fail("Exception should not have been caught");
        }
        assertEquals(1, testEarth.getRockets().size());
        assertEquals("Saturn V", testEarth.getRockets().get(0).getName());
        rocket1.setDisp(new Vector(1000.0,Earth.RADIUS,0.0));
        addSecondRocketByNameSuccess();
    }

    @Test
    void testIncrement() {
        double mass;
        double thrustVal = 10000.0;
        mass = setUpTestIncrement(thrustVal);

        displacement = rocket1.getDisplacement();
        acceleration = rocket1.getAccel();
        velocity = rocket1.getVel();

        double expectedAccel2 = 1.0 / mass
                * (thrustVal - Earth.GRAV_CONSTANT_TIMES_MASS * mass / Math.pow(displacement.getNorm(), 2));

        checkAfterIncrementConditions(expectedAccel2, new Vector(0.0,Earth.RADIUS,0.0), new Vector(0.0,0.0,0.0));

        double expectedAccel1 = expectedAccel2;
        Vector oldDisp = displacement;
        Vector oldVel = velocity;
        expectedAccel2 = 1.0 / mass
                * (thrustVal - Earth.GRAV_CONSTANT_TIMES_MASS * mass / Math.pow(displacement.getNorm(), 2));

        testEarth.increment();
        displacement = rocket1.getDisplacement();
        acceleration = rocket1.getAccel();
        velocity = rocket1.getVel();
        checkAfterIncrementConditions(expectedAccel2, oldDisp, oldVel);

        displacement = rocket2.getDisplacement();
        acceleration = rocket2.getAccel();
        velocity = rocket2.getVel();
        checkAfterIncrementConditions(expectedAccel1, new Vector(0.0,Earth.RADIUS,0.0), new Vector(0.0,0.0,0.0));
    }

    @Test
    void testAddThreeRocketsByNameOneFailRocketOnLaunchException() {
        addFirstRocketByName();
        rocket1.setThrust(10000.0);
        rocket1.increment();
        addSecondRocketByNameSuccess();
        try {
            testEarth.addRocket("Atlas V");
            fail("Exception should have been caught");
        } catch (NameTakenException nte) {
            fail("This exception should not have been caught");
        } catch (RocketOnLaunchPadException rolpe) {
            assertEquals(2, testEarth.getRockets().size());
            rocket1 = testEarth.getRockets().get(0);
            rocket2 = testEarth.getRockets().get(1);
            assertEquals("Saturn V", rocket1.getName());
            assertEquals("N1 Rocket", rocket2.getName());
        }
    }

    @Test
    void testAddThreeRocketsByNameNoExceptionsRemoveFirstRocket() {
        addFirstRocketByName();
        rocket1.setThrust(10000.0);
        rocket1.increment();
        addSecondRocketByNameSuccess();
        rocket2.setThrust(10000.0);
        rocket2.increment();
        try {
            testEarth.addRocket("Atlas V");
        } catch (Exception e) {
            fail("Exception should have been caught");
        }
        Rocket rocket3 = testEarth.getRockets().get(2);
        testEarth.removeRocket("Saturn V");
        assertEquals(2, testEarth.getRockets().size());
        assertEquals(rocket2, testEarth.getRockets().get(0));
        assertEquals(rocket3, testEarth.getRockets().get(1));
        assertEquals(rocket3, testEarth.getCurrentRocket());
    }

    @Test
    void testAddThreeRocketsByNameNoExceptionsRemoveSecondRocket() {
        addFirstRocketByName();
        rocket1.setThrust(10000.0);
        rocket1.increment();
        addSecondRocketByNameSuccess();
        rocket2.setThrust(10000.0);
        rocket2.increment();
        try {
            testEarth.addRocket("Atlas V");
        } catch (Exception e) {
            fail("Exception should have been caught");
        }
        Rocket rocket3 = testEarth.getRockets().get(2);
        testEarth.removeRocket("N1 Rocket");
        assertEquals(2, testEarth.getRockets().size());
        assertEquals(rocket1, testEarth.getRockets().get(0));
        assertEquals(rocket3, testEarth.getRockets().get(1));
        assertEquals(rocket3, testEarth.getCurrentRocket());
    }

    @Test
    void testAddThreeRocketsByNameNoExceptionsRemoveThirdRocket() {
        addFirstRocketByName();
        rocket1.setThrust(10000.0);
        rocket1.increment();
        addSecondRocketByNameSuccess();
        rocket2.setThrust(10000.0);
        rocket2.increment();
        try {
            testEarth.addRocket("Atlas V");
        } catch (Exception e) {
            fail("Exception should have been caught");
        }
        testEarth.removeRocket("Atlas V");
        assertEquals(2, testEarth.getRockets().size());
        assertEquals(rocket1, testEarth.getRockets().get(0));
        assertEquals(rocket2, testEarth.getRockets().get(1));
        assertEquals(rocket1, testEarth.getCurrentRocket());
    }

    private void checkInitialConds() {
        assertEquals(0.0, rocket1.getAngle());
        assertEquals(0.0, displacement.getX());
        assertEquals(Earth.RADIUS, displacement.getY());
        assertEquals(0.0, velocity.getNorm());
        assertEquals(0.0, acceleration.getNorm());
        assertEquals(0.0, thrust.getNorm());
    }

    private double setUpTestIncrement(double thrustVal) {
        try {
            testEarth.addRocket("Saturn V");
        } catch (Exception e) {
            fail("Exception should not have been caught");
        }
        assertEquals(1, testEarth.getRockets().size());
        rocket1 = testEarth.getRockets().get(0);
        rocket1.setThrust(thrustVal);
        rocket1.increment();
        try {
            testEarth.addRocket("N1 Rocket");
        } catch (Exception e) {
            fail("Exception should not have been caught");
        }
        rocket2 = testEarth.getRockets().get(1);
        rocket2.setThrust(thrustVal);
        return rocket1.getMass();
    }

    private void checkAfterIncrementConditions(double expectedAccel, Vector oldDisp, Vector oldVel) {
        assertEquals(0.0, acceleration.getX());
        assertTrue(relativeError(expectedAccel, acceleration.getY()) < 0.001);
        assertEquals(0.0, velocity.getX());
        assertTrue(relativeError(oldVel.getY() + expectedAccel * Earth.DT, velocity.getY()) < 0.001);
        assertEquals(0.0, displacement.getX());
        assertTrue(relativeError(
                oldDisp.getY() + (expectedAccel * Earth.DT * Earth.DT), displacement.getY()) < 0.001);
    }

    private double relativeError(double expected, double actual) {
        return Math.abs(actual - expected) / expected;
    }

}
