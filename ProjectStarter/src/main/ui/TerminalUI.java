package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.*;
import model.exceptions.NameTakenException;
import model.exceptions.RocketOnLaunchPadException;
import persistence.JsonReader;
import persistence.JsonWriter;

// Represents the simulator app that contains all the methods in the simulator,
// for main to run
// Domain analysis:
// Fields: earth, selectedRocket, scanner for user input, isProgramRunning for 
//         program running state
// Methods:
//      - Simulator constructor
//      - Method to provide menu for user for next steps
//      - Method to accept user requests based on the menu
//      - Method to accept number of times to increment
//      - Method to increment however many times user wants
//      - Method to select the requested rocket
//      - Method to accept thrust amount and assign thrust amount 
//        to selectedRocket
//      - Method to accept angle amount and turn rocket by that amount
//        of angle
//      - Method to see information of current state of rocket
@ExcludeFromJacocoGeneratedReport
public class TerminalUI {
    private static final String JSON_STORE = "./data/earth.json";
    public static final int PANEL_HEIGHT = 800;
    private Earth earth;
    private Rocket selectedRocket;

    private Scanner scanner;
    private boolean isProgramRunning;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public TerminalUI() {
        this.earth = new Earth("Earth");
        selectedRocket = earth.getCurrentRocket();

        this.scanner = new Scanner(System.in);
        isProgramRunning = true;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        while (isProgramRunning) {
            handleMenu();
        }
    }

    // Structure source: Lab 4 FlashcardReviewer.java
    // EFFECTS: displays main display menu, accept input from user,
    //          and proccesses the input
    public void handleMenu() {
        mainDisplayMenu();
        String input = this.scanner.nextLine();
        processMainOptions(input);
    }

    // Structure source: Lab 4 FlashcardReviewer.java
    // EFFECTS: displays options for user to use in the main menu
    public void mainDisplayMenu() {
        System.out.println("Please select option:");
        System.out.println("a: add new rocket");
        System.out.println("v: view names of all rockets");
        System.out.println("s: select rocket");
        System.out.println("r: see options for selected rocket");
        System.out.println("i: increment all rockets on the map by given amount");
        System.out.println("w: save earth to file");
        System.out.println("l: load earth from file");
        System.out.println("q: quit the application");
    }

    // Structure source: Lab 4 FlashcardReviewer.java and CPSC 210 Json Sample
    //                   (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
    // EFFECTS: takes user input from main menu and processes the inputs
    public void processMainOptions(String input) {
        if (input.equals("a")) {
            addRocket();
        } else if (input.equals("s")) {
            selectRocket();
        } else if (input.equals("v")) {
            displayRocketNames();
        } else if (input.equals("r")) {
            getRocketOptions();
        } else if (input.equals("i")) {
            increment();
        } else if (input.equals("w")) {
            saveEarthFile();
        } else if (input.equals("l")) {
            loadEarthFile();
        } else if (input.equals("q")) {
            quitApplication();
        } else {
            System.out.println("Invalid, please try again");
        }
    }

    // Structure source: Lab 4 FlashcardReviewer.java
    // EFFECTS: displays names of all rockets placed on Earth
    public void displayRocketNames() {
        System.out.println("Current rockets");
        for (Rocket rocket : earth.getRockets()) {
            System.out.println(rocket.getName());
        }
    }

    // EFFECTS: accepts rocket name from user and adds rocket
    //          to the list of rockets. Changes selected rocket
    //          to the added rocket.
    public void addRocket() {
        System.out.print("Enter new rocket name: ");
        String name = this.scanner.nextLine();
        try {
            earth.addRocket(name);
        } catch (NameTakenException nte) {
            System.out.println("Name already taken!");
        } catch (RocketOnLaunchPadException rolpe) {
            System.out.println("There is currently a rocket on launch pad!");
            System.out.println("Launch the first rocket before adding another.");
        }
        selectedRocket = earth.getCurrentRocket();
    }

    // MODIFIES: this
    // EFFECTS: changes the selected rocket to the rocket
    //          with the given name
    public void selectRocket() {
        System.out.print("Enter rocket name to select: ");
        String name = this.scanner.nextLine();
        earth.setCurrentRocket(name);
        selectedRocket = earth.getCurrentRocket();
        System.out.println("Selected rocket: " + this.selectedRocket.getName());
    }

    // Structure source: Lab 4 FlashcardReviewer.java
    // EFFECTS: displays options to operate on the selected rocket to
    //          user and gets input from user
    //          Loops on this menu until told otherwise to return to main menu
    public void getRocketOptions() {
        String input = "";
        if (selectedRocket == null) {
            System.out.println("No rockets selected!");
        } else {
            while (!(input.equals("b"))) {
                selectedRocketDisplayMenu();
                input = this.scanner.nextLine();
                processSelectedRocketOptions(input);
            }
        }
    }

