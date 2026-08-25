package ui;

import model.*;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import persistence.JsonReader;

// Represents the menu to provide options to load data or 
// start a new simulation.
//
// Structure from: CPSC210 SpaceInvaders, SmartHome
//                 (repo: https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase.git)
//                 (repo: https://github.students.cs.ubc.ca/CPSC210/LongFormProblemStarters.git)
@ExcludeFromJacocoGeneratedReport
public class StartMenu extends JPanel {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private static final String DEFAULT_JSON_STORE = "./data/earth.json";
    private static final int CENTER = SwingConstants.CENTER;

    private Earth earth;
    private JPanel menu;
    private GridLayout layout;

    private JLabel welcomeMessage;

    private JButton loadButton;

    private JButton addRocketButton;

    private JLabel enterNameLabel;
    private JTextField rocketNameField;
    private JLabel emptyNameWarning;

    private JsonReader jsonReader;
    private JLabel enterFileLabel;
    private JTextField fileNameField;
    private JLabel fileNameWarning;

    private boolean startMenuRunning;

    // EFFECTS: creates the start menu by providing an option to load data
    //          or create a new simulation with a new rocket
    public StartMenu(Earth earth) {
        this.earth = earth;
        jsonReader = new JsonReader(DEFAULT_JSON_STORE);
        layout = new GridLayout(7,1);
        menu = new JPanel(layout);
        welcomeMessage = new JLabel("Start Spaceflight Simulation", CENTER);
        fileNameWarning = new JLabel("", CENTER);
        emptyNameWarning = new JLabel("", CENTER);
        menu.add(welcomeMessage);
        loadDataButton();
        enterFileName();
        firstRocketButton();
        enterRocketName();
        this.add(menu);
        startMenuRunning = true;
    }

    // EFFECTS: creates the load data button for interation
    //          that loads the data to earth when clicked
    public void loadDataButton() {
        loadButton = new JButton("Load Data");
        JPanel buttonRow = formatButtonRow(loadButton);
        buttonRow.setSize(WIDTH, HEIGHT / 6);

        loadButton.addActionListener(new ButtonListener());

        menu.add(buttonRow);
    }

    // EFFECTS: creates a text field for user to enter new
    //          file name
    public void enterFileName() {
        JPanel row = new JPanel();
        enterFileLabel = new JLabel("Enter File Name:");
        fileNameField = new JTextField(10);
        row.add(enterFileLabel);
        row.add(fileNameField);
        menu.add(row);
        menu.add(fileNameWarning);
    }

    // EFFECTS: creates the add rocket button for interaction
    //          that adds a new rocket to earth when clicked
    public void firstRocketButton() {
        addRocketButton = new JButton("Start New Simulation");
        JPanel buttonRow = formatButtonRow(addRocketButton);
        buttonRow.setSize(WIDTH, HEIGHT / 6);
        addRocketButton.addActionListener(new ButtonListener());

        menu.add(buttonRow);
    }

    // EFFECTS: creates a text field for user to enter new
    //          rocket name
    public void enterRocketName() {
        JPanel row = new JPanel();
        enterNameLabel = new JLabel("Enter Rocket Name:");
        rocketNameField = new JTextField(10);
        row.add(enterNameLabel);
        row.add(rocketNameField);
        menu.add(row);
        menu.add(emptyNameWarning);
    }

    @ExcludeFromJacocoGeneratedReport
    private class ButtonListener implements ActionListener {
        //MODIFIES: this
        //EFFECTS: if "Load Data" button clicked, loads in data from file.
        //         if "Start New Simulation" button clicked, checks if entered field is empty,
        //         if not then creates new rocket with inputted name and adds to this earth.
        //         if entered field is empty, tells user to enter a name
        //         sets status of menu running to false when the load data action or create new
        //         rocket action is accepted.
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Load Data")) {
                readEarth();
            } else if (command.equals("Start New Simulation")) {
                if (rocketNameField.getText().isBlank()) {
                    emptyNameWarning.setText("Enter a name!");
                } else {
                    try {
                        earth.addRocket(rocketNameField.getText());
                        startMenuRunning = false;
                    } catch (Exception exception) {
                        // Should not happen
                    }
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: tells user if the file name field is empty,
    //          otherwise tries loads the Earth file with the inputted name in 
    //          the file name. If file not found exception is caught, tells user 
    //          the file is not found
    //          Resets the file name field to blank
    private void readEarth() {
        if (fileNameField.getText().isBlank()) {
            fileNameWarning.setText("Enter a name!");
        } else {
            jsonReader = new JsonReader("./data/" + fileNameField.getText() + ".json");
            try {
                this.earth = jsonReader.read();
                startMenuRunning = false;
            } catch (IOException e1) {
                fileNameWarning.setText("No file found!");
            }
        }
        fileNameField.setText("");
    }

    // EFFECTS: returns the status of the start menu
    public boolean getRunning() {
        return startMenuRunning;
    }

    // EFFECTS: returns the earth gotten from the start menu
    public Earth getEarth() {
        return this.earth;
    }

    //EFFECTS: creates and returns row with button included
    public JPanel formatButtonRow(JButton b) {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(b);

        return p;
    }

}
