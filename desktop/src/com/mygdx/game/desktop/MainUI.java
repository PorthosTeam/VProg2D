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
import java.awt.Dimension;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class MainUI
{
    // This will hold the screen area available to us for our windows (we don't
    // want to open windows bigger than these bounds if we can avoid it).
    private Rectangle screenBounds;
    
    // The actual editable game engine.
    private VProgEngine vprog;
    
    // Actions for each of the menu buttons.
    private ActionListener newProjectAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            String name = "blah";
            vprog = new VProgEngine(name);
            
            LwjglApplicationConfiguration config
                = new LwjglApplicationConfiguration();
            config.title = name;
            config.x = -1;
            config.y = -1;
            config.width = Math.min(screenBounds.width, 800);
            config.height = Math.min(screenBounds.height, 600);
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
           JFrame window = new JFrame("About VProg2D");
           
           Rectangle bounds = new Rectangle();
           bounds.width = Math.min(220, screenBounds.width);
           bounds.height = Math.min(300, screenBounds.height);
           bounds.x = (screenBounds.width - screenBounds.x) / 2
               + screenBounds.x - (bounds.width / 2);
           bounds.y = (screenBounds.height - screenBounds.y) / 2
               + screenBounds.y - (bounds.height / 2);
           window.setBounds(bounds);
           
           // For certain likely-to-be-used-often frames it would make sense to
           // open them once and set close operation to the do-nothing one, so
           // as to not go through the whole create/destroy cycle each time. But
           // this window is unlikely to be visited often enough to warrant
           // keeping it around in memory.
           window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           
           EmptyBorder margin = new EmptyBorder(10, 10, 10, 10);
           
           ImageIcon logoIcon = new ImageIcon("logo_small.png");
           JLabel logo = new JLabel(logoIcon);
           logo.setBorder(margin);
           
           String text = "<html>"
               + "VProg2D is a visual game editor.<br/>"
               + " It allows for flexibly creating simple games.<br/>"
               + " Also this text needs improving!</html>";
           JLabel label = new JLabel(text);
           label.setBorder(margin);
           // Need this to ensure proper word-wrapping.
           label.setSize(new Dimension(bounds.width, bounds.height));
           
           window.add(logo, BorderLayout.NORTH);
           window.add(label, BorderLayout.CENTER);
           
           window.setVisible(true);
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

        wrapperFrame.setVisible(true);
    }
}
