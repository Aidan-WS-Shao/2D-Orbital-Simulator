package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.*;

// Represents the window to display the event log
@ExcludeFromJacocoGeneratedReport
public class LogDisplay extends JFrame {
    private static final int LBL_WIDTH = 450;
    private static final int PANEL_HEIGHT = 500;
    
    // EFFECTS: creates a new log display frame and adds the display panel
    public LogDisplay() {
        super("Event Log");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);
        setBackground(new Color(180, 180, 180));
        setPreferredSize(new Dimension(LBL_WIDTH, PANEL_HEIGHT));
        add(displayPanel());
        pack();
        centreOnScreen();
        setVisible(true);
    }

    // EFFECTS: creates the scroll pane for the display panel by adding 
    //          all the logs in a text area
    private JScrollPane displayPanel() {
        JTextArea log = new JTextArea("Event Log \n\n");
        log.setEditable(false);
        EventLog eventLog = EventLog.getInstance();
        for (Event event : eventLog) {
            log.setText(log.getText() + event.toString() + "\n\n");
        }

        return new JScrollPane(log);
    }

    // Centres frame on desktop
	// modifies: this
	// effects: location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize(); 
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }
}
