package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.vprog;

// These modules are needed to ascertain available screen "real estate".
import java.awt.Rectangle;
import java.awt.Insets;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;

// These modules are Alex experimenting with a UI. All of this UI work might be
// better off being moved into the Core, rather than Desktop, sub-project.
import java.awt.Container;
import java.awt.BorderLayout;
import javax.swing.*;

public class DesktopLauncher {
    // Swing is not thread safe, so it is "good practice" to move all Swing
    // commands out of main, into a function that is run by the event dispatcher
    // thread once it's actually safe.
    private static void startSwingGUI() {
        // Don't want any of this fixed-size stuff if we can avoid it, so we get
        // the screen area available to us for our windows (we don't want to
        // open windows bigger than these bounds if we can avoid it).
        Rectangle screenBounds = getWindowBounds();
        
        openTestWindow(screenBounds);
        
        LwjglApplicationConfiguration config
            = new LwjglApplicationConfiguration();
        config.title = "VProg2D";
        config.x = screenBounds.x;
        config.y = screenBounds.y;
        config.width = screenBounds.width;
        config.height = screenBounds.height;
        config.resizable = true;
        new LwjglApplication(new vprog(), config);
    }
    
    private static Rectangle getWindowBounds() {
        // First grab the overall screen graphics configuration...
        GraphicsConfiguration graphicsConf
            = GraphicsEnvironment.getLocalGraphicsEnvironment()
              .getDefaultScreenDevice().getDefaultConfiguration();
        
        // ..then by grabbing the 'real' screen bounds (e.g. physical)...
        Rectangle screenBounds = getScreenBounds(graphicsConf);
        
        // ..then getting the "insets"...
        Insets screenInsets = getScreenInsets(graphicsConf);
        
        // ..and finally calculating the effective screen bounds based on the
        // physical bounds, minus the insets.
        return calculateInsetBounds(screenBounds, screenInsets);
    }
    
    private static Rectangle getScreenBounds
    (GraphicsConfiguration graphicsConf) {
        return graphicsConf.getBounds();
    }
    
    private static Insets getScreenInsets(GraphicsConfiguration graphicsConf) {
        return Toolkit.getDefaultToolkit().getScreenInsets(graphicsConf);
    }
    
    private static Rectangle calculateInsetBounds
    (Rectangle bounds, Insets margin) {
        Rectangle insetBounds = new Rectangle();
        insetBounds.x = bounds.x + margin.left;
        insetBounds.y = bounds.y + margin.top;
        insetBounds.width = bounds.width - margin.left - margin.right;
        insetBounds.height = bounds.height - margin.top - margin.bottom;
        return insetBounds;
    }
    
    private static void openTestWindow(Rectangle bounds) {
        JMenuBar menuBar = new JMenuBar();
        
        menuBar.add(buildMenu_file());
        menuBar.add(buildMenu_project());
        menuBar.add(buildMenu_help());
        
        JFrame wrapperFrame = new JFrame("VProg2D Wrapper Frame");
        
        wrapperFrame.setBounds(bounds);
        // NOTE: This is only DISPOSE_ON_CLOSE while I'm testing. In the future,
        // it probably ought to be EXIT_ON_CLOSE (e.g. when, as I'm envisioning
        // it, the main app is embedded inside this UI's JFrame.)
        wrapperFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Container frameContainer = wrapperFrame.getContentPane();
        
        // BorderLayout is the default layout for Swing elements. Specifying the
        // "NORTH" constant forces the menu bar into the top-left, as typical.
        frameContainer.add(menuBar, BorderLayout.NORTH);
        
        wrapperFrame.setVisible(true);
    }
    
    private static JMenuItem newJMenuItemWithIcon
    (String name, String iconFilename) {
        ImageIcon icon = new ImageIcon(iconFilename);
        JMenuItem item = new JMenuItem(name);
        item.setIcon(icon);
        return item;
    }
    
    // These 'buildMenu_*' functions could/should probably be refactored into
    // some sort of less-programmatic way of generating the menus. But it's not
    // a priority right now.
    
    private static JMenu buildMenu_file() {
        JMenu menu = new JMenu("File");
        
        // "new_" is icky, but "new" is a reserved keyword.
        JMenuItem new_ = newJMenuItemWithIcon("New", "createProject_small.png");
        JMenuItem open = newJMenuItemWithIcon("Open", "loadProject_small.png");
        JMenuItem save = newJMenuItemWithIcon("Save", "saveProject_small.png");
        
        menu.add(new_);
        menu.add(open);
        menu.add(save);
        
        return menu;
    }
    
    private static JMenu buildMenu_project() {
        JMenu menu = new JMenu("Project");
        
        JMenuItem build = newJMenuItemWithIcon("Build", "construct_small.png");
        JMenuItem test = newJMenuItemWithIcon("Test", "test_small.png");
        JMenuItem run = newJMenuItemWithIcon("Run", "runProgram_small.png");
        
        menu.add(build);
        menu.add(test);
        menu.add(run);
        
        return menu;
    }
    
    private static JMenu buildMenu_help() {
        JMenu menu = new JMenu("Help");
        
        JMenuItem about = new JMenuItem("About");
        
        menu.add(about);
        
        return menu;
    }
    
    public static void main (String[] arg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startSwingGUI();
            }
        });
    }
}
