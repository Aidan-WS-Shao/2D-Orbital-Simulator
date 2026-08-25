package ui;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class Main {
    public static void main(String[] args) throws Exception {
        SimulatorApp main = new SimulatorApp();
        while (main.isActive()) {
            // Let simulator app run
        }
        new LogDisplay();
    }
}
