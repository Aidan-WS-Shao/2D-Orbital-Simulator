package model;

// Represents a 3D vector with entries x, y, z which represents the unit
// in 3 dimensions
// Domain analysis:
// Fields: x, y, z
// Methods: 
//      - calculates the length of the vector (2-norm)
//      - calculates the cross product of two vectors
//      - getters and setters of x, y, z
public class Vector {
    private double componentX;
    private double componentY;
    private double componentZ;
    private double length;

    // EFFECTS: creates a new vector with components x, y, z
    public Vector(double x, double y, double z) {
        this.componentX = x;
        this.componentY = y;
        this.componentZ = z;
        this.updateNorm();
    }

    // MODIFIES: this
    // EFFECTS: sets components of this vector to the components of the
    // given vector, but does not make this vector *equal* to the
    // update vector
    public void setVector(Vector update) {
        this.componentX = update.getX();
        this.componentY = update.getY();
        this.componentZ = update.getZ();
        this.updateNorm();
    }

    // MODIFIES: this
    // EFFECTS: sets components of this vector to the given x y z components
    public void setVector(double x, double y, double z) {
        this.componentX = x;
        this.componentY = y;
        this.componentZ = z;
        this.updateNorm();
    }

    // MODIFIES: this
    // EFFECTS: Calculates and updates this vector's length = sqrt(x^2 + y^2 + z^2)
    private void updateNorm() {
        length = Math.sqrt(Math.pow(componentX, 2) + Math.pow(componentY, 2) + Math.pow(componentZ, 2));
    }

    // REQUIRES: Vector length > 0
    // EFFECTS: Calculates and returns the vector's unit vector = vector / |vector|
    //          by dividing each component by the length of the vector
    public Vector unit() {
        Vector unitVector;
        double unitX = this.componentX / this.length;
        double unitY = this.componentY / this.length;
        double unitZ = this.componentZ / this.length;
        unitVector = new Vector(unitX, unitY, unitZ);

        return unitVector;
    }

    // EFFECTS: multiplies this vector with a given scalar by multiplying each component
    //          with the scalar an returns the new Vector
    public Vector scale(double scalar) {
        Vector scaledVector;
        double scaledX = this.componentX * scalar;
        double scaledY = this.componentY * scalar;
        double scaledZ = this.componentZ * scalar;
        scaledVector = new Vector(scaledX, scaledY, scaledZ);
        return scaledVector;
    }

    // EFFECTS: returns the element-wise sum of this and other vector by setting the 
    //          component of the new vector as the sum of the components in the 
    //          corresponding dimension, of the two vectors
    public Vector addVector(Vector other) {
        Vector sumVector;
        double sumX = this.componentX + other.getX();
        double sumY = this.componentY + other.getY();
        double sumZ = this.componentZ + other.getZ();
        sumVector = new Vector(sumX, sumY, sumZ);

        return sumVector;
    }

    // EFFECTS: returns the element-wise difference of this and other vector by 
    //          setting the component of the new vector as the difference of the 
    //          components in the corresponding dimension, of the two vectors
    //          Difference is calculated as diff = (this - other)
    public Vector subtractVector(Vector other) {
        Vector diffVector;
        double diffX = this.componentX - other.getX();
        double diffY = this.componentY - other.getY();
        double diffZ = this.componentZ - other.getZ();
        diffVector = new Vector(diffX, diffY, diffZ);

        return diffVector;
    }

    // EFFECTS: returns the dot product of this and other vector. Calculated 
    //          as the sum of the products of the components in the same dimension.
    //          dot product = this.x*other.y + this.y*other.y + this.z*other.z
    public double dotProd(Vector other) {
        double product = 0;
        product += this.componentX * other.getX();
        product += this.componentY * other.getY();
        product += this.componentZ * other.getZ();

        return product;
    }

    // EFFECTS: returns cross product of this x other vector
    //          cross product is a vector with:
    //              - x component = this.y*other.z - this.z*other.y
    //              - y component = -this.x*other.z + this.z*other.x
    //              - z component = this.x*other.y - this.y*other.x
    public Vector crossProd(Vector other) {
        Vector product;
        double prodX = this.componentY * other.getZ() - this.componentZ * other.getY();
        double prodY = -(this.componentX * other.getZ() - this.componentZ * other.getX());
        double prodZ = this.componentX * other.getY() - this.componentY * other.getX();
        product = new Vector(prodX, prodY, prodZ);

        return product;
    }

    // EFFECTS: returns the component of this vector that is perpendicular 
    //          to the other vector
    public Vector perpVectorToTwoVectors(Vector other) {
        double scalarOfParallel = this.dotProd(other.unit());
        Vector parallelVector = other.unit().scale(scalarOfParallel);
        Vector perpVector = this.subtractVector(parallelVector);
        return perpVector;
    }

    // EFFECTS: returns x component of vector
    public double getX() {
        return this.componentX;
    }

    // EFFECTS: returns y component of vector
    public double getY() {
        return this.componentY;
    }

    // EFFECTS: returns z component of vector
    public double getZ() {
        return this.componentZ;
    }

    // EFFECTS: returns length (2-norm) of vector
    public double getNorm() {
        return this.length;
    }

}
