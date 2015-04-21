package com.mygdx.game.desktop;

// This file is the central hub of the 'primary' or 'editor' UI.

// This is for grabbing available space for our window(s) on the screen.
import com.mygdx.game.WindowBoundsChecker;
import java.awt.Rectangle;

// This is for the editable-game engine.
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.VProgEngine;

// This is for the graphical user interface.
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class MainUI
{
    // This will hold the screen area available to us for our windows (we don't
    // want to open windows bigger than these bounds if we can avoid it).
    private Rectangle screenBounds;
    
    // The actual editable game engine.
    private VProgEngine vprog;
    
    private ActionListener newProjectAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            vprog = new VProgEngine();
            
            LwjglApplicationConfiguration config
                = new LwjglApplicationConfiguration();
            config.title = "VProg2D";
            config.x = screenBounds.x;
            config.y = screenBounds.y;
            config.width = Math.min(screenBounds.width, 1280);
            config.height = Math.min(screenBounds.height, 720);
            config.resizable = true;
            config.allowSoftwareMode = true;
            new LwjglApplication(vprog, config);
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
           vprog.save();
        }
    };
    
    private ActionListener constructAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
           System.out.print("Construct Action\n"); 
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
           System.out.print("Run Program Action\n"); 
        }
    };
    
    private ActionListener helpAboutAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
           System.out.print("Help About Action\n"); 
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
        menuBar.add(projectMenu.jMenu());
        
        JMenuHelper helpMenu = new JMenuHelper("Help");
        helpMenu.add("About", helpAboutAction);
        menuBar.add(helpMenu.jMenu());
        
        return menuBar;
    }
    
    private void launchMainWindow(Rectangle bounds)
    {
        JMenuBar menuBar = buildMenuBar();
        
        JFrame wrapperFrame = new JFrame("VProg2D Wrapper Frame");
        
        wrapperFrame.setBounds(bounds);
        wrapperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container frameContainer = wrapperFrame.getContentPane();
        
        // BorderLayout is the default layout for Swing elements. Specifying the
        // "NORTH" constant forces the menu bar into the top-left, as typical.
        frameContainer.add(menuBar, BorderLayout.NORTH);
        
        wrapperFrame.setVisible(true);
    }
}
