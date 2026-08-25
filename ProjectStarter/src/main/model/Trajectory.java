package model;

import java.util.ArrayList;

// Represents the trajectory to be drawn based on given position and speed
// Domain Analysis:
// Fields: mass, position, and speed of rocket
//         Intermediate values: momentum, semi-major axis, energy, eccentricity, 
//                              initial radial vel, initial angular vel, initial theta
//         Array of x coordinates, array of y coordinates, and array of theta from 0 to 360 deg
// Constants: mass of Earth and gravitational constant
// Methods:
//   - calculates the trajectory based on the current position and speed vectors
public class Trajectory {
    public static final int NUMBER_OF_POINTS = 1000;
    private double mass;
    private Vector radVector;
    private Vector velVector;
    private ArrayList<Double> thetas;
    private ArrayList<Double> trajectoryXCoords;
    private ArrayList<Double> trajectoryYCoords;
    private Vector momentum;
    private double semiMajorAxis;
    private double energy;
    private double eccentricity;
    private double initRadVel;
    private double initAngVel;
    private double initTheta;

    // EFFECTS: constructs projected trajectory with mass of rocket,
    //          displacement vector at (0, Earth.Radius), zero velocity
    //          vector, an arrayList of thetas to define the ellipse shape
    //          of the trajectory, and the empty trajectory coordinates lists
    public Trajectory(double mass) {
        this.mass = mass;
        radVector = new Vector(0, Earth.RADIUS, 0);
        velVector = new Vector(0, 0, 0);
        thetas = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_POINTS; i++) {
            thetas.add(2 * Math.PI / NUMBER_OF_POINTS * i);
        }
        trajectoryXCoords = new ArrayList<>();
        trajectoryYCoords = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: sets the position vector and speed vector to the current
    //          position of the rocket, and updates the trajectory based on
    //          the new conditions
    public void setInitialConditions(Vector r, Vector v) {
        radVector.setVector(r);
        velVector.setVector(v);
        calculateTrajectory();
    }

    // MODIFIES: this
    // EFFECTS: calculates the vector of x and y positions based on an array of
    //          values from 0 to 2pi, using the orbital equation and the characteristics
    //          of the rocket calculated in the methods below
    public void calculateTrajectory() {
        trajectoryXCoords = new ArrayList<>();
        trajectoryYCoords = new ArrayList<>();
        calcMomentum();
        calcSemiMajorAxis();
        calcEnergy();
        calcEccentricity();
        calcInitRadVel();
        calcInitAngVel();
        calcInitialTheta();

        Vector vectorZHat = momentum.unit();
        Vector vectorXHat = radVector.unit();
        Vector vectorYHat = vectorZHat.crossProd(vectorXHat);
        Vector vectorXHatPrime = vectorXHat.scale(Math.cos(initTheta))
                .subtractVector(vectorYHat.scale(Math.sin(initTheta)));
        Vector vectorYHatPrime = vectorXHat.scale(Math.sin(initTheta)).addVector(vectorYHat.scale(Math.cos(initTheta)));

        for (int i = 0; i < thetas.size(); i++) {
            double theta = thetas.get(i);
            double valRAtAngle = semiMajorAxis * (1 - Math.pow(eccentricity, 2)) / (1 + eccentricity * Math.cos(theta));
            Vector vectorRAtAngle = vectorXHatPrime.scale(Math.cos(theta))
                    .addVector(vectorYHatPrime.scale(Math.sin(theta))).scale(valRAtAngle);
            trajectoryXCoords.add(vectorRAtAngle.getX());
            trajectoryYCoords.add(vectorRAtAngle.getY());
        }
    }

    // MODIFIES: this
    // EFFECTS: calculates the momentum vector = mass * r vector x velocity vector
    //          where * is scalar multiplication, x is cross product
    public void calcMomentum() {
        momentum = radVector.crossProd(velVector).scale(mass);
    }

    // REQUIRES: length of r vector > 0
    // MODIFIES: this
    // EFFECTS: calculates semi-major axis a = 1 / (2/r - v^2/(G*M))
    public void calcSemiMajorAxis() {
        double radius = radVector.getNorm();
        double velocity = velVector.getNorm();
        semiMajorAxis = 1.0 / (2.0 / radius - Math.pow(velocity, 2) / Earth.GRAV_CONSTANT_TIMES_MASS);
    }

