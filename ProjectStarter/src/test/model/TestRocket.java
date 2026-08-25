package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class TestRocket {
    Earth earth;
    Rocket testRocket;
    double mass;
    Vector displacement;
    Vector velocity;
    Vector acceleration;
    Vector thrust;

    @BeforeEach
    void setUp() {
        testRocket = new Rocket("Test");
        mass = testRocket.getMass();
    }

    @Test
    void testConstructor() {
        displacement = testRocket.getDisplacement();
        velocity = testRocket.getVel();
        acceleration = testRocket.getAccel();
        thrust = testRocket.getThrust();
        assertEquals("Test", testRocket.getName());
        assertEquals(0.0, testRocket.getAngle());
        assertEquals(0.0, displacement.getX());
        assertEquals(Earth.RADIUS, displacement.getY());
        assertEquals(0.0, displacement.getZ());
        assertEquals(0.0, velocity.getX());
        assertEquals(0.0, velocity.getY());
        assertEquals(0.0, velocity.getZ());
        assertEquals(0.0, acceleration.getX());
        assertEquals(0.0, acceleration.getY());
        assertEquals(0.0, acceleration.getZ());
        assertEquals(0.0, thrust.getX());
        assertEquals(0.0, thrust.getY());
        assertEquals(0.0, thrust.getZ());
    }

    @Test
    void testSetters() {
        testRocket.setName("Rocket");
        assertEquals("Rocket", testRocket.getName());
        testRocket.setAngle(45.5);
        assertEquals(45.5, testRocket.getAngle());

        testRocket.setThrust(new Vector(1234.5, 6789.0, 0.0));
        assertEquals(1234.5, testRocket.getThrust().getX());
        assertEquals(6789.0, testRocket.getThrust().getY());
        assertEquals(0.0, testRocket.getThrust().getZ());

        testRocket.setAccel(new Vector(5.4321, 0.9865, 0.0));
        acceleration = testRocket.getAccel();
        assertEquals(5.4321, acceleration.getX());
        assertEquals(0.9865, acceleration.getY());
        assertEquals(0.0, acceleration.getZ());

        testRocket.setVel(new Vector(12.456, 83.2546, 0.0));
        velocity = testRocket.getVel();
        assertEquals(12.456, velocity.getX());
        assertEquals(83.2546, velocity.getY());
        assertEquals(0.0, velocity.getZ());

        testRocket.setDisp(new Vector(56789.90, 54367.0, 0.0));
        displacement = testRocket.getDisplacement();
        assertEquals(56789.90, displacement.getX());
        assertEquals(54367.0, displacement.getY());
        assertEquals(0.0, displacement.getZ());
    }

    @Test
    void testTurnAngle() {
        testRocket.turnAngleBy(10.0);
        assertEquals(10.0, testRocket.getAngle());
    }

    @Test
    void testTurnAngleTwice() {
        testRocket.turnAngleBy(20.0);
        assertEquals(20.0, testRocket.getAngle());
        testRocket.turnAngleBy(46.2);
        assertEquals(66.2, testRocket.getAngle());
    }

    @Test
    void testTurnAnglePast360() {
        testRocket.turnAngleBy(270.0);
        assertEquals(270.0, testRocket.getAngle());
        testRocket.turnAngleBy(100.1);
        assertEquals(10.1, testRocket.getAngle());
    }

    @Test
    void testSetThrustAtZeroDeg() {
        testRocket.setThrust(100.0);
        Vector thrust = testRocket.getThrust();
        assertEquals(0.0, thrust.getX());
        assertEquals(100.0, thrust.getY());
    }

    @Test
    void testSetThrustAtArbAngle() {
        testRocket.turnAngleBy(45.3);
        testRocket.setThrust(100.0);
        thrust = testRocket.getThrust();
        assertEquals(100.0 * Math.sin(45.3 * Math.PI / 180.0), thrust.getX());
        assertEquals(100.0 * Math.cos(45.3 * Math.PI / 180.0), thrust.getY());
    }

    @Test
    void testSetThrustThenTurnAngle() {
        testRocket.setThrust(100.0);
        thrust = testRocket.getThrust();
        assertEquals(0.0, thrust.getX());
        assertEquals(100.0, thrust.getY());
        testRocket.turnAngleBy(30.4);
        thrust = testRocket.getThrust();
        assertEquals(100.0 * Math.sin(30.4 * Math.PI / 180.0), thrust.getX());
        assertEquals(100.0 * Math.cos(30.4 * Math.PI / 180.0), thrust.getY());
    }

    @Test
    void testIncrementNoThrust() {
        testRocket.increment();
        displacement = testRocket.getDisplacement();
        acceleration = testRocket.getAccel();
        velocity = testRocket.getVel();

        assertEquals(0.0, displacement.getX());
        assertEquals(Earth.RADIUS, displacement.getY());
        assertEquals(0.0, displacement.getZ());
        assertEquals(0.0, velocity.getX());
        assertEquals(0.0, velocity.getY());
        assertEquals(0.0, velocity.getZ());
        assertEquals(0.0, acceleration.getX());
        assertEquals(0.0, acceleration.getY());
        assertEquals(0.0, acceleration.getZ());
    }

    @Test
    void testIncrementHorizontalThrust() {
        double thrust = 10000;
        testRocket.turnAngleBy(90);
        testRocket.setThrust(thrust);
        testRocket.increment();
        displacement = testRocket.getDisplacement();
        acceleration = testRocket.getAccel();
        velocity = testRocket.getVel();

        assertEquals(thrust / mass * Earth.DT * Earth.DT, displacement.getX());
        assertEquals(Earth.RADIUS, displacement.getY());
        assertEquals(0.0, displacement.getZ());
        assertEquals(thrust / mass * Earth.DT, velocity.getX());
        assertEquals(0.0, velocity.getY());
        assertEquals(0.0, velocity.getZ());
        assertEquals(thrust / mass, acceleration.getX());
        assertEquals(0.0, acceleration.getY());
        assertEquals(0.0, acceleration.getZ());
    }

    @Test
    void testIncrementOnceVertical() {
        double thrustVal = 50000.0;
        testRocket.setThrust(thrustVal);
        testRocket.increment();
        displacement = testRocket.getDisplacement();
        acceleration = testRocket.getAccel();
        velocity = testRocket.getVel();

        checkFirstIncrement(thrustVal);
    }

    @Test
    void testIncrementTwiceVertical() {
        double thrustVal = 10000.0;
        double expectedAccelY;
        testRocket.setThrust(thrustVal);
        testRocket.increment();
        displacement = testRocket.getDisplacement();
        acceleration = testRocket.getAccel();
        velocity = testRocket.getVel();

        checkFirstIncrement(thrustVal);

        testRocket.increment();
        expectedAccelY = 1.0 / mass
                * (thrustVal - Earth.GRAV_CONSTANT_TIMES_MASS * mass / Math.pow(displacement.getNorm(), 2));
        Vector oldVel = velocity;
        Vector oldDisp = displacement;
        displacement = testRocket.getDisplacement();
        acceleration = testRocket.getAccel();
        velocity = testRocket.getVel();
        checkSecondIncrement(expectedAccelY, oldDisp, oldVel);
    }

    @Test
    void testIncrementTwiceAtAngle() {
        double thrustVal = 50000.0;
        testRocket.setThrust(thrustVal);
        testRocket.increment();
        displacement = testRocket.getDisplacement();
        acceleration = testRocket.getAccel();
        velocity = testRocket.getVel();

        checkFirstIncrement(thrustVal);

        testRocket.turnAngleBy(30);
        testRocket.increment();
        Vector oldDisplacement = displacement;
        Vector oldVelocity = velocity;
        displacement = testRocket.getDisplacement();
        acceleration = testRocket.getAccel();
        velocity = testRocket.getVel();

        checkSecondIncrementAtAngle(thrustVal, oldDisplacement, oldVelocity);
    }

    @Test
    void testRocketTrajectory() {
        Trajectory expectedTrajectory = new Trajectory(mass);
        testRocket.turnAngleBy(45);
        testRocket.setThrust(50000.0);
        testRocket.increment();
        displacement = testRocket.getDisplacement();
        acceleration = testRocket.getAccel();
        velocity = testRocket.getVel();

        double expectedAccelX = 1.0 / mass * (50000.0 * Math.sin(Math.PI / 4));
        double expectedAccelY = 1.0 / mass * (50000.0 * Math.cos(Math.PI / 4)
                - Earth.GRAV_CONSTANT_TIMES_MASS * mass / Math.pow(displacement.getNorm(), 2));
        double expectedVelX = expectedAccelX * Earth.DT;
        double expectedVelY = expectedAccelY * Earth.DT;
        Vector expectedVel = new Vector(expectedVelX, expectedVelY, 0.0);
        double expectedDispX = expectedVelX * Earth.DT;
        double expectedDispY = Earth.RADIUS + expectedVelY * Earth.DT;
        Vector expectedDisp = new Vector(expectedDispX, expectedDispY, 0.0);

        expectedTrajectory.setInitialConditions(expectedDisp, expectedVel);
        ArrayList<Double> expectedXCoords = expectedTrajectory.getXCoords();
        ArrayList<Double> expectedYCoords = expectedTrajectory.getYCoords();
        Trajectory testTrajectory = testRocket.getTrajectory();
        ArrayList<Double> actualXCoords = testTrajectory.getXCoords();
        ArrayList<Double> actualYCoords = testTrajectory.getYCoords();

        checkCoordsAllMatch(expectedXCoords, expectedYCoords, actualXCoords, actualYCoords);
    }

    private double checkFirstIncrement(double thrustVal) {
        double expectedAccelY = 1.0 / mass
                * (thrustVal - Earth.GRAV_CONSTANT_TIMES_MASS * mass / Math.pow(displacement.getNorm(), 2));
        assertEquals(0.0, acceleration.getX());
        assertTrue(Math.abs(expectedAccelY - acceleration.getY()) < 0.001);
        assertEquals(0.0, velocity.getX());
        assertTrue(Math.abs(expectedAccelY * Earth.DT - velocity.getY()) < 0.001);
        assertEquals(0.0, displacement.getX());
        assertTrue(Math.abs(
                Earth.RADIUS + (0.0 + expectedAccelY * Earth.DT) * Earth.DT - displacement.getY()) < 0.001);

        return expectedAccelY;
    }

    private void checkSecondIncrement(double expectedAccelY, Vector oldDisp, Vector oldVel) {
        assertEquals(0.0, acceleration.getX());
        assertEquals(expectedAccelY, acceleration.getY());
        assertEquals(0.0, velocity.getX());
        assertEquals(oldVel.getY() + expectedAccelY * Earth.DT, velocity.getY());
        assertEquals(0.0, displacement.getX());
        assertTrue(relativeError(oldDisp.getY() + expectedAccelY * Earth.DT * Earth.DT,
                displacement.getY()) < 0.00001);
    }

    private void checkSecondIncrementAtAngle(double thrustVal, Vector oldDisplacement, Vector oldVelocity) {
        double expectedAccelX = 1.0 / mass * (thrustVal * Math.sin(Math.PI / 6));
        double expectedAccelY = 1.0 / mass * (thrustVal * Math.cos(Math.PI / 6)
                - Earth.GRAV_CONSTANT_TIMES_MASS * mass / Math.pow(displacement.getNorm(), 2));
        assertTrue(Math.abs(expectedAccelX - acceleration.getX()) < 0.001);
        assertTrue(Math.abs(expectedAccelY - acceleration.getY()) < 0.001);
        assertTrue(
                Math.abs(oldVelocity.getX() + expectedAccelX * Earth.DT - velocity.getX()) < 0.001);
        assertTrue(
                Math.abs(oldVelocity.getY() + expectedAccelY * Earth.DT - velocity.getY()) < 0.001);
        assertTrue(Math.abs(oldDisplacement.getX() + velocity.getX() * Earth.DT
                - displacement.getX()) < 0.001);
        assertTrue(Math.abs(oldDisplacement.getY() + velocity.getY() * Earth.DT
                - displacement.getY()) < 0.001);
    }

    private void checkCoordsAllMatch(ArrayList<Double> expectedXCoords, ArrayList<Double> expectedYCoords,
            ArrayList<Double> actualXCoords, ArrayList<Double> actualYCoords) {
        for (int i = 0; i < expectedXCoords.size(); i++) {
            assertTrue(relativeError(expectedXCoords.get(0), actualXCoords.get(0)) < 0.0001);
            assertTrue(relativeError(expectedYCoords.get(0), actualYCoords.get(0)) < 0.001);
        }
    }

    private double relativeError(double expected, double actual) {
        return Math.abs(actual - expected) / expected;
    }

}
