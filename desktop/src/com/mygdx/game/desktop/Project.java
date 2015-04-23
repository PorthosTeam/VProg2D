package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Node;
import com.mygdx.game.VProgEngine;
import java.util.ArrayList;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;

/**
 * @description Stores the project's title, an array of all existing nodes
 * within the project, global variables relating to the project, and the
 * associated JDK for the the game.
 *
 * @author Trevor Richardson
 * @date Mar. 22 2015
 * @module CreateProject
 */
public class Project extends JPanel implements ActionListener {

    // Project variables
    private String title;
    private ArrayList<Node> nodes;
    private ArrayList<String> globalVars;
    private JDK jdk;

    // Display variables
    private JTextField projName;
    private JButton projDir;
    private JTextField displayProjDir;
    private JButton createButton;
    JFileChooser file;
    URL dir;
    String dirString;

    // Create a new project
    public Project() {

        // Init layout and construct dialog window
        super(new FlowLayout());

        // Project name input
        add(new JLabel("Project name: ", JLabel.CENTER));
        projName = new JTextField(36);
        add(projName);

        // Project directory input & selection
        add(new JLabel("Project directory: ", JLabel.CENTER));
        displayProjDir = new JTextField(25);
        displayProjDir
                .setToolTipText("Enter the folder you wish to store the project in.");
        add(displayProjDir);
        displayProjDir.setEditable(false);
        projDir = new JButton("Select Folder");
        // Button listener for directory selection
        projDir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                file = new JFileChooser();
                file.setCurrentDirectory(new java.io.File("."));
                file.setDialogTitle("Select Project Directory");
                file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                // disable "All files" option
                file.setAcceptAllFileFilterUsed(false);
                if (file.showOpenDialog((Component) e.getSource()) == JFileChooser.APPROVE_OPTION) {
                    dirString = "" + file.getSelectedFile();
                    displayProjDir.setText(dirString);
                    dir = Project.class.getProtectionDomain().getCodeSource().getLocation();
                    dirString = dir.getFile();
                } else {
                    System.out.println("Closed without selection.");
                }
            }
        });
        add(projDir);

        // Create button, verifies the entered information is correct and checks for the JDK
        add(new JLabel(""));
        createButton = new JButton("Create");
        createButton.addActionListener(this);
        add(createButton);
        add(new JLabel(""));

    }

    public void WriteProject() throws IOException {
        PrintWriter writer = new PrintWriter(new FileOutputStream(new File(dirString + "Projects.txt"), false));
        writer.println(title);
        writer.close();
    }

    public void actionPerformed(ActionEvent e) {
        // Find system's JDK, if present / installed correctly
        /*if (System.getenv("JAVA_HOME") == null) {
         JOptionPane
         .showMessageDialog(
         null,
         "Your JAVA_HOME variable is not set. Please make sure you installed the JDK on your system correctly",
         "No JDK Found", 0);
         } */
        // Check project name
        if (projName.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter a project name",
                    "Error", 0);
        } // Check project directory
        else if (displayProjDir.getText().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Please specify a project directory", "Error", 0);
        } // Everything verified, creating project
        else {
            //String jdkDir = System.getenv("JAVA_HOME");
            //String version = System.getProperty("java.specification.version");
            //jdk = new JDK(jdkDir, version);

            title = projName.getText();
            boolean exists = false;
            boolean fileCreated = false;

            // if projects file doesn't exist, create it an add the current project
            if (!new File(dirString + "Projects.txt").isFile()) {
                try {
                    WriteProject();
                    fileCreated = true;
                } catch (IOException ioe) {
                    //
                }
            } // otherwise, check the current file for the desired title
            else {
                try {
                    File projFile = new File(dirString + "Projects.txt");
                    BufferedReader br = new BufferedReader(new FileReader(projFile));
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.equals(title)) {
                            exists = true;
                        }
                    }
                } catch (IOException ioe) {
                    //
                }
            }
            // append if it doesn't exist
            if (!exists) {
                if (!fileCreated) {
                try {
                    PrintWriter writer = new PrintWriter(new FileOutputStream(new File(dirString + "Projects.txt"), true));
                    writer.println(title);
                    writer.close();
                } catch (IOException ioe) {
                    //
                }
                }

                // Open game frame
                MainUI.vprog = new VProgEngine(title);

                LwjglApplicationConfiguration config
                        = new LwjglApplicationConfiguration();
                config.title = title;
                config.x = -1;
                config.y = -1;
                config.width = Math.min(MainUI.screenBounds.width, 800);
                config.height = Math.min(MainUI.screenBounds.height, 600);
                config.resizable = true;
                config.allowSoftwareMode = true;
                new LwjglApplication(MainUI.vprog, config);
                JFrame projectFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                projectFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Project already exists!", "Error", 0);
            }
        }
    }

// JFrame size
    public Dimension getPreferredSize() {
        return new Dimension(530, 125);
    }

    // Getters/Setters
    public void setTitle(String _title) {
        title = _title;
    }

    public String getTitle() {
        return title;
    }

    public void addGlobalVar(String _var) {
        globalVars.add(_var);
    }

    public ArrayList<String> getGlobalVars() {
        return globalVars;
    }

    public void addNode(Node _node) {
        nodes.add(_node);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setJDK(JDK _jdk) {
        jdk = _jdk;
    }

    public JDK getJDK() {
        return jdk;
    }

    public void export() {
        // TBD, need interface from other module
    }

    public void compile(String _file) {
        // TBD, need interface from other module
    }

}
