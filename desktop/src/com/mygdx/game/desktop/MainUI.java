package com.mygdx.game.desktop;

// This file is the central hub of the 'primary' or 'editor' UI.
// This is for grabbing available space for our window(s) on the screen.
import com.mygdx.game.WindowBoundsChecker;
import java.awt.Rectangle;

// This is for the editable-game engine.
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.VProgEngine;
import com.mygdx.game.Callback;

// This is for the graphical user interface.
import java.util.ArrayList;
import java.awt.Component;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.*;
import java.io.IOException;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;

public class MainUI {

    // This will hold the screen area available to us for our windows (we don't
    // want to open windows bigger than these bounds if we can avoid it).
    public static Rectangle screenBounds;

    // The actual editable game engine.
    public static VProgEngine vprog;

    public Project newProject;
    public ArrayList<String> projectNames;
    String selectedProject;
    JFrame frame;

    // existing projects list
    // Actions for each of the menu buttons.
    
    // Create new project
    private ActionListener newProjectAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // New game project dialog
            JFrame frame = new JFrame("Create Project");
            frame.setLocationRelativeTo(null);
            User testUser = new User("TestUser");
            Project project = new Project();
            // testUser.addProject(project);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(project);
            frame.setSize(project.getPreferredSize());
            frame.setVisible(true);
        }
    };

    // Load a saved project
    private ActionListener loadProjectAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open previous game frame
            frame = new JFrame("Load Project");
            frame.setLocationRelativeTo(null);
            frame.setSize(new Dimension(300, 300));
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Container contentPane = frame.getContentPane();
            final JLabel label = new JLabel("Projects");
            // locate relative JAR/class location for project storage
            String dirString = Project.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            // load the project names from the global projects file
            projectNames = new ArrayList<String>();
            try {
                File projFile = new File(dirString + "Projects.txt");
                BufferedReader br = new BufferedReader(new FileReader(projFile));
                String line;
                while ((line = br.readLine()) != null) {
                    projectNames.add(line);
                }
            } catch (IOException ioe) {
            }
            // populate selection list
            String[] projNamesArr = new String[projectNames.size()];
            projNamesArr = projectNames.toArray(projNamesArr);
            final JList dataList = new JList(projNamesArr);
            contentPane.add(dataList, BorderLayout.NORTH);
            final JButton loadButton = new JButton("Load Project (None Selected)");
            contentPane.add(loadButton, BorderLayout.SOUTH);
            
            // track selection
            dataList.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent arg0) {
                    if (!arg0.getValueIsAdjusting()) {
                        selectedProject = dataList.getSelectedValue().toString();
                        loadButton.setText("Load Project (" + selectedProject + ")");
                    }
                }
            });
            frame.setVisible(true);
            
            // open a project once a project is selected
            loadButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedProject == null) {
                        System.out.println("No project selected.");
                    } else {
                        // load selected project
                        if (MainUI.vprog != null)
                            MainUI.vprog.closeGame();
                        
                        MainUI.vprog = new VProgEngine(selectedProject);

                        LwjglApplicationConfiguration config
                                = new LwjglApplicationConfiguration();
                        config.title = selectedProject;
                        config.x = -1;
                        config.y = -1;
                        config.width = Math.min(MainUI.screenBounds.width, 800);
                        config.height = Math.min(MainUI.screenBounds.height, 600);
                        config.resizable = true;
                        config.allowSoftwareMode = true;
                        new LwjglApplication(MainUI.vprog, config);
                        frame.dispose();
                    }
                }
            });

        }
    };

    private ActionListener saveProjectAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // If libgdx's prefs.flush() returns a success or failure, we should
            // provide the user some 'save succesful'/'save failed' feedback.
            vprog.saveScene();
        }
    };

    private ActionListener constructAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // I'm thinking this is a mix of the Freeze action, and entering
            // into a different "mode" in the VProgEngine which will allow you
            // to edit elements in the game UI directly. (BIG MAYBE, LOTS OF
            // WORK TO DO.)
            vprog.freeze();
        }
    };

    private ActionListener runProgramAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            vprog.run();
        }
    };

    private ActionListener freezeProgramAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            vprog.freeze();
        }
    };

    private ActionListener helpAboutAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            AboutFrame aboutFrame = new AboutFrame();
        }
    };

    public MainUI()
    {
        screenBounds = WindowBoundsChecker.getWindowBounds();

        launchMainWindow(screenBounds);
    }

    private JMenuBar buildMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenuHelper fileMenu = new JMenuHelper("File");
        fileMenu.add("New", "createProject_small.png", newProjectAction);
        fileMenu.add("Open", "loadProject_small.png", loadProjectAction);
        fileMenu.add("Save", "saveProject_small.png", saveProjectAction);
        menuBar.add(fileMenu.jMenu());

        JMenuHelper projectMenu = new JMenuHelper("Project");
        projectMenu.add("Construct", "construct_small.png", constructAction);
        projectMenu.add("Run", "runProgram_small.png", runProgramAction);
        projectMenu.add("Freeze", "pauseProgram_small.png",
            freezeProgramAction);
        menuBar.add(projectMenu.jMenu());

        JMenuHelper helpMenu = new JMenuHelper("Help");
        helpMenu.add("About", helpAboutAction);
        menuBar.add(helpMenu.jMenu());

        return menuBar;
    }

    private void launchMainWindow(Rectangle bounds) {
        JMenuBar menuBar = buildMenuBar();

        JFrame wrapperFrame = new JFrame("VProg2D");

        Rectangle adjustedBounds = new Rectangle(bounds);
        adjustedBounds.width = Math.min(bounds.width, 800);
        adjustedBounds.height = Math.min(bounds.width, 480);
        wrapperFrame.setBounds(adjustedBounds);
        wrapperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container frameContainer = wrapperFrame.getContentPane();

        // BorderLayout is the default layout for Swing elements. Specifying the
        // "NORTH" constant forces the menu bar into the top-left, as typical.
        frameContainer.add(menuBar, BorderLayout.NORTH);

        JButton buttonUploadTexture
                = new JButton("Upload Texture", new ImageIcon("uploadAsset.png"));
        buttonUploadTexture.addActionListener(uploadTexture);
        frameContainer.add(buttonUploadTexture, BorderLayout.WEST);

        wrapperFrame.setVisible(true);
    }

    private ActionListener uploadTexture = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setVisible(true);
            fileChooser.setDialogTitle("Select Texture (.png) File");
            int result = fileChooser.showOpenDialog((Component) e.getSource());
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }
            java.io.File file = fileChooser.getSelectedFile();
            String filepath;
            try {
                filepath = file.getCanonicalPath();
            } catch (java.io.IOException ex) {
                ErrorFrame errorFrame = new ErrorFrame(ex);
                return;
            }
            vprog.queuedAssetChaperone.add(filepath, new Callback() {
                @Override
                public void call() {
                    System.out.println("LOADED OMFG!");
                }
            });
            vprog.loadTexture(filepath);
        }
    };
}
