package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class TestTrajectory {
    Trajectory testTrajectory;
    Vector radiusVector;
    Vector velocityVector;
    double mass;
    double radX;
    double radY;
    double radZ;
    double velX;
    double velY;
    double velZ;

    Vector expectedMomemtum;
    double expectedSemiMajor;
    double expectedEnergy;
    double denom;
    double expectedEccentricity;
    double expectedInitRadVel;
    double expectedInitAngVel;
    double expectedInitTheta;

    @BeforeEach
    void runBefore() {
        mass = 50000.0;
        radX = 9643700.0;
        radY = 9415200.0;
        radZ = 0;
        velX = 100.0;
        velY = -3000.0;
        velZ = 0;
        testTrajectory = new Trajectory(mass);
        radiusVector = new Vector(radX, radY, radZ);
        velocityVector = new Vector(velX, velY, velZ);
        testTrajectory.setInitialConditions(radiusVector, velocityVector);
    }

    @Test
    void testConstructor() {
        testTrajectory = new Trajectory(100.0);
        ArrayList<Double> thetas;
        assertTrue(testTrajectory.getMass() == 100.0);
        thetas = testTrajectory.getThetas();
        for (int i = 0; i < Trajectory.NUMBER_OF_POINTS; i++) {
            assertTrue(thetas.get(i) == 2.0 * Math.PI / Trajectory.NUMBER_OF_POINTS * i);
        }
        assertTrue(testTrajectory.getXCoords().isEmpty());
        assertTrue(testTrajectory.getYCoords().isEmpty());
    }

    @Test
    void testSetInitialCond() {
        Vector testRadVector = testTrajectory.getInitRadVector();
        Vector testVelVector = testTrajectory.getInitVelVector();
        assertEquals(radX, testRadVector.getX());
        assertEquals(radY, testRadVector.getY());
        assertEquals(radZ, testRadVector.getZ());
        assertEquals(velX, testVelVector.getX());
        assertEquals(velY, testVelVector.getY());
        assertEquals(velZ, testVelVector.getZ());
    }

    @Test
    void testMomentum() {
        testTrajectory.calcMomentum();
        Vector momentumVector = testTrajectory.getMomentum();
        assertEquals(mass * (radY * velZ - radZ * velY), momentumVector.getX());
        assertEquals(mass * -(radX * velZ - radZ * velX), momentumVector.getY());
        assertEquals(mass * (radX * velY - radY * velX), momentumVector.getZ());
    }

    @Test
    void testSemiMajorAxis() {
        testTrajectory.calcSemiMajorAxis();
        double semiMajor = testTrajectory.getSemiMajor();
        double radius = testTrajectory.getInitRadVector().getNorm();
        double velocity = testTrajectory.getInitVelVector().getNorm();
        assertEquals(1.0 / (2.0 / radius - Math.pow(velocity, 2) / Earth.GRAV_CONSTANT_TIMES_MASS), semiMajor);
    }

    @Test
    void testEnergy() {
        testTrajectory.calcSemiMajorAxis();
        testTrajectory.calcEnergy();
        double mass = testTrajectory.getMass();
        double semiMajor = testTrajectory.getSemiMajor();
        double energy = testTrajectory.getEnergy();
        assertEquals(-Earth.GRAV_CONSTANT_TIMES_MASS * mass / (2.0 * semiMajor), energy);
    }

    @Test
    void testEccentricity() {
        testTrajectory.calcMomentum();
        testTrajectory.calcSemiMajorAxis();
        testTrajectory.calcEnergy();
        testTrajectory.calcEccentricity();
        double momentum = testTrajectory.getMomentum().getNorm();
        double mass = testTrajectory.getMass();
        double energy = testTrajectory.getEnergy();
        double denom = Math.pow(Earth.GRAV_CONSTANT_TIMES_MASS, 2) * Math.pow(mass, 3);
        assertEquals(Math.sqrt(1.0 + 2.0 * energy * Math.pow(momentum, 2) / denom), testTrajectory.getEccentricity());
    }

    @Test
    void testInitRadVel() {
        testTrajectory.calcInitRadVel();
        double initRadVel = testTrajectory.getInitRadVel();
        assertEquals((velX * radX + velY * radY + velZ * radZ) / radiusVector.getNorm(), initRadVel);
    }

    @Test
    void testInitAngVel() {
        testTrajectory.calcInitRadVel();
        testTrajectory.calcInitAngVel();
        double initVel = testTrajectory.getInitVelVector().getNorm();
        double initRadVel = testTrajectory.getInitRadVel();
        double initAngVel = testTrajectory.getInitAngVel();
        assertEquals(Math.sqrt(Math.pow(initVel, 2) - Math.pow(initRadVel, 2)), initAngVel);
    }

    @Test
    void testInitThetaCase1() {
        radiusVector.setVector(new Vector(-3.0, -2.0, -1.0));
        velocityVector.setVector(new Vector(-1.0 / 2, 1.0 / 3, 1.0 / 4));
        testTrajectory.setInitialConditions(radiusVector, velocityVector);
        calculateTrajectoryValuesFromTrajectory();
        double mom = testTrajectory.getMomentum().getNorm();
        double semiMaj = testTrajectory.getSemiMajor();
        double mass = testTrajectory.getMass();
        double eccen = testTrajectory.getEccentricity();
        double initAngVel = testTrajectory.getInitAngVel();

        double cosTheta = (initAngVel * mass * semiMaj / mom * (1 - Math.pow(eccen, 2)) - 1.0) / eccen;
        assertEquals(Math.acos(cosTheta), testTrajectory.getInitTheta());
    }

    @Test
    void testInitThetaCase2() {
        radiusVector.setVector(new Vector(1.0, 2.0, 3.0));
        velocityVector.setVector(new Vector(1.0 / 2, 1.0 / 3, 1.0 / 4));
        testTrajectory.setInitialConditions(radiusVector, velocityVector);
        calculateTrajectoryValuesFromTrajectory();
        double mom = testTrajectory.getMomentum().getNorm();
        double semiMaj = testTrajectory.getSemiMajor();
        double mass = testTrajectory.getMass();
        double eccen = testTrajectory.getEccentricity();
        double initAngVel = testTrajectory.getInitAngVel();

        double cosTheta = (initAngVel * mass * semiMaj / mom * (1 - Math.pow(eccen, 2)) - 1.0) / eccen;
        assertEquals(Math.acos(cosTheta), testTrajectory.getInitTheta());
    }

    @Test
    void testInitThetaCase3() {
        calculateTrajectoryValuesFromTrajectory();
        double mom = testTrajectory.getMomentum().getNorm();
        double semiMaj = testTrajectory.getSemiMajor();
        double mass = testTrajectory.getMass();
        double eccen = testTrajectory.getEccentricity();
        double initAngVel = testTrajectory.getInitAngVel();

        double cosTheta = (initAngVel * mass * semiMaj / mom * (1 - Math.pow(eccen, 2)) - 1.0) / eccen;
        assertEquals(2 * Math.PI - Math.acos(cosTheta), testTrajectory.getInitTheta());
    }

    @Test
    void testInitThetaCase4() {
        radiusVector.setVector(new Vector(3.0, -2.0, -1.0));
        velocityVector.setVector(new Vector(-1.0 / 2, -1.0 / 3, 1.0 / 4));
        testTrajectory.setInitialConditions(radiusVector, velocityVector);
        calculateTrajectoryValuesFromTrajectory();
        double mom = testTrajectory.getMomentum().getNorm();
        double semiMaj = testTrajectory.getSemiMajor();
        double mass = testTrajectory.getMass();
        double eccen = testTrajectory.getEccentricity();
        double initAngVel = testTrajectory.getInitAngVel();

        double cosTheta = (initAngVel * mass * semiMaj / mom * (1 - Math.pow(eccen, 2)) - 1.0) / eccen;
        assertEquals(2 * Math.PI - Math.acos(cosTheta), testTrajectory.getInitTheta());
    }

    @Test
    void testCalcTrajectory() {
        testTrajectory.calculateTrajectory();
        ArrayList<Double> thetas = testTrajectory.getThetas();
        ArrayList<Double> coordsX = testTrajectory.getXCoords();
        ArrayList<Double> coordsY = testTrajectory.getYCoords();
        for (int i = 0; i < Trajectory.NUMBER_OF_POINTS; i++) {
            assertTrue(thetas.get(i) == 2.0 * Math.PI / Trajectory.NUMBER_OF_POINTS * i);
        }
        checkAllCoords(thetas, coordsX, coordsY);
    }

    @Test
    void testCalcTrajectoryTwice() {
        testTrajectory.calculateTrajectory();
        ArrayList<Double> thetas = testTrajectory.getThetas();
        ArrayList<Double> coordsX = testTrajectory.getXCoords();
        ArrayList<Double> coordsY = testTrajectory.getYCoords();
        assertEquals(Trajectory.NUMBER_OF_POINTS, coordsX.size());
        assertEquals(Trajectory.NUMBER_OF_POINTS, coordsY.size());
        checkAllCoords(thetas, coordsX, coordsY);

        testTrajectory.calculateTrajectory();
        coordsX = testTrajectory.getXCoords();
        coordsY = testTrajectory.getYCoords();
        assertEquals(Trajectory.NUMBER_OF_POINTS, coordsX.size());
        assertEquals(Trajectory.NUMBER_OF_POINTS, coordsY.size());
        coordsX = testTrajectory.getXCoords();
        coordsY = testTrajectory.getYCoords();
        checkAllCoords(thetas, coordsX, coordsY);
    }

    private void checkAllCoords(ArrayList<Double> thetas, ArrayList<Double> coordsX, ArrayList<Double> coordsY) {
        ArrayList<Double> expectedXCoords = new ArrayList<>();
        ArrayList<Double> expectedYCoords = new ArrayList<>();
        calculateAllInitialValuesByHand();

        Vector vectorZHat = expectedMomemtum.unit();
        Vector vectorXHat = radiusVector.unit();
        Vector vectorYHat = vectorZHat.crossProd(vectorXHat);
        Vector vectorXHatPrime = vectorXHat.scale(Math.cos(expectedInitTheta))
                .subtractVector(vectorYHat.scale(Math.sin(expectedInitTheta)));
        Vector vectorYHatPrime = vectorXHat.scale(Math.sin(expectedInitTheta))
                .addVector(vectorYHat.scale(Math.cos(expectedInitTheta)));

        for (int i = 0; i < thetas.size(); i++) {
            double theta = thetas.get(i);
            double valRAtAngle = expectedSemiMajor * (1 - Math.pow(expectedEccentricity, 2))
                    / (1 + expectedEccentricity * Math.cos(theta));
            Vector vectorRAtAngle = vectorXHatPrime.scale(Math.cos(theta))
                    .addVector(vectorYHatPrime.scale(Math.sin(theta))).scale(valRAtAngle);
            expectedXCoords.add(vectorRAtAngle.getX());
            expectedYCoords.add(vectorRAtAngle.getY());
        }

        for (int i = 0; i < coordsX.size(); i++) {
            assertEquals(expectedXCoords.get(i), coordsX.get(i));
            assertEquals(expectedYCoords.get(i), coordsY.get(i));
        }
    }

    private void calculateTrajectoryValuesFromTrajectory() {
        testTrajectory.calcMomentum();
        testTrajectory.calcSemiMajorAxis();
        testTrajectory.calcEnergy();
        testTrajectory.calcEccentricity();
        testTrajectory.calcInitRadVel();
        testTrajectory.calcInitAngVel();
        testTrajectory.calcInitialTheta();
    }

    private void calculateAllInitialValuesByHand() {
        radX = testTrajectory.getInitRadVector().getX();
        radY = testTrajectory.getInitRadVector().getY();
        radZ = testTrajectory.getInitRadVector().getZ();
        velX = testTrajectory.getInitVelVector().getX();
        velY = testTrajectory.getInitVelVector().getY();
        velZ = testTrajectory.getInitVelVector().getZ();
        double radius = Math.sqrt(Math.pow(radX, 2) + Math.pow(radY, 2) + Math.pow(radZ, 2));
        double velocity = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2) + Math.pow(velZ, 2));
        double expectedMomentumX = mass * (radY * velZ - radZ * velY);
        double expectedMomentumY = mass * -(radX * velZ - radZ * velX);
        double expectedMomentumZ = mass * (radX * velY - radY * velX);
        expectedMomemtum = new Vector(expectedMomentumX, expectedMomentumY, expectedMomentumZ);
        expectedSemiMajor = 1.0 / (2.0 / radius - Math.pow(velocity, 2) / Earth.GRAV_CONSTANT_TIMES_MASS);
        expectedEnergy = -Earth.GRAV_CONSTANT_TIMES_MASS * mass / (2.0 * expectedSemiMajor);
        double denom = Math.pow(Earth.GRAV_CONSTANT_TIMES_MASS, 2) * Math.pow(mass, 3);
        expectedEccentricity = Math
                .sqrt(1.0 + 2.0 * expectedEnergy * Math.pow(expectedMomemtum.getNorm(), 2) / denom);
        expectedInitRadVel = (velX * radX + velY * radY + velZ * radZ) / radiusVector.getNorm();
        expectedInitAngVel = Math.sqrt(Math.pow(velocity, 2) - Math.pow(expectedInitRadVel, 2));
        double cosTheta = (expectedInitAngVel * mass * expectedSemiMajor / expectedMomemtum.getNorm()
                * (1 - Math.pow(expectedEccentricity, 2)) - 1.0) / expectedEccentricity;
        expectedInitTheta = 2 * Math.PI - Math.acos(cosTheta);
    }
}
