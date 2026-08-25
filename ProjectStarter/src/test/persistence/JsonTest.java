package persistence;

import model.Rocket;
import model.Trajectory;
import model.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// Source: CPSC 210 Json sample
//         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
@ExcludeFromJacocoGeneratedReport
public class JsonTest {
    protected void checkRocket(String name, double mass, double angle, Vector thrust, Vector displacement,
            Vector velocity, Vector acceleration, Rocket rocket) {
        assertEquals(name, rocket.getName());
        assertEquals(mass, rocket.getMass());
        assertEquals(angle, rocket.getAngle());
        assertEquals(thrust.getX(),rocket.getThrust().getX());
        assertEquals(thrust.getY(),rocket.getThrust().getY());
        assertEquals(displacement.getX(),rocket.getDisplacement().getX());
        assertEquals(displacement.getY(),rocket.getDisplacement().getY());
        assertEquals(velocity.getX(),rocket.getVel().getX());
        assertEquals(velocity.getY(),rocket.getVel().getY());
        assertEquals(acceleration.getX(),rocket.getAccel().getX());
        assertEquals(acceleration.getY(),rocket.getAccel().getY());
        Trajectory expectedTrajectory = new Trajectory(mass);
        expectedTrajectory.setInitialConditions(displacement, velocity);
        assertTrajectory(expectedTrajectory, rocket.getTrajectory());
    }

    protected void assertTrajectory(Trajectory expectedTrajectory, Trajectory actualTrajectory) {
        List<Double> expectedX = expectedTrajectory.getXCoords();
        List<Double> expectedY = expectedTrajectory.getYCoords();
        List<Double> actualX = actualTrajectory.getXCoords();
        List<Double> actualY = actualTrajectory.getYCoords();
        assertEquals(expectedX.size(), actualX.size());
        assertEquals(expectedY.size(), actualY.size());
        for (int i = 0; i < expectedTrajectory.getXCoords().size(); i++) {
            assertEquals(expectedX.get(i), actualX.get(i));
            assertEquals(expectedY.get(i), actualY.get(i));
        }
    }
}
