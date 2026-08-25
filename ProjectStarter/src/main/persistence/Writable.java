package persistence;

import org.json.JSONObject;

// Source: CPSC 210 Project JSON sample
//         (repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git)
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
