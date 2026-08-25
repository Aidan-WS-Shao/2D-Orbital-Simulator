package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.*;
import model.exceptions.NameTakenException;
import model.exceptions.RocketOnLaunchPadException;
import persistence.JsonReader;
import persistence.JsonWriter;

// Represents the panel to display information to user and provide
// interaction to the user
//
// Structure from: CPSC210 SpaceInvaders, SmartHome
//                 (repo: https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase.git)
//                 (repo: https://github.students.cs.ubc.ca/CPSC210/LongFormProblemStarters.git)
@ExcludeFromJacocoGeneratedReport
public class UserInteractionPanel extends JPanel {
    private static final String DEFAULT_JSON_STORE = "./data/earth.json";
    private static final int LBL_WIDTH = 450;
    private static final int LBL_HEIGHT = 24;
    private static final int CENTER = SwingConstants.CENTER;
    private static final double D_ANGLE = 2.0;
    private static final int NUMBER_OF_TICKS = 5;
    private Earth earth;
    private Rocket currentRocket;
    private double setThrust;
    private boolean thrustOn;

    private JLabel enterNameLabel;
    private JTextField rocketNameField;
    private JLabel rocketFieldErrorWarning;

    private JButton turnThrustButton;
    private JSlider thrustSlider;

    private JLabel scaleText;
    private double zoomScale;
    private JLabel simSpeedText;
    private int simSpeedScale;
    private ArrayList<Integer> simSpeeds;

    private JPanel displayPanel;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JTextField fileNameField;
    private JLabel fileErrorLabel;

    private GridLayout displayLayout;

    // EFFECTS: creates a new user panel object that displays
    //          data, rocket information, and interaction options
    public UserInteractionPanel(Earth earth) {
        this.earth = earth;
        this.currentRocket = earth.getCurrentRocket();
        jsonWriter = new JsonWriter(DEFAULT_JSON_STORE);
        jsonReader = new JsonReader(DEFAULT_JSON_STORE);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(new Color(180, 180, 180));
        setPreferredSize(new Dimension(LBL_WIDTH, SimulatorApp.INTERACTION_PANEL_HEIGHT));
        setUpJLabels();
        thrustOn = false;
        zoomScale = 1;
        setSpeeds();
        simSpeedScale = simSpeeds.get(0);
        placeAllActions();
        add(displayPanel);
    }

    // MODIFIES: this
    // EFFECTS: Sets up the labels
    private void setUpJLabels() {
        displayLayout = new GridLayout(15, 100);
        displayPanel = new JPanel(displayLayout);
        rocketFieldErrorWarning = new JLabel("");
        fileErrorLabel = new JLabel("");
        scaleText = new JLabel("1x");
        simSpeedText = new JLabel("1x");
    }

    // MODIFIES: this
    // EFFECTS: sets up the array of speeds for time speed up
    private void setSpeeds() {
        simSpeeds = new ArrayList<>();
        simSpeeds.add(1);
        simSpeeds.add(2);
        simSpeeds.add(5);
        simSpeeds.add(10);
        simSpeeds.add(25);
        simSpeeds.add(50);
        simSpeeds.add(100);
        simSpeeds.add(250);
        simSpeeds.add(500);
        simSpeeds.add(1000);
    }

    // MODIFIES: this
    // EFFECTS: places all of the user interaction items on main panel
    private void placeAllActions() {
        addSelectRemoveRocketButtons();
        enterRocketName();
        turnAngleButtons();
        turnOnThrustButton();
        addZoomAndSpeedButtons();
        writeLoadFileButtons();
        enterFileName();
    }

    // MODIFIES: this
    // EFFECTS: creates an add Rocket button, select rocket button, 
    //          and remove rocket button, assigns the action listener to them
    private void addSelectRemoveRocketButtons() {
        JButton addRocketButton = new JButton("Add New Rocket");
        JButton selectRocketButton = new JButton("Select Rocket");
        JButton removeRocketButton = new JButton("Remove Rocket");
        JPanel buttonRow = formatButtonRow(addRocketButton);
        buttonRow.add(selectRocketButton);
        buttonRow.add(removeRocketButton);
        buttonRow.setSize(WIDTH, HEIGHT / 6);

        addRocketButton.addActionListener(new UserPanelActionListener());
        selectRocketButton.addActionListener(new UserPanelActionListener());
        removeRocketButton.addActionListener(new UserPanelActionListener());

        displayPanel.add(buttonRow);
    }

    // MODIFIES: this
    // EFFECTS: creates a text field for user to enter rocket name to be selected, 
    //          added or deleted
    public void enterRocketName() {
        JPanel row = new JPanel();
        enterNameLabel = new JLabel("Enter Rocket Name:");
        rocketNameField = new JTextField(10);
        rocketNameField.setSize(new Dimension(LBL_WIDTH, LBL_HEIGHT * 2));
        row.add(enterNameLabel);
        row.add(rocketNameField);
        row.add(rocketFieldErrorWarning);
        displayPanel.add(row);
    }

    // MODIFIES: this
    // EFFECTS: creates two buttons to turn the rocket, assigns the action 
    //          listener to them
    public void turnAngleButtons() {
        JLabel angleLabel = new JLabel("Turn Angle", CENTER);
        JButton turnLeftButton = new JButton("<");
        JButton turnRightButton = new JButton(">");
        JPanel buttonRow = formatButtonRow(turnLeftButton);
        JPanel row = new JPanel();
        row.add(angleLabel);
        row.add(buttonRow);
        buttonRow.add(turnRightButton);
        buttonRow.setSize(WIDTH / 6, HEIGHT / 6);

        turnLeftButton.addActionListener(new UserPanelActionListener());
        turnRightButton.addActionListener(new UserPanelActionListener());

        displayPanel.add(row);
    }

    // MODIFIES: this
    // EFFECTS: adds button to toggle the thrust. Sets toggle thrust button
    //          to on if the current rocket has a non zero thrust.
    //          adds slider to set thrust level. Sets thrust slider to
    //          current rocket's thrust. Assigns the action listener to them
    public void turnOnThrustButton() {
        turnThrustButton = new JButton("Thrust Off");
        if (currentRocket.getThrust().getNorm() > 0) {
            turnThrustButton.setText("Thrust On");
            thrustOn = true;
        }
        JPanel buttonRow = formatButtonRow(turnThrustButton);
        JPanel row = new JPanel();
        buttonRow.setSize(WIDTH, HEIGHT / 6);

        turnThrustButton.addActionListener(new UserPanelActionListener());

        double currentThrust = currentRocket.getThrust().getNorm() / Rocket.MAX_THRUST * 100.0;
        thrustSlider = new JSlider(0, 100, (int) currentThrust);
        thrustSlider.setMajorTickSpacing(Math.round(100 / NUMBER_OF_TICKS));
        thrustSlider.setPaintTicks(true);
        thrustSlider.setPaintLabels(true);
        thrustSlider.addChangeListener(new SliderListener());

        row.add(thrustSlider);
        row.add(buttonRow);
        displayPanel.add(row);
    }

    // MODIFIES: this
    // EFFECTS: adds the zoom toggle and speed toggle buttons
    private void addZoomAndSpeedButtons() {
        JPanel row = new JPanel();
        zoomToggleButtons(row);
        row.add(Box.createHorizontalStrut(15));
        timeSpeedToggleButtons(row);
        displayPanel.add(row);
    }

    // EFFECTS: adds buttons to scale up or down the zoom, adds to the
    //          inputted JPanel row Assigns the action listener to them
    private void zoomToggleButtons(JPanel row) {
        JLabel scaleLabel = new JLabel("Zoom");
        JButton scaleUp = new JButton("+");
        JButton scaleDown = new JButton("-");
        JPanel buttonRow = formatButtonRow(scaleDown);
        buttonRow.add(scaleUp);
        row.add(scaleLabel);
        row.add(buttonRow);
        row.add(scaleText);
        buttonRow.setSize(WIDTH / 6, HEIGHT / 6);

        scaleUp.addActionListener(new UserPanelActionListener());
        scaleDown.addActionListener(new UserPanelActionListener());
    }

    // EFFECTS: adds two buttons to speed up and slow down time
    //          by changing the number of times to increment per
    //          simulation time loop, adds to the given JPanel row
    //          Assigns the action listener to them
    private void timeSpeedToggleButtons(JPanel row) {
        JLabel windLabel = new JLabel("Speed");
        JButton slowDown = new JButton("<<");
        JButton speedUp = new JButton(">>");
        JPanel buttonRow = formatButtonRow(slowDown);
        buttonRow.add(speedUp);
        row.add(windLabel);
        row.add(buttonRow);
        row.add(simSpeedText);
        buttonRow.setSize(WIDTH / 6, HEIGHT / 6);

        slowDown.addActionListener(new UserPanelActionListener());
        speedUp.addActionListener(new UserPanelActionListener());
    }

