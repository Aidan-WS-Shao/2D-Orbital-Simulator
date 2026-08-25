package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class TestVector {
    Vector testVector1;
    Vector testVector2;
    Vector testVector3;
    Vector testVector4;
    double root;

    @BeforeEach
    void runBefore() {
        testVector1 = new Vector(1, 2, 3);
        testVector2 = new Vector(4.3, 5.8, 6.1);
    }

    @Test
    void testConstructor() {
        root = Math.pow(1.0, 2) + Math.pow(2.0, 2) + Math.pow(3.0, 2);
        assertTrue(1.0 == testVector1.getX());
        assertTrue(2.0 == testVector1.getY());
        assertTrue(3.0 == testVector1.getZ());
        assertTrue(Math.sqrt(root) == testVector1.getNorm());

        root = Math.pow(4.3, 2) + Math.pow(5.8, 2) + Math.pow(6.1, 2);
        assertTrue(4.3 == testVector2.getX());
        assertTrue(5.8 == testVector2.getY());
        assertTrue(6.1 == testVector2.getZ());
        assertTrue(Math.sqrt(root) == testVector2.getNorm());
    }

    @Test
    void testSetVectorVector() {
        root = Math.pow(4.3, 2) + Math.pow(5.8, 2) + Math.pow(6.1, 2);
        testVector1.setVector(testVector2);
        assertTrue(4.3 == testVector1.getX());
        assertTrue(5.8 == testVector1.getY());
        assertTrue(6.1 == testVector1.getZ());
        assertTrue(Math.sqrt(root) == testVector1.getNorm());
        assertTrue(4.3 == testVector2.getX());
        assertTrue(5.8 == testVector2.getY());
        assertTrue(6.1 == testVector2.getZ());
        assertTrue(Math.sqrt(root) == testVector2.getNorm());
        assertFalse(testVector1.equals(testVector2));
    }

    @Test
    void testSetVectorDoubleDoubleDouble() {
        root = Math.pow(4.3, 2) + Math.pow(5.8, 2) + Math.pow(6.1, 2);
        testVector1.setVector(testVector2.getX(), testVector2.getY(), testVector2.getZ());
        assertTrue(4.3 == testVector1.getX());
        assertTrue(5.8 == testVector1.getY());
        assertTrue(6.1 == testVector1.getZ());
        assertTrue(Math.sqrt(root) == testVector1.getNorm());
        assertTrue(4.3 == testVector2.getX());
        assertTrue(5.8 == testVector2.getY());
        assertTrue(6.1 == testVector2.getZ());
        assertTrue(Math.sqrt(root) == testVector2.getNorm());
        assertFalse(testVector1.equals(testVector2));
    }

    @Test
    void testUnit() {
        root = Math.pow(1.0, 2) + Math.pow(2.0, 2) + Math.pow(3.0, 2);
        testVector3 = testVector1.unit();
        assertTrue(1.0 / Math.sqrt(root) == testVector3.getX());
        assertTrue(2.0 / Math.sqrt(root) == testVector3.getY());
        assertTrue(3.0 / Math.sqrt(root) == testVector3.getZ());
    }

    @Test
    void testScale() {
        testVector3 = testVector1.scale(2.5);
        assertTrue(1.0 == testVector1.getX());
        assertTrue(2.0 == testVector1.getY());
        assertTrue(3.0 == testVector1.getZ());
        assertTrue(2.5 == testVector3.getX());
        assertTrue(5.0 == testVector3.getY());
        assertTrue(7.5 == testVector3.getZ());

        root = Math.pow(2.5, 2) + Math.pow(5.0, 2) + Math.pow(7.5, 2);
        assertTrue(Math.sqrt(root) == testVector3.getNorm());
    }

    @Test
    void testScaleTwice() {
        testVector3 = testVector1.scale(3.0);
        assertTrue(3.0 == testVector3.getX());
        assertTrue(6.0 == testVector3.getY());
        assertTrue(9.0 == testVector3.getZ());
        root = Math.pow(3.0, 2) + Math.pow(6.0, 2) + Math.pow(9.0, 2);
        assertTrue(Math.sqrt(root) == testVector3.getNorm());

        testVector4 = testVector3.scale(1.5);
        assertTrue(4.5 == testVector4.getX());
        assertTrue(9.0 == testVector4.getY());
        assertTrue(13.5 == testVector4.getZ());
        root = Math.pow(4.5, 2) + Math.pow(9.0, 2) + Math.pow(13.5, 2);
        assertTrue(Math.sqrt(root) == testVector4.getNorm());
    }

    @Test
    void testAddVector() {
        testVector3 = testVector1.addVector(testVector2);
        assertTrue(5.3 == testVector3.getX());
        assertTrue(7.8 == testVector3.getY());
        assertTrue(9.1 == testVector3.getZ());
        root = Math.pow(5.3, 2) + Math.pow(7.8, 2) + Math.pow(9.1, 2);
        assertTrue(Math.sqrt(root) == testVector3.getNorm());

        assertTrue(1.0 == testVector1.getX());
        assertTrue(2.0 == testVector1.getY());
        assertTrue(3.0 == testVector1.getZ());
        root = Math.pow(1.0, 2) + Math.pow(2.0, 2) + Math.pow(3.0, 2);
        assertTrue(Math.sqrt(root) == testVector1.getNorm());
        assertTrue(4.3 == testVector2.getX());
        assertTrue(5.8 == testVector2.getY());
        assertTrue(6.1 == testVector2.getZ());
        root = Math.pow(4.3, 2) + Math.pow(5.8, 2) + Math.pow(6.1, 2);
        assertTrue(Math.sqrt(root) == testVector2.getNorm());
    }

    @Test
    void testAddToTwice() {
        testVector3 = testVector1.addVector(testVector2);
        assertTrue(5.3 == testVector3.getX());
        assertTrue(7.8 == testVector3.getY());
        assertTrue(9.1 == testVector3.getZ());
        root = Math.pow(5.3, 2) + Math.pow(7.8, 2) + Math.pow(9.1, 2);
        assertTrue(Math.sqrt(root) == testVector3.getNorm());
        assertTrue(1.0 == testVector1.getX());
        assertTrue(2.0 == testVector1.getY());
        assertTrue(3.0 == testVector1.getZ());
        root = Math.pow(1.0, 2) + Math.pow(2.0, 2) + Math.pow(3.0, 2);
        assertTrue(Math.sqrt(root) == testVector1.getNorm());
        assertTrue(4.3 == testVector2.getX());
        assertTrue(5.8 == testVector2.getY());
        assertTrue(6.1 == testVector2.getZ());
        root = Math.pow(4.3, 2) + Math.pow(5.8, 2) + Math.pow(6.1, 2);
        assertTrue(Math.sqrt(root) == testVector2.getNorm());
        testVector4 = testVector2.addVector(testVector1);
        assertTrue(5.3 == testVector4.getX());
        assertTrue(7.8 == testVector4.getY());
        assertTrue(9.1 == testVector4.getZ());
        root = Math.pow(5.3, 2) + Math.pow(7.8, 2) + Math.pow(9.1, 2);
        assertTrue(Math.sqrt(root) == testVector4.getNorm());
    }

    @Test
    void testSubtract() {
        testVector3 = testVector1.subtractVector(testVector2);
        assertTrue(1 - 4.3 == testVector3.getX());
        assertTrue(2 - 5.8 == testVector3.getY());
        assertTrue(3 - 6.1 == testVector3.getZ());
        root = Math.pow(1 - 4.3, 2) + Math.pow(2 - 5.8, 2) + Math.pow(3 - 6.1, 2);
        assertTrue(Math.sqrt(root) == testVector3.getNorm());
        assertTrue(1.0 == testVector1.getX());
        assertTrue(2.0 == testVector1.getY());
        assertTrue(3.0 == testVector1.getZ());
        root = Math.pow(1.0, 2) + Math.pow(2.0, 2) + Math.pow(3.0, 2);
        assertTrue(Math.sqrt(root) == testVector1.getNorm());
        assertTrue(4.3 == testVector2.getX());
        assertTrue(5.8 == testVector2.getY());
        assertTrue(6.1 == testVector2.getZ());
        root = Math.pow(4.3, 2) + Math.pow(5.8, 2) + Math.pow(6.1, 2);
        assertTrue(Math.sqrt(root) == testVector2.getNorm());
    }

    @Test
    void testSubtractTwice() {
        testVector3 = testVector1.subtractVector(testVector2);
        assertTrue(1 - 4.3 == testVector3.getX());
        assertTrue(2 - 5.8 == testVector3.getY());
        assertTrue(3 - 6.1 == testVector3.getZ());
        root = Math.pow(1 - 4.3, 2) + Math.pow(2 - 5.8, 2) + Math.pow(3 - 6.1, 2);
        assertTrue(Math.sqrt(root) == testVector3.getNorm());
        assertTrue(4.3 == testVector2.getX());
        assertTrue(5.8 == testVector2.getY());
        assertTrue(6.1 == testVector2.getZ());
        root = Math.pow(4.3, 2) + Math.pow(5.8, 2) + Math.pow(6.1, 2);
        assertTrue(Math.sqrt(root) == testVector2.getNorm());
        testVector4 = testVector2.subtractVector(testVector1);
        assertTrue(4.3 - 1 == testVector4.getX());
        assertTrue(5.8 - 2 == testVector4.getY());
        assertTrue(6.1 - 3 == testVector4.getZ());
    }

    @Test
    void testDotProd() {
        double testProd = testVector1.dotProd(testVector2);
        assertEquals(1 * 4.3 + 2 * 5.8 + 3 * 6.1, testProd);
    }

    @Test
    void testCrossProd() {
        Vector testVector3 = testVector1.crossProd(testVector2);
        assertTrue(2 * 6.1 - 3 * 5.8 == testVector3.getX());
        assertTrue(-(6.1 - 3 * 4.3) == testVector3.getY());
        assertTrue(5.8 - 2 * 4.3 == testVector3.getZ());
        root = Math.pow(2 * 6.1 - 3 * 5.8, 2) + Math.pow(-(6.1 - 3 * 4.3), 2) + Math.pow(5.8 - 2 * 4.3, 2);
        assertTrue(Math.sqrt(root) == testVector3.getNorm());
    }

    @Test
    void testperpVectorToTwoVectors() {
        Vector testVector3 = testVector1.perpVectorToTwoVectors(testVector2);
        double scalar = testVector1.dotProd(testVector2.unit());
        testVector4 = testVector2.unit().scale(scalar);
        assertEquals(testVector1.getNorm(),
                Math.sqrt(Math.pow(testVector3.getNorm(), 2) + Math.pow(testVector4.getNorm(), 2)),0.005);
        assertEquals(testVector1.getX() - testVector4.getX(), testVector3.getX());
        assertEquals(testVector1.getY() - testVector4.getY(), testVector3.getY());
        assertEquals(testVector1.getZ() - testVector4.getZ(), testVector3.getZ());
        assertEquals(0,testVector3.dotProd(testVector2));
    }
}
