package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.*;

// Represents the panel to visualize the simulation including rocket placements and
// trajectories
//
// Structure from: CPSC210 SpaceInvaders, SmartHome
//                 (repo: https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase.git)
//                 (repo: https://github.students.cs.ubc.ca/CPSC210/LongFormProblemStarters.git)
@ExcludeFromJacocoGeneratedReport
public class SimulationPanel extends JPanel {
    private static final double DEFAULT_SCALE_FACTOR = 2e-5;
    private static final int PANEL_WIDTH = 1000;
    private static final int CENTER_X = PANEL_WIDTH / 2;
    private static final int CENTER_Y = SimulatorApp.PANEL_HEIGHT / 2;
    private static final int ROCKET_WIDTH = 10;
    private static final int ROCKET_HEIGHT = 16;

    private Earth earth;
    private Rocket currentRocket;
    private int currentRocketX;
    private int currentRocketY;
    private double scaleFactor;

    // EFFECTS: creates a new simulation panel with given earth
    public SimulationPanel(Earth earth) {
        setPreferredSize(new Dimension(PANEL_WIDTH, SimulatorApp.PANEL_HEIGHT));
        setBackground(Color.BLACK);
        this.earth = earth;
        this.currentRocket = earth.getCurrentRocket();
        this.currentRocketX = scale(currentRocket.getDisplacement().getX());
        this.currentRocketY = scale(currentRocket.getDisplacement().getY());
        scaleFactor = DEFAULT_SCALE_FACTOR;
    }

    @Override
    protected void paintComponent(Graphics g) {
        updateCurrentRocket();
        super.paintComponent(g);

        drawEarth(g);
        drawRockets(g);
        drawTrajectory(g);
    }

    // MODIFIES: this
    // EFFECTS: sets the current rocket and the displacement components to the 
    //          earth's current rocket
    private void updateCurrentRocket() {
        this.currentRocket = earth.getCurrentRocket();
        this.currentRocketX = scale(currentRocket.getDisplacement().getX());
        this.currentRocketY = scale(currentRocket.getDisplacement().getY());
    }

    // MODIFIES: this
    // EFFECTS: sets the earth to the inputted earth and
    //          sets the scale factor to DEFAULT_SCALE_FACTOR x given int
    public void updateEarthAndScaleFactor(Earth earth, double timesNumber) {
        this.earth = earth;
        scaleFactor = timesNumber * DEFAULT_SCALE_FACTOR;
    }

    // MODIFIES: Graphics
    // EFFECTS: draws the earth relative to the current rocket, scaled down to the
    //          dedicated scale value
    private void drawEarth(Graphics g) {
        Color savedCol = g.getColor();
        g.setColor(Earth.COLOUR);
        g.fillOval(CENTER_X - scale(Earth.RADIUS) - currentRocketX, CENTER_Y - scale(Earth.RADIUS) + currentRocketY,
                scale(Earth.RADIUS * 2), scale(Earth.RADIUS * 2));
        g.setColor(savedCol);
    }

    // MODIFIES: Graphics
    // EFFECTS: draws all of the rockets depending on their distance from earth
    //          centre and orientation, relative to the current rocket
    //          Distances are scaled down
    private void drawRockets(Graphics g) {
        for (Rocket rocket : earth.getRockets()) {
            drawRocket(g, rocket);
        }
    }

    // MODIFIES: Graphics
    // EFFECTS: draws one rocket depending on their distance from earth
    //          centre and orientation, relative to the current rocket
    //          Draws the current rocket at the centre of the panel
    //          Distances are scaled down
    private void drawRocket(Graphics g, Rocket rocket) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();
        Vector rocketCoords = rocket.getDisplacement();
        int positionX = CENTER_X + scale(rocketCoords.getX()) - currentRocketX - ROCKET_WIDTH / 2;
        int positionY = CENTER_Y - scale(rocketCoords.getY()) + currentRocketY - ROCKET_HEIGHT / 2;
        g2d.rotate(0, positionX + ROCKET_WIDTH / 2, positionY + ROCKET_HEIGHT / 2);
        g2d.rotate(Math.toRadians(rocket.getAngle()), positionX + ROCKET_WIDTH / 2, positionY + ROCKET_HEIGHT / 2);
        
        Color savedCol = g.getColor();
        Polygon triangle = makeTriangle(positionX, positionY);
        
        g2d.setColor(Color.WHITE);
        g2d.fillPolygon(triangle);
        g2d.setColor(savedCol);
        g2d.setTransform(oldTransform);
    }

    // EFFECTS: creates a polygon with ROCKET_WIDTH and ROCKET_HEIGHT, placed
    //          at the given positions of the top left corner of the triangle
    //          as if the triangle is upright
    private Polygon makeTriangle(int positionX, int positionY) {
        Polygon triangle = new Polygon();
        triangle.addPoint(ROCKET_WIDTH / 2 + positionX, positionY);
        triangle.addPoint(positionX, ROCKET_HEIGHT + positionY);
        triangle.addPoint(ROCKET_WIDTH + positionX, ROCKET_HEIGHT + positionY);
        return triangle;
    }

    // REQUIRES: current rocket's trajectory to have the same sized x and y coordinates
    //           ArrayList
    // MODIFIES: Graphics
    // EFFECTS: draws the trajectory path of the rocket, relative to the current rocket
    //          Draws the trajectory path by drawing finite lines between two concurrent points
    //          in the trajectory
    //          Distances are scaled down
    private void drawTrajectory(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Trajectory currentTrajectory = currentRocket.getTrajectory();
        ArrayList<Double> coordsX = currentTrajectory.getXCoords();
        ArrayList<Double> coordsY = currentTrajectory.getYCoords();
        for (int i = 0; i < coordsX.size(); i += 2) {
            int x1 = CENTER_X + scale(coordsX.get(i)) - currentRocketX;
            int x2 = CENTER_X + scale(coordsX.get(i + 1)) - currentRocketX;
            int y1 = CENTER_Y - scale(coordsY.get(i)) + currentRocketY;
            int y2 = CENTER_Y - scale(coordsY.get(i + 1)) + currentRocketY;
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    // EFFECTS: returns the scaled value of the inputted value, rounded
    //          using the dedicated scale factor
    private int scale(double val) {
        double scaled = val * scaleFactor;
        return (int) Math.round(scaled);
    }
}