    // REQUIRES: semi-major axis > 0
    // MODIFIES: this
    // EFFECTS: calculates energy E = -G*M*m / (2*a) where a is the semi-major axis
    public void calcEnergy() {
        energy = -Earth.GRAV_CONSTANT_TIMES_MASS * mass / (2.0 * semiMajorAxis);
    }

    // REQUIRES: mass > 0
    // MODIFIES: this
    // EFFECTS: calculates eccentricity e = sqrt(1 + 2*E*|L|^2 / (G^2*M^2*m^3))
    public void calcEccentricity() {
        double denominator = Math.pow(Earth.GRAV_CONSTANT_TIMES_MASS, 2) * Math.pow(mass, 3);
        eccentricity = Math.sqrt(1 + 2 * energy * Math.pow(momentum.getNorm(), 2.0) / denominator);
    }

    // REQUIRES: r vector length > 0
    // MODIFIES: this
    // EFFECTS: calculates the radial velocity (scalar) v_r = v . r / |r|
    //          where . is the dot product
    public void calcInitRadVel() {
        initRadVel = velVector.dotProd(radVector.unit());
    }

    // MODIFIES: this
    // EFFECTS: calculates the angular velocity (scalar) v_theta = sqrt(|v|^2 - v_r^2)
    public void calcInitAngVel() {
        double initVel = velVector.getNorm();
        initAngVel = Math.sqrt(Math.pow(initVel, 2) - Math.pow(initRadVel, 2));
    }

    // REQUIRES: eccentricity < 1
    // MODIFIES: this
    // EFFECTS: calculates the initial theta of the rocket in the trajectory from the equations:
    //          v_r = |L| / (m*a) * e * sin(theta) / (1 - e^2)
    //          v_theta = |L| / (m*a) * (1 + e * cos(theta)) / (1 - e^2)
    //          by calculating sin(theta) and cos(theta), and checking which quadrant on the 
    //          graph theta lies, then calculates theta based on the quadrant.
    public void calcInitialTheta() {
        double sinTheta = initRadVel * mass * semiMajorAxis / momentum.getNorm() * (1 - Math.pow(eccentricity, 2))
                / eccentricity;
        double cosTheta = (initAngVel * mass * semiMajorAxis / momentum.getNorm() * (1 - Math.pow(eccentricity, 2))
                - 1.0) / eccentricity;

        if (sinTheta > 0) {
            initTheta = Math.acos(cosTheta);
        } else {
            initTheta = 2 * Math.PI - Math.acos(cosTheta);
        }
    }

    // EFFECTS: returns the mass of the rocket
    public double getMass() {
        return mass;
    }

    // EFFECTS: returns the inital radius vector
    public Vector getInitRadVector() {
        return radVector;
    }

    // EFFECTS: returns the inital velocity vector
    public Vector getInitVelVector() {
        return velVector;
    }

    // EFFECTS: returns the set of angles from 0 to 2pi of the trajectory
    public ArrayList<Double> getThetas() {
        return thetas;
    }

    // EFFECTS: returns the x coordinates of the trajectory
    public ArrayList<Double> getXCoords() {
        return trajectoryXCoords;
    }

    // EFFECTS: returns the y coordinates of the trajectory
    public ArrayList<Double> getYCoords() {
        return trajectoryYCoords;
    }

    // EFFECTS: returns the current momentum
    public Vector getMomentum() {
        return momentum;
    }

    // EFFECTS: returns the current semi-major axis
    public double getSemiMajor() {
        return semiMajorAxis;
    }

    // EFFECTS: returns the current energy
    public double getEnergy() {
        return energy;
    }

    // EFFECTS: returns the current eccentricity
    public double getEccentricity() {
        return eccentricity;
    }

    // EFFECTS: returns the current initial radial velocity
    public double getInitRadVel() {
        return initRadVel;
    }

    // EFFECTS: returns the current initial angular velocity
    public double getInitAngVel() {
        return initAngVel;
    }

    // EFFECTS: returns the current initial theta
    public double getInitTheta() {
        return initTheta;
    }

}
