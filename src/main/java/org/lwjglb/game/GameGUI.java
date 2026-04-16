package org.lwjglb.game;

/**
 * Author: Leon Wasiliew
 * Subject: INFT4000 - Special Topics 1
 * Creation Date: 2026-04-15
 * Modification Date: 2026-04-15
 * Description: GameGUI provides a Swing interface for issuing commands to the game engine.
 * It exposes buttons for spawning cars & planes, clearing the scene, and resetting the world.
 */

import javax.swing.*;
import java.awt.event.ActionListener;

public class GameGUI {

    private JPanel panelMain;

    private JTextArea keyboardHelp;

    private JButton addCarButton;
    private JButton addPlaneButton;
    private JButton clearButton;
    private JButton resetButton;
    private JButton fasterButton;
    private JButton slowerButton;
    private JButton leftButton;
    private JButton rightButton;
    private JButton upButton;
    private JButton downButton;


    // Defines command flags used by the game loop
    private static boolean addCarCommand = false;
    private static boolean addPlaneCommand = false;
    private static boolean clearCommand = false;
    private static boolean resetCommand = false;
    private static boolean fasterCommand = false;
    private static boolean slowerCommand = false;
    private static boolean leftCommand = false;
    private static boolean rightCommand = false;
    private static boolean upCommand = false;
    private static boolean downCommand = false;

    /**
     * Constructs the GUI and attaches button listeners that
     * set command flags for the game engine to process.
     */
    public GameGUI() {

        /**
         * Handles the "Add Car" button press.
         */
        addCarButton.addActionListener(e -> {
            addCarCommand = true;
            System.out.println("GUI: Add Car");
        });

        /**
         * Handles the "Add Plane" button press.
         */
        addPlaneButton.addActionListener(e -> {
            addPlaneCommand = true;
            System.out.println("GUI: Add Plane");
        });

        /**
         * Handles the "Clear" button press.
         */
        clearButton.addActionListener(e -> {
            clearCommand = true;
            System.out.println("GUI: Clear");
        });

        /**
         * Handles the "Reset" button press.
         */
        resetButton.addActionListener(e -> {
            resetCommand = true;
            System.out.println("GUI: Reset");
        });

        // Defines all Vehicle-based controls (Last Vehicle Only)
        fasterButton.addActionListener(e -> fasterCommand = true);
        slowerButton.addActionListener(e -> slowerCommand = true);
        leftButton.addActionListener(e -> leftCommand = true);
        rightButton.addActionListener(e -> rightCommand = true);
        upButton.addActionListener(e -> upCommand = true);
        downButton.addActionListener(e -> downCommand = true);
    }

    /**
     * Creates and displays the GUI window containing the control panel.
     */
    public static void guiSetup() {
        JFrame frame = new JFrame("GameGUI");
        frame.setContentPane(new GameGUI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(150, 150);
        frame.setVisible(true);
    }

    /**
     * Returns and clears the "Add Car" command flag.
     */
    public static boolean getCarAddCommand() {
        boolean value = addCarCommand;
        addCarCommand = false;
        return value;
    }

    /**
     * Returns and clears the "Add Plane" command flag.
     */
    public static boolean getPlaneAddCommand() {
        boolean value = addPlaneCommand;
        addPlaneCommand = false;
        return value;
    }

    /**
     * Returns and clears the "Clear" command flag.
     */
    public static boolean getClearCommand() {
        boolean value = clearCommand;
        clearCommand = false;
        return value;
    }

    /**
     * Returns and clears the "Reset" command flag.
     */
    public static boolean getResetCommand() {
        boolean value = resetCommand;
        resetCommand = false;
        return value;
    }

    // Getters for the Vehicle-based command flags
    public static boolean getFasterCommand() { boolean v = fasterCommand; fasterCommand = false; return v; }
    public static boolean getSlowerCommand() { boolean v = slowerCommand; slowerCommand = false; return v; }
    public static boolean getLeftCommand() { boolean v = leftCommand; leftCommand = false; return v; }
    public static boolean getRightCommand() { boolean v = rightCommand; rightCommand = false; return v; }
    public static boolean getUpCommand() { boolean v = upCommand; upCommand = false; return v; }
    public static boolean getDownCommand() { boolean v = downCommand; downCommand = false; return v; }
}