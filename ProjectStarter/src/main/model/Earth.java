package model;

import java.awt.Color;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import model.exceptions.NameTakenException;
import model.exceptions.RocketOnLaunchPadException;
import persistence.Writable;

// Represents Earth with constants of Earth like mass and radius. Collects and 
// handles all rockets placed in the Earth object
// Domain analysis:
// Fields: rockets as a list of Rocket, currentRocket as the selected rocket to do
//         specific methods on
// Methods:
//      - Adds a new rocket with given name to list of rockets
//      - Increments all rockets with time
//      - Gets the list of rockets
public class Earth implements Writable {
    public static final double GRAV_CONSTANT_TIMES_MASS = 3.9859e14;
    public static final double RADIUS = 6378.0e3;
    public static final double DT = 0.05;
    public static final Color COLOUR = new Color(118, 212, 222);
    public static final double D_ANGLE = 2.0;

    private String name;
    private ArrayList<Rocket> rockets;
    private Rocket currentRocket;

    // EFFECTS: creates new Earth object with empty list of rockets
    public Earth(String name) {
        this.name = name;
        rockets = new ArrayList<>();
        currentRocket = null;
    }

    // MODIFIES: this
    // EFFECTS: checks if there is a rocket in the origin, adds new rocket
    //          with the given name to the list of rockets.
    //          Throws NameTakenException if the name is already taken
    //          Throws RocketOnLaunchPadException if there is a rocket already in
    //          the launch base
    //          Changes the currentRocket to the given
    public void addRocket(String name) throws NameTakenException, RocketOnLaunchPadException {
        Vector disp;
        for (Rocket rocket : rockets) {
            disp = rocket.getDisplacement();
            if (rocket.getName().equals(name)) {
                throw new NameTakenException();
            } else if (disp.getX() == 0 && disp.getY() == RADIUS) {
                throw new RocketOnLaunchPadException();
            }
        }
        currentRocket = new Rocket(name);
        rockets.add(currentRocket);
        EventLog.getInstance().logEvent(new Event("New rocket added and selected with name: " + name));
    }

    // MODIFIES: this
    // EFFECTS: checks if there is a rocket in the origin, adds the given
    //          new rocket to the list of rockets.
    //          Throws NameTakenException if the name is already taken
    //          Throws RocketOnLaunchPadException if there is a rocket already in
    //          the launch base
    //          Changes the currentRocket to the given
    public void addRocket(Rocket newRocket) throws NameTakenException, RocketOnLaunchPadException {
        Vector disp;
        for (Rocket rocket : rockets) {
            disp = rocket.getDisplacement();
            if (rocket.getName().equals(newRocket.getName())) {
                throw new NameTakenException();
            } else if (disp.getX() == 0 && disp.getY() == RADIUS) {
                throw new RocketOnLaunchPadException();
            }
        }
        currentRocket = newRocket;
        rockets.add(currentRocket);
        EventLog.getInstance().logEvent(new Event("New rocket added with name: " + newRocket.getName()));
    }

    // MODIFIES: this
    // EFFECTS: removes rocket with the given name from Earth and returns true
    //          then sets the current rocket to the first rocket in the updated list,
    //          sets current rocket to null if the list becomes empty
    //          if no rocket with name found, returns false
    public boolean removeRocket(String name) {
        for (Rocket rocket : rockets) {
            if (rocket.getName().equals(name)) {
                rockets.remove(rocket);
                EventLog.getInstance().logEvent(new Event("Removed rocket with name: " + name));
                if (currentRocket.getName().equals(name)) {
                    if (rockets.isEmpty()) {
                        this.currentRocket = null;
                    } else {
                        this.currentRocket = rockets.get(0);
                        EventLog.getInstance()
                                .logEvent(new Event("Selected rocket with name: " + currentRocket.getName()));
                    }
                }
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: increments every rocket on the map
    public void increment() {
        for (Rocket rocket : rockets) {
            rocket.increment();
        }
    }

    // MODIFIES: this
    // EFFECTS: renames this earth to the given name
    public void setName(String name) {
        this.name = name;
    }

    // EFFECTS: returns the name of the Earth
    public String getName() {
        return name;
    }

    // EFFECTS: returns the list of rockets
    public ArrayList<Rocket> getRockets() {
        return rockets;
    }

    // EFFECTS: returns the currentRocket
    public Rocket getCurrentRocket() {
        return currentRocket;
    }

    // MODIFIES: this
    // EFFECTS: sets the current rocket to the given name, returns
    //          false if no rocket with that name found
    public boolean setCurrentRocket(String name) {
        for (Rocket rocket : rockets) {
            if (rocket.getName().equals(name)) {
                currentRocket = rocket;
                EventLog.getInstance().logEvent(new Event("Set selected rocket to: " + name));
                return true;
            }
        }
        return false;
    }

    // Source: CPSC 210 Json Sample
    //         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        if (currentRocket == null) {
            json.put("current rocket", "null");
        } else {
            json.put("current rocket", currentRocket.getName());
        }
        json.put("rockets", earthToJson());
        EventLog.getInstance().logEvent(new Event("Saved simulation to file"));
        return json;
    }

    // Source: CPSC 210 Json Sample
    //         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray earthToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Rocket rocket : rockets) {
            jsonArray.put(rocket.toJson());
        }

        return jsonArray;
    }
}
