package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.*;

// Represents the simulator app to run the start menu, then the simulation and user panels for
// the simulation
@ExcludeFromJacocoGeneratedReport
public class SimulatorApp extends JFrame implements WindowListener {
    private static final int INTERVAL = 50;
    public static final int PANEL_HEIGHT = 800;
    public static final int INTERACTION_PANEL_HEIGHT = 420;
    public static final int DISPLAY_PANEL_HEIGHT = PANEL_HEIGHT - INTERACTION_PANEL_HEIGHT;
    private Earth earth;

    private StartMenu startMenu;
    private DataDisplayPanel dataDisplayPanel;
    private UserInteractionPanel userInteractionPanel;
    private SimulationPanel simPanel;

    // Structure source: Lab 4 FlashcardReviewer.java
    // EFFECTS: constructs simulator app with new Earth
    //          Starts the program by runnning the start menu until a valid
    //          option is chosen, then run the main simulation
    public SimulatorApp() {
        super("Spaceflight Sim");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);
        this.earth = new Earth("Earth");
        runStartMenu();
        startSimulationPanel();
        addTimer();
    }

    // MODIFIES: this
    // EFFECTS: runs the start menu until a valid option is chosen
    private void runStartMenu() {
        startMenu = new StartMenu(this.earth);
        add(startMenu);
        pack();
        centreOnScreen();
        while (startMenu.getRunning()) {
            setVisible(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: closes the start menu and starts the simulation and user panel
    //          which includes the data display panel and the user interaction panel
    private void startSimulationPanel() {
        this.earth = startMenu.getEarth();
        simPanel = new SimulationPanel(this.earth);
        dataDisplayPanel = new DataDisplayPanel(this.earth);
        userInteractionPanel = new UserInteractionPanel(this.earth);
        remove(startMenu);
        add(simPanel);
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.PAGE_AXIS));
        userPanel.add(dataDisplayPanel);
        userPanel.add(userInteractionPanel);
        add(userPanel, BorderLayout.EAST);
        pack();
        centreOnScreen();
        setVisible(true);
    }

    // Set up timer
    // MODIFIES: this
    // EFFECTS: initializes a timer that updates game each INTERVAL milliseconds.
    //          updates this earth to the userPanel's earth if a new one is loaded
    //          increments the earth the number of times specified by the User Panel
    //          updates the User Panel with earth's new values
    //          updates the Simulation Panel by updating its zoom scale and repainting
    private void addTimer() {
        Timer t = new Timer(INTERVAL, new ActionListener() {
            @Override
            @ExcludeFromJacocoGeneratedReport
            public void actionPerformed(ActionEvent ae) {
                earth = userInteractionPanel.getEarth();
                for (int i = 0; i < userInteractionPanel.getSimSpeedScale(); i++) {
                    earth.increment();
                }
                dataDisplayPanel.update(earth);
                simPanel.updateEarthAndScaleFactor(earth, userInteractionPanel.getZoomScale());
                simPanel.repaint();
            }
        });

        t.start();
    }

    // Centres frame on desktop
	// modifies: this
	// effects: location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize(); 
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        
    }

    @Override
    public void windowClosed(WindowEvent e) {
        this.dispose();
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
