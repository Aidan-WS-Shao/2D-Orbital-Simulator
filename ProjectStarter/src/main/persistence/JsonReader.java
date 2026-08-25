package persistence;

import model.Earth;
import model.Rocket;
import model.Vector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
// Source: CPSC 210 Project JSON sample 
//         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads earth from file and returns it;
    //          throws IOException if an error occurs reading data from file
    public Earth read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseEarth(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses earth from JSON object and returns it
    private Earth parseEarth(JSONObject jsonObject) {
        String name = jsonObject.getString("name");        
        Earth earth = new Earth(name);
        addRockets(earth, jsonObject);
        return earth;
    }

    // MODIFIES: Earth
    // EFFECTS: parses rockets from JSON object and adds them to earth
    private void addRockets(Earth earth, JSONObject jsonObject) {
        String currentRocketName = jsonObject.getString("current rocket");
        JSONArray jsonArray = jsonObject.getJSONArray("rockets");
        for (Object json : jsonArray) {
            JSONObject nextRocket = (JSONObject) json;
            addRocket(earth, nextRocket);
        }
        earth.setCurrentRocket(currentRocketName);
    }

    // MODIFIES: Earth
    // EFFECTS: parses rocket from JSON object and adds it to earth
    private void addRocket(Earth earth, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Vector thrust = new Vector(jsonObject.getDouble("thrust X"), jsonObject.getDouble("thrust Y"), 0.0);
        Vector accel = new Vector(jsonObject.getDouble("acceleration X"), jsonObject.getDouble("acceleration Y"), 0.0);
        Vector vel = new Vector(jsonObject.getDouble("velocity X"), jsonObject.getDouble("velocity Y"), 0.0);
        Vector disp = new Vector(jsonObject.getDouble("displacement X"), jsonObject.getDouble("displacement Y"), 0.0);
        Rocket rocket = new Rocket(name);
        rocket.setName(jsonObject.getString("name"));
        rocket.setMass(jsonObject.getDouble("mass"));
        rocket.setAngle(jsonObject.getDouble("angle"));
        rocket.setThrust(thrust);
        rocket.setAccel(accel);
        rocket.setVel(vel);
        rocket.setDisp(disp);
        rocket.getTrajectory().setInitialConditions(disp, vel);
        try {
            earth.addRocket(rocket);
        } catch (Exception exception) {
            // should not happen
        }
    }
}
