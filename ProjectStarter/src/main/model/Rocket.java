package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a rocket with position, angle, speed, expected trajectory, thrust, mass, acceleration
// Domain analysis:
// Fields: position from (0,0), angle, speed, expected trajectory, thrust, mass, acceleration
// Constants: mass of Earth and gravitational constant. Note that angle is relative to the 90 
//            degree line of Earth
// Methods: 
//      - calculates the trajectory based on the current position and speed vectors
//      - getters and setters for position, speed, trajectory, thrust, mass, accel
//      - add speed based on acceleration and time
//      - add position based on speed and time
//      - turn rocket by some change in angle
public class Rocket implements Writable {
    public static final double MAX_THRUST = 10000.0;
    private String name;
    private double mass;
    private double angle;
    private Vector displacement;
    private Vector velocity;
    private Vector acceleration;
    private Vector thrust;
    private Trajectory trajectory;

    // EFFECTS: creates a new rocket object with initial position (0,Earth Radius),
    //          zero speed, zero thrust, zero acceleration, upright position,
    //          zero expected trajectory
    public Rocket(String name) {
        this.name = name;
        mass = 1000.0;
        angle = 0.0;
        displacement = new Vector(0.0,Earth.RADIUS,0.0);
        velocity = new Vector(0.0,0.0,0.0);
        acceleration = new Vector(0.0,0.0,0.0);
        thrust = new Vector(0.0,0.0,0.0);
        trajectory = new Trajectory(mass);
    }

    // MODIFIES: this
    // EFFECTS: turns the rocket by the given angle amount in degrees, resets the 
    //          angle to 0 when angle turns over 360 degrees. Rounds the angle to 
    //          2 decimals
    //          updates the thrust by changing its direction vector
    public void turnAngleBy(double angle) {
        this.angle = Math.round(((this.angle + angle) % 360.0) * 100.0) / 100.0;
        setThrust(thrust.getNorm());
    }

    // MODIFIES: this
    // EFFECTS: increments the rocket's acceleration, speed, and distance travelled
    //          with consideration of thrust and gravity. Applies acceleration to 
    //          velocity and displacement if distance from the center is greater than
    //          the Earth's radius, or if acceleration is outward from centre. 
    //          Otherwise, if acceleration has a component that is perpendicular to the 
    //          displacement to Earth's (0,0), apply the acceleration only in that 
    //          perpendicular direction
    //          Otherwise, acceleration and velocity is zero, and distance from 
    //          earth is unchanged
    public void increment() {
        double r = displacement.getNorm();
        double forceOfGrav = Earth.GRAV_CONSTANT_TIMES_MASS * mass / Math.pow(r,2);
        
        acceleration = thrust.subtractVector(displacement.unit().scale(forceOfGrav)).scale(1 / mass);
        if (displacement.getNorm() > Earth.RADIUS || displacement.unit().dotProd(acceleration.unit()) >= 0) {
            velocity = velocity.addVector(acceleration.scale(Earth.DT));
            displacement = displacement.addVector(velocity.scale(Earth.DT));
        } else if (acceleration.perpVectorToTwoVectors(displacement).getNorm() > 0) {
            acceleration = acceleration.perpVectorToTwoVectors(displacement);
            velocity = velocity.perpVectorToTwoVectors(displacement);
            velocity = velocity.addVector(acceleration.scale(Earth.DT));
            displacement = displacement.unit().scale(Earth.RADIUS);
            displacement = displacement.addVector(velocity.scale(Earth.DT));
        } else {
            displacement = displacement.unit().scale(Earth.RADIUS);
            velocity.setVector(0.0,0.0,0.0);
            acceleration.setVector(0.0,0.0,0.0);
        }
        trajectory.setInitialConditions(displacement,velocity);
    }
    
    // MODIFIES: this
    // EFFECTS: sets rocket's name to given name
    public void setName(String name) {
        this.name = name;
    }

    // MODIFIES: this
    // EFFECTS: sets rocket's mass to given mass
    public void setMass(double mass) {
        this.mass = mass;
    }

    // MODIFIES: this
    // EFFECTS: sets rocket's angle to given angle
    public void setAngle(double angle) {
        this.angle = angle;
    }

    // MODIFIES: this
    // EFFECTS: sets the rocket's magnitude of thrust to the given 
    //          magnitude of thrust, at the direction of its turn angle
    public void setThrust(double thrust) {
        Vector angleVector = new Vector(Math.sin(angle * Math.PI / 180.0), Math.cos(angle * Math.PI / 180.0),0.0);
        this.thrust.setVector(angleVector.scale(thrust));
    }

    // MODIFIES: this
    // EFFECTS: sets rocket's thrust to given thrust
    public void setThrust(Vector thrust) {
        this.thrust = thrust;
    }

    // MODIFIES: this
    // EFFECTS: sets rocket's acceleration to given acceleration
    public void setAccel(Vector accel) {
        this.acceleration = accel;
    }

    // MODIFIES: this
    // EFFECTS: sets rocket's velocity to given velocity
    public void setVel(Vector vel) {
        this.velocity = vel;   
    }

    // MODIFIES: this
    // EFFECTS: sets rocket's displacement to given displacement
    public void setDisp(Vector disp) {
        this.displacement = disp;
    }


    // EFFECTS: returns the name of the rocket
    public String getName() {
        return name;
    }

    // EFFECTS: returns the mass of the rocket
    public double getMass() {
        return mass;
    }

    // EFFECTS: returns the angle of the rocket
    public double getAngle() {
        return angle;
    }

    // EFFECTS: returns the thrust of the rocket
    public Vector getThrust() {
        return thrust;
    }

    // EFFECTS: returns the acceleration vector of the rocket
    public Vector getAccel() {
        return acceleration;
    }

    // EFFECTS: returns the velocity vector of the rocket
    public Vector getVel() {
        return velocity;
    }

    // EFFECTS: returns the displacement vector of the rocket
    public Vector getDisplacement() {
        return displacement;
    }

    // EFFECTS: returns the trajectory of the rocket
    public Trajectory getTrajectory() {
        return trajectory;
    }

    // Source: CPSC 210 Json Sample
    //         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("mass", mass);
        json.put("angle", angle);
        json.put("thrust X", thrust.getX());
        json.put("thrust Y", thrust.getY());
        json.put("displacement X", displacement.getX());
        json.put("displacement Y", displacement.getY());
        json.put("velocity X", velocity.getX());
        json.put("velocity Y", velocity.getY());
        json.put("acceleration X", acceleration.getX());
        json.put("acceleration Y", acceleration.getY());

        return json;
    }

}