    // Structure source: Lab 4 FlashcardReviewer.java
    // EFFECTS: displays options for user to use for the selected rocket
    public void selectedRocketDisplayMenu() {
        System.out.println("Selected Rocket: " + this.selectedRocket.getName());
        System.out.println("t: set rocket thrust");
        System.out.println("r: rotate rocket by given amount clockwise");
        System.out.println("v: view information of the selected rocket");
        System.out.println("b: return to main menu");
    }

    // Structure source: Lab 4 FlashcardReviewer.java
    // EFFECTS: takes user input and processes for the rocket options
    public void processSelectedRocketOptions(String input) {
        switch (input) {
            case "t":
                acceptAndAssignThrust();
                break;
            case "r":
                acceptAndAssignAngle();
                break;
            case "v":
                seeInformation();
                break;
            case "b":
                break;
            default:
                System.out.println("Invalid, please try again");
        }
    }

    // MODIFIES: this
    // EFFECTS: takes user input for thrust and sets the thrust
    //          of the selected rocket
    public void acceptAndAssignThrust() {
        System.out.print("Enter thrust for selected rocket: ");
        String thrust = this.scanner.nextLine();
        selectedRocket.setThrust(Double.parseDouble(thrust));
    }

    // MODIFIES: this
    // EFFECTS: takes user input for angle and sets the angle
    //          of the selected rocket
    public void acceptAndAssignAngle() {
        System.out.print("Enter angle to turn rocket by: ");
        String angle = this.scanner.nextLine();
        selectedRocket.turnAngleBy(Double.parseDouble(angle));
    }

    // MODIFIES: this
    // EFFECTS: takes user input for increment and increments
    //          all rockets by the amount of times provided
    public void increment() {
        System.out.print("Enter number of times to increment: ");
        String numberOfIncrements = this.scanner.nextLine();
        for (int i = 0; i < Integer.parseInt(numberOfIncrements); i++) {
            earth.increment();
        }
    }

    // EFFECTS: displays the current conditions of the selected rocket
    //          including position, speed, thrust, angle, Trajectory coordinates
    public void seeInformation() {
        Vector disp = selectedRocket.getDisplacement();
        double distance = disp.getNorm() - Earth.RADIUS;
        Vector velocity = selectedRocket.getVel();
        double speedWithDir = disp.unit().dotProd(velocity.unit()) * (Math.round(velocity.getNorm() * 1000.0) / 1000.0)
                / Math.abs(disp.unit().dotProd(velocity.unit()));
        ArrayList<Double> trajectoryXCoords = selectedRocket.getTrajectory().getXCoords();
        ArrayList<Double> trajectoryYCoords = selectedRocket.getTrajectory().getYCoords();
        System.out.println("Rocket: " + selectedRocket.getName() + "\n");
        System.out.println("Angle from vertical: " + selectedRocket.getAngle() + " degrees");
        System.out.println("Position: [x: " + Math.round(disp.getX() * 1000.0) / 1000.0 + "m, y: "
                + Math.round(disp.getY() * 1000.0) / 1000.0 + "m]");
        System.out.println("Distance from ground: " + Math.round(distance * 1000.0) / 1000.0 + "m");
        System.out.println("Velocity: [Vx: " + Math.round(velocity.getX() * 1000.0) / 1000.0 + "m/s, Vy: "
                + Math.round(velocity.getY() * 1000.0) / 1000.0 + "m/s]");
        System.out.println("Speed: " + speedWithDir + "m/s");
        System.out.println("Thrust: " + Math.round(selectedRocket.getThrust().getNorm() * 1000.0) / 1000.0 + "N");
        System.out.println("Trajectory Information:\n   x      y   ");
        for (int j = 0; j < trajectoryXCoords.size(); j++) {
            double singleXCoord = Math.round(trajectoryXCoords.get(j) * 1000.0) / 1000.0;
            double singleYCoord = Math.round(trajectoryYCoords.get(j) * 1000.0) / 1000.0;
            System.out.println(singleXCoord + "m " + singleYCoord + "m");
        }
    }

    // EFFECTS: saves this earth to the file
    // Source: CPSC 210 Json sample
    //         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
    public void saveEarthFile() {
        try {
            jsonWriter.open();
            jsonWriter.write(earth);
            jsonWriter.close();
            System.out.println("Saved " + earth.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the dedicated Earth file
    // Source: CPSC 210 Json sample
    //         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
    public void loadEarthFile() {
        try {
            earth = jsonReader.read();
            selectedRocket = earth.getCurrentRocket();
            System.out.println("Loaded " + earth.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets isProgramRunning state to false
    public void quitApplication() {
        this.isProgramRunning = false;
    }

}
