package com.mygdx.game.desktop;

// This file is the central hub of the 'primary' or 'editor' UI.

// This is for grabbing available space for our window(s) on the screen.
import com.badlogic.gdx.Gdx;
import com.mygdx.game.WindowBoundsChecker;
import java.awt.Rectangle;

// This is for the editable-game engine.
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.VProgEngine;

// This is for the graphical user interface.
import java.awt.Component;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

class MainUI
{
    // This will hold the screen area available to us for our windows (we don't
    // want to open windows bigger than these bounds if we can avoid it).
    public static Rectangle screenBounds;
    
    // The actual editable game engine.
    public static VProgEngine vprog;
    
    public Project newProject;
    private boolean creating = false;
    
    // Actions for each of the menu buttons.
    private ActionListener newProjectAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            // Debug stuff
            JFrame frame = new JFrame("Create Project");
            frame.setLocationRelativeTo(null);
            User testUser = new User("TestUser");
            Project project = new Project();
            // testUser.addProject(project);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.getContentPane().add(project);
            frame.setSize(project.getPreferredSize());
            frame.setVisible(true);
        }
    };
    
    private ActionListener loadProjectAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
           System.out.print("Load Project Action\n"); 
        }
    };
    
    private ActionListener saveProjectAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
           // If libgdx's prefs.flush() returns a success or failure, we should
           // provide the user some 'save succesful'/'save failed' feedback.
           vprog.saveEnginePrefs();
        }
    };
    
    private ActionListener constructAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            // I'm thinking this is a mix of the Freeze action, and entering
            // into a different "mode" in the VProgEngine which will allow you
            // to edit elements in the game UI directly. (BIG MAYBE, LOTS OF
            // WORK TO DO.)
            vprog.freeze();
        }
    };
    
    private ActionListener testAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
           System.out.print("Test Action\n"); 
        }
    };
    
    private ActionListener runProgramAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            vprog.run();
        }
    };
    
    private ActionListener freezeProgramAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            vprog.freeze();
        }
    };
    
    private ActionListener helpAboutAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            AboutFrame aboutFrame = new AboutFrame();
        }
    };
    
    // This class pretty much just launches the UI from the constructor.
    // Prob'ly not the "right"/"proper" way to do it, but it works fine: If we
    // ever need to do a lot of custom initialization before launching it,
    // refactoring it will make sense, but for now this works fine.
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
        projectMenu.add("Test", "test_small.png", testAction);
        projectMenu.add("Run", "runProgram_small.png", runProgramAction);
        projectMenu.add("Freeze", "runProgram_small.png", freezeProgramAction);
        menuBar.add(projectMenu.jMenu());
        
        JMenuHelper helpMenu = new JMenuHelper("Help");
        helpMenu.add("About", helpAboutAction);
        menuBar.add(helpMenu.jMenu());
        
        return menuBar;
    }
    
    private void launchMainWindow(Rectangle bounds)
    {
        JMenuBar menuBar = buildMenuBar();
        
        JFrame wrapperFrame = new JFrame("VProg2D");
        
        wrapperFrame.setBounds(bounds);
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
    
    private ActionListener uploadTexture = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setVisible(true);
            fileChooser.setDialogTitle("Select Texture (.png) File");
            int result = fileChooser.showOpenDialog((Component )e.getSource());
            if(result != JFileChooser.APPROVE_OPTION)
            {
                return;
            }
            java.io.File file = fileChooser.getSelectedFile();
            try
            {
                vprog.loadTexture(file.getCanonicalPath());
            }
            catch(java.io.IOException ex)
            {
                ErrorFrame errorFrame = new ErrorFrame(ex);
            }
        }
    };
}
