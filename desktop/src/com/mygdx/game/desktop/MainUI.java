package com.mygdx.game.desktop;

// This file is the central hub of the 'primary' or 'editor' UI.
// This is for grabbing available space for our window(s) on the screen.
import com.mygdx.game.WindowBoundsChecker;
import java.awt.Rectangle;

// This is for the editable-game engine.
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.VProgEngine;
import com.mygdx.game.Enemy;

// This is for the graphical user interface.
import java.util.ArrayList;
import java.awt.Component;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Image;
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
import javax.swing.border.LineBorder;
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

    private JPanel texturesUploadedPanel;
    private JPanel objectsAddPanel2;

    private String selectedObjectType;
    public int enemyCount;
    public int bgCount = 0;

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

                        // clear objects panel
                        System.out.println("remove");
                        objectsAddPanel2.removeAll();
                        redrawMainUI();
                        // reload if application window already open
                        if (MainUI.vprog != null) {
                            MainUI.vprog.reloadApp(selectedProject);
                        } else {
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
                        }
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

    public MainUI() {
        screenBounds = WindowBoundsChecker.getWindowBounds();

        launchMainWindow(screenBounds);
    }

    private JMenuBar buildMenuBar() {
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

        JPanel imageUploadPanel = new JPanel();
        imageUploadPanel.setLayout(
                new BoxLayout(imageUploadPanel, BoxLayout.PAGE_AXIS)
        );
        imageUploadPanel.setPreferredSize(new Dimension(230, 0));

        JButton imageUploadButton
                = new JButton("Upload Texture", new ImageIcon("uploadAsset.png"));
        imageUploadButton.addActionListener(uploadTexture);
        imageUploadButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        texturesUploadedPanel = new JPanel();
        texturesUploadedPanel.setLayout(
                new BoxLayout(texturesUploadedPanel, BoxLayout.PAGE_AXIS)
        );
        texturesUploadedPanel.setMaximumSize(new Dimension(220, 0));
        texturesUploadedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane imagesScrollPane = new JScrollPane(texturesUploadedPanel);
        imagesScrollPane.setPreferredSize(new Dimension(220, 0));
        imagesScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        imagesScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        imagesScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        imageUploadPanel.add(imageUploadButton);
        imageUploadPanel.add(imagesScrollPane);

        frameContainer.add(imageUploadPanel, BorderLayout.WEST);

        JPanel objectsAddPanel = new JPanel();
        objectsAddPanel.setLayout(new BoxLayout(objectsAddPanel, BoxLayout.PAGE_AXIS)
        );
        objectsAddPanel.setPreferredSize(new Dimension(230, 0));

        JButton objectsAddButton
                = new JButton("Add New Object", new ImageIcon("uploadAsset.png"));
        objectsAddButton.addActionListener(addObject);
        objectsAddButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        objectsAddPanel2 = new JPanel();
        objectsAddPanel2.setLayout(new BoxLayout(objectsAddPanel2, BoxLayout.PAGE_AXIS)
        );
        objectsAddPanel2.setMaximumSize(new Dimension(220, 0));
        objectsAddPanel2.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JScrollPane objectsScrollPane = new JScrollPane(objectsAddPanel2);
        objectsScrollPane.setPreferredSize(new Dimension(220, 0));
        objectsScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        objectsScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        objectsScrollPane.setAlignmentX(Component.RIGHT_ALIGNMENT);

        objectsAddPanel.add(objectsAddButton);
        objectsAddPanel.add(objectsScrollPane);

        frameContainer.add(objectsAddPanel, BorderLayout.EAST);

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
            TextureAssetPanel t = new TextureAssetPanel(filepath);
            texturesUploadedPanel.add(t);
            redrawMainUI();
            t.parentPanel = texturesUploadedPanel;
            vprog.queuedAssetChaperone.add(filepath, t);
            vprog.loadTexture(filepath);
        }
    };

    public void redrawMainUI() {
        texturesUploadedPanel.updateUI();
        objectsAddPanel2.updateUI();
    }

    private ActionListener addObject = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (MainUI.vprog == null) {
                JOptionPane.showMessageDialog(null,
                        "You must open a project to add objects to a scene.", "No Project Open", 0);
            } else {

                // Open previous game frame
                frame = new JFrame("Create Object");
                frame.setLocationRelativeTo(null);
                frame.setSize(new Dimension(300, 200));
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                Container contentPane = frame.getContentPane();
                final JLabel label = new JLabel("Object Type");
                String[] objectTypes = {"Player", "Enemy", "Background", "Sound"};
                final JList dataList = new JList(objectTypes);
                contentPane.add(dataList, BorderLayout.NORTH);
                final JButton loadButton = new JButton("Create Object (None Selected)");
                contentPane.add(loadButton, BorderLayout.SOUTH);

                // track selection
                dataList.addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent arg0) {
                        if (!arg0.getValueIsAdjusting()) {
                            selectedObjectType = dataList.getSelectedValue().toString();
                            loadButton.setText("Create " + selectedObjectType);
                        }
                    }
                });
                frame.setVisible(true);

                // open a project once a project is selected
                loadButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (selectedObjectType == null) {
                            System.out.println("No object type selected.");
                        } else {
                            // add selected
                            if (selectedObjectType.equals("Enemy")) {
                                int eIndex = vprog.addEnemy(0, 300, vprog.ground, 0, 0, 0, 150);
                                ObjectAssetPanel o = new ObjectAssetPanel(selectedObjectType + String.valueOf(eIndex));
                                objectsAddPanel2.add(o);
                                redrawMainUI();
                                frame.dispose();
                            }
                        }
                    }
                });
            }
        }
    };
}
