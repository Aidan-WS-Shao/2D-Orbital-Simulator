package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.*;

// Represents the panel to display information to user and provide
// interaction to the user
//
// Structure from: CPSC210 SpaceInvaders, SmartHome
//                 (repo: https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase.git)
//                 (repo: https://github.students.cs.ubc.ca/CPSC210/LongFormProblemStarters.git)
@ExcludeFromJacocoGeneratedReport
public class DataDisplayPanel extends JPanel {
    private static final String ROCKETS_LIST_TXT = "List of rocket names: ";
    private static final String SELECTED_ROCKET_TXT = "Selected rocket name: ";
    private static final String ANGLE_TXT = "Angle: ";
    private static final String DISP_TXT = "Displacement: ";
    private static final String DISTANCE_TXT = "Distance from ground: ";
    private static final String VEL_TXT = "Velocity: ";
    private static final String SPEED_TXT = "Speed: ";
    private static final String THRUST_TXT = "Thrust: ";
    private static final int LBL_WIDTH = 450;
    private static final int LBL_HEIGHT = 45;

    private JLabel listLabel;
    private JLabel currRocketLabel;
    private JLabel angleLabel;
    private JLabel dispLabel;
    private JLabel distanceLabel;
    private JLabel velLabel;
    private JLabel speedLabel;
    private JLabel thrustLabel;
    private ArrayList<JLabel> labels;

    private JPanel displayPanel;

    private GridLayout displayLayout;

    // EFFECTS: creates a new user panel object that displays
    //          data, rocket information, and interaction options
    public DataDisplayPanel(Earth earth) {
        setBackground(new Color(180, 180, 180));
        setPreferredSize(new Dimension(LBL_WIDTH, SimulatorApp.DISPLAY_PANEL_HEIGHT));
        setUpJLabels();
        for (JLabel label : labels) {
            label.setPreferredSize(new Dimension(LBL_WIDTH, LBL_HEIGHT));
            displayPanel.add(label);
        }
        add(displayPanel);
        update(earth);
    }

    // MODIFIES: this
    // EFFECTS: Sets up the labels
    private void setUpJLabels() {
        displayLayout = new GridLayout(15, 100);
        displayPanel = new JPanel(displayLayout);
        labels = new ArrayList<>();
        listLabel = new JLabel(ROCKETS_LIST_TXT + "\n   ");
        labels.add(listLabel);
        currRocketLabel = new JLabel(SELECTED_ROCKET_TXT + "\n");
        labels.add(currRocketLabel);
        angleLabel = new JLabel(ANGLE_TXT);
        labels.add(angleLabel);
        dispLabel = new JLabel(DISP_TXT);
        labels.add(dispLabel);
        distanceLabel = new JLabel(DISTANCE_TXT);
        labels.add(distanceLabel);
        velLabel = new JLabel(VEL_TXT);
        labels.add(velLabel);
        speedLabel = new JLabel(SPEED_TXT);
        labels.add(speedLabel);
        thrustLabel = new JLabel(THRUST_TXT);
        labels.add(thrustLabel);
    }

    // MODIFIES: this
    // EFFECTS: updates the information for the user
    public void update(Earth earth) {
        Rocket currentRocket = earth.getCurrentRocket();
        listLabel.setText(ROCKETS_LIST_TXT + rocketNames(earth));
        currRocketLabel.setText(SELECTED_ROCKET_TXT + currentRocket.getName());
        angleLabel.setText(ANGLE_TXT + currentRocket.getAngle() + "degrees");
        dispLabel.setText(DISP_TXT + "x: " + round(currentRocket.getDisplacement().getX() / 1000.0) + "km  y: "
                + round(currentRocket.getDisplacement().getY() / 1000.0) + "km");
        distanceLabel.setText(
                DISTANCE_TXT + round((currentRocket.getDisplacement().getNorm() - Earth.RADIUS) / 1000.0) + "km");
        velLabel.setText(VEL_TXT + "Vx: " + round(currentRocket.getVel().getX()) + "m/s  Vy: "
                + round(currentRocket.getVel().getY()) + "m/s");
        speedLabel.setText(SPEED_TXT + round(currentRocket.getVel().getNorm()) + "m/s");
        thrustLabel.setText(THRUST_TXT + round(currentRocket.getThrust().getNorm()) + "N");
    }

    // EFFECTS: returns the rounded value of the inputted val to 3 decimal places
    private double round(double val) {
        return Math.round(val * 1000.0) / 1000.0;
    }

    // EFFECTS: puts the list of rockets to a list of the names and returns it
    private String rocketNames(Earth earth) {
        String names = "";
        for (Rocket rocket : earth.getRockets()) {
            names += rocket.getName() + ", ";
        }
        return names;
    }
}