    // MODIFIES: this
    // EFFECTS: adds the write file and load file buttons
    //          assigns the action listener to them
    private void writeLoadFileButtons() {
        JButton writeFileButton = new JButton("Write File");
        JButton loadFileButton = new JButton("Load File");
        JPanel buttonRow = formatButtonRow(writeFileButton);
        JPanel row = new JPanel();
        buttonRow.add(loadFileButton);

        writeFileButton.addActionListener(new UserPanelActionListener());
        loadFileButton.addActionListener(new UserPanelActionListener());

        buttonRow.setSize(WIDTH / 6, HEIGHT / 6);
        row.add(buttonRow);
        displayPanel.add(row);    
    }

    // MODIFIES: this
    // EFFECTS: creates a text field for user to enter file name to be loaded 
    //          and saved to
    public void enterFileName() {
        JPanel row = new JPanel();
        JLabel fileNameLabel = new JLabel("Enter file name: ");
        fileNameField = new JTextField(10);
        fileNameField.setSize(new Dimension(LBL_WIDTH, LBL_HEIGHT * 2));
        row.add(fileNameLabel);
        row.add(fileNameField);
        row.add(fileErrorLabel);
        displayPanel.add(row);
    }

    @ExcludeFromJacocoGeneratedReport
    private class UserPanelActionListener implements ActionListener {
        // MODIFIES: this
        // EFFECTS: if "Add New Rocket" button clicked, calls the add rocket action
        //          if "Select Rocket" button clicked, calls the select rocket action
        //          if "Remove Rocket" button clicked, calls the remove rocket action
        //          if "<" button clicked, turns the rocket's angle by -D_ANGLE
        //          if ">" button clicked, turns the rocket's angle by D_ANGLE
        //          if "Thrust Off" or "Thrust On" buttons clicked, calls the toggle thrust action
        //          if "+" or "-" buttons clicked, calls the adjust zoom method
        //          if "<<" or ">>" buttons clicked, calls the adjust speed method
        //          if "Write File" button clicked, calls the save Earth file method
        //          if "Load File" button clicked, calls the load Earth file method
        @ExcludeFromJacocoGeneratedReport
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Add New Rocket")) {
                addRocketAction();
            } else if (command.equals("Select Rocket")) {
                selectRocketAction();
            } else if (command.equals("Remove Rocket")) {
                removeRocketAction();
            } else if (command.equals("<")) {
                earth.getCurrentRocket().turnAngleBy(-D_ANGLE);
            } else if (command.equals(">")) {
                earth.getCurrentRocket().turnAngleBy(D_ANGLE);
            } else if (command.equals("Thrust Off") || command.equals("Thrust On")) {
                toggleThrustAction(command);
            } else if (command.equals("+") || command.equals("-")) {
                adjustZoom(command);
            } else if (command.equals("<<") || command.equals(">>")) {
                adjustSpeed(command);
            } else if (command.equals("Write File")) {
                saveEarthFile();
            } else if (command.equals("Load File")) {
                loadEarthFile();
            }
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private class SliderListener implements ChangeListener {
        // MODIFIES: this
        // EFFECTS: sets the thrust value to set for the rocket sets the current 
        //          rocket's thrust to the value if thrust is on already
        @Override
        @ExcludeFromJacocoGeneratedReport
        public void stateChanged(ChangeEvent e) {
            JSlider slider = (JSlider) e.getSource();
            if (!slider.getValueIsAdjusting()) {
                setThrust = Math.round(slider.getValue() / 100.0 * Rocket.MAX_THRUST);
                if (thrustOn) {
                    earth.getCurrentRocket().setThrust(setThrust);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: tells user to enter a name if the name field is empty
    //          tells user there is a rocket on the launch pad if adding rocket fails
    //          otherwise adds rocket with name inputted in the name field to earth,
    //          and resets the name field and warning text to blank
    private void addRocketAction() {
        if (rocketNameField.getText().isBlank()) {
            rocketFieldErrorWarning.setText("Enter a name!");
        } else {
            try {
                earth.addRocket(rocketNameField.getText());
                rocketFieldErrorWarning.setText("");
                turnThrustButton.setText("Thrust Off");
                thrustSlider.setValue(0);
                thrustOn = false;
            } catch (NameTakenException nte) {
                rocketFieldErrorWarning.setText("Name is already taken!");
            } catch (RocketOnLaunchPadException rolpe) {
                rocketFieldErrorWarning.setText("There is currently a rocket on launch pad!");
            }
            rocketNameField.setText("");
        }
    }

    // MODIFIES: this
    // EFFECTS: tells user to enter a name if the name field is empty
    //          otherwise selects rocket with name inputted in the name field to current
    //          rocket of earth, and resets the name field and warning text to blank,
    //          then sets the thrust toggle button and thrust slider to match the selected
    //          rocket's thrust
    private void selectRocketAction() {
        if (rocketNameField.getText().isBlank()) {
            rocketFieldErrorWarning.setText("Enter a name!");
        } else {
            if (!(earth.setCurrentRocket(rocketNameField.getText()))) {
                rocketFieldErrorWarning.setText("No rocket with that name!");
            } else {
                rocketFieldErrorWarning.setText("");
                if (earth.getCurrentRocket().getThrust().getNorm() > 0) {
                    double thrust = earth.getCurrentRocket().getThrust().getNorm() / Rocket.MAX_THRUST * 100;
                    turnThrustButton.setText("Thrust On");
                    thrustSlider.setValue((int) thrust);
                    thrustOn = true;
                } else {
                    turnThrustButton.setText("Thrust Off");
                    thrustOn = false;
                }
            }
            rocketNameField.setText("");
        }
    }

    // MODIFIES: this
    // EFFECTS: tells user to enter a name if the name field is empty
    //          otherwise removes rocket with name inputted in the name field to remove
    //          from earth, and resets the name field and warning text to blank,
    //          then sets the thrust toggle button and thrust slider to match the selected
    //          rocket's thrust
    private void removeRocketAction() {
        if (rocketNameField.getText().isBlank()) {
            rocketFieldErrorWarning.setText("Enter a name!");
        } else {
            if (earth.getRockets().size() == 1) {
                rocketFieldErrorWarning.setText("There is only one rocket left!");
            } else if (!(earth.removeRocket(rocketNameField.getText()))) {
                rocketFieldErrorWarning.setText("No rocket with that name!");
            } else {
                rocketFieldErrorWarning.setText("");
                if (earth.getCurrentRocket().getThrust().getNorm() > 0) {
                    double thrust = earth.getCurrentRocket().getThrust().getNorm() / Rocket.MAX_THRUST * 100;
                    turnThrustButton.setText("Thrust On");
                    thrustSlider.setValue((int) thrust);
                    thrustOn = true;
                } else {
                    turnThrustButton.setText("Thrust Off");
                    thrustOn = false;
                }
            }
            rocketNameField.setText("");
        }
    }

    // REQUIRES: inputted String command is one of "Thrust Off" or "Thrust On"
    // MODIFIES: this
    // EFFECTS: toggles the thrust on or off depending on the button
    //          if command is "Thrust Off", sets the current rocket's thrust to
    //          the dedicated set thrust value, sets the button name to "Thrust On",
    //          and sets the dedicated thrust on value to true
    //          if command is "Thrust On", sets the current rocket's thrust to 
    //          zero, sets the button name to "Thrust Off", and sets the dedicated
    //          thrust on value to false
    private void toggleThrustAction(String command) {
        if (command.equals("Thrust Off")) {
            earth.getCurrentRocket().setThrust(setThrust);
            turnThrustButton.setText("Thrust On");
            thrustOn = true;
        } else if (command.equals("Thrust On")) {
            earth.getCurrentRocket().setThrust(0);
            turnThrustButton.setText("Thrust Off");
            thrustOn = false;
        }
    }

    // REQUIRES: inputted String is either "+" or "-"
    // MODIFIES: this
    // EFFECTS: adds or decreases zoom scale by +1x depending on inputted String
    //          if zoom scale is at least 10, increments up by +10x. If zoom
    //          scale is at least 20, decrements down by -10x.
    //          if zoom scale is at least 100, increments up by +100x. If zoom 
    //          scale is at least 200, decrements down by -100x.
    //          if zoom scale is between 0.125 and 1, increments up by twice as much,
    //          decremends down by half as much
    //          changes scaleText to display zoom value to the current value
    private void adjustZoom(String plusOrMinus) {
        if (plusOrMinus.equals("+")) {
            if (zoomScale >= 100) {
                zoomScale += 100;
            } else if (zoomScale >= 10) {
                zoomScale += 10;
            } else if (zoomScale >= 1) {
                zoomScale++;
            } else if (zoomScale > 0) {
                zoomScale *= 2.0;
            }
        } else if (plusOrMinus.equals("-")) {
            if (zoomScale >= 200) {
                zoomScale -= 100;
            } else if (zoomScale >= 20) {
                zoomScale -= 10;
            } else if (zoomScale > 1) {
                zoomScale--;
            } else if (zoomScale > 0.125) {
                zoomScale = zoomScale / 2;
            }
        }
        scaleText.setText(zoomScale + "x");
    }

    // REQUIRES: inputted String slowOrFast is one of "<<" and ">>"
    // MODIFIES: this
    // EFFECTS: sets the speed multiplier. 
    //          If the inputted String is ">>", sets the speed to the next speed
    //          in the speeds array if the current speed is not the last
    //          If the inputted String is "<<", sets the speed to the previous 
    //          speed in the speeds array if the current speed is not the first
    //          changes windText to display zoom value to the current value
    private void adjustSpeed(String slowOrFast) {
        int index = simSpeeds.indexOf(simSpeedScale);
        if (slowOrFast.equals(">>")) {
            if (index < simSpeeds.size() - 1) {
                simSpeedScale = simSpeeds.get(index + 1);
            }
        } else if (slowOrFast.equals("<<")) {
            if (index > 0) {
                simSpeedScale = simSpeeds.get(index - 1);
            }
        }
        simSpeedText.setText(simSpeedScale + "x");
    }

    // EFFECTS: tells user if the file name field is empty,
    //          otherwise tries to write the data into the file with the inputted name in 
    //          the file name. If file not found exception is caught, tells user the file is
    //          is not found
    //          Resets the file name field to blank
    // Source: CPSC 210 Json sample
    //         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
    private void saveEarthFile() {
        if (fileNameField.getText().isBlank()) {
            fileErrorLabel.setText("Enter a name!");
        } else {
            jsonWriter = new JsonWriter("./data/" + fileNameField.getText() + ".json");
            try {
                jsonWriter.open();
                jsonWriter.write(earth);
                jsonWriter.close();
                fileErrorLabel.setText("Write Successful!");
            } catch (FileNotFoundException e) {
                fileErrorLabel.setText("File Not Found!");
            }
        }
        fileNameField.setText("");
    }

    // MODIFIES: this
    // EFFECTS: tells user if the file name field is empty,
    //          otherwise tries loads the Earth file with the inputted name in 
    //          the file name, and sets the thrust toggle button and thrust slider to the 
    //          current rocket's values. If file not found exception is caught, 
    //          tells user the file is not found
    //          Resets the file name field to blank
    // EFFECTS: 
    // Source: CPSC 210 Json sample
    //         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
    private void loadEarthFile() {
        if (fileNameField.getText().isBlank()) {
            fileErrorLabel.setText("Enter a name!");
        } else {
            jsonReader = new JsonReader("./data/" + fileNameField.getText() + ".json");
            try {
                earth = jsonReader.read();
                currentRocket = earth.getCurrentRocket();
                fileErrorLabel.setText("Read Successful!");
                if (earth.getCurrentRocket().getThrust().getNorm() > 0) {
                    double thrust = earth.getCurrentRocket().getThrust().getNorm() / Rocket.MAX_THRUST * 100;
                    turnThrustButton.setText("Thrust On");
                    thrustSlider.setValue((int) thrust);
                    thrustOn = true;
                } else {
                    turnThrustButton.setText("Thrust Off");
                    thrustOn = false;
                }
            } catch (IOException e) {
                fileErrorLabel.setText("File Not Found!");
            }
        }
        fileNameField.setText("");
    }

    // Getters
    // EFFECTS: returns the zoom scale
    public double getZoomScale() {
        return zoomScale;
    }

    // EFFECTS: returns the speed scale
    public int getSimSpeedScale() {
        return simSpeedScale;
    }

    // EFFECTS: returns the earth from user panel
    public Earth getEarth() {
        return this.earth;
    }

    // EFFECTS: creates and returns row with button included
    private JPanel formatButtonRow(JButton b) {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(b);

        return p;
    }

}
