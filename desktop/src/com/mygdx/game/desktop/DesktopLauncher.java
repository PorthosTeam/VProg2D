package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.vprog;
import com.mygdx.game.JMenuBuilder;

// This is for grabbing available space for our window(s) on the screen.
import com.mygdx.game.WindowBoundsChecker;
import java.awt.Rectangle;

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
        Rectangle screenBounds = WindowBoundsChecker.getWindowBounds();
        
        // This is currently just Alex testing his UI work. This will definitely
        // not be exactly how it's done later.
        openTestWindow(screenBounds);
        
        LwjglApplicationConfiguration config
            = new LwjglApplicationConfiguration();
        config.title = "VProg2D";
        config.x = screenBounds.x;
        config.y = screenBounds.y;
        config.width = Math.min(screenBounds.width, 1280);
        config.height = Math.min(screenBounds.height, 720);
        config.resizable = true;
        config.allowSoftwareMode = true;
        new LwjglApplication(new vprog(), config);
    }
    
    private static void openTestWindow(Rectangle bounds) {
        JMenuBar menuBar = new JMenuBar();
        
        menuBar.add(JMenuBuilder.buildFileMenu());
        menuBar.add(JMenuBuilder.buildProjectMenu());
        menuBar.add(JMenuBuilder.buildHelpMenu());
        
        JFrame wrapperFrame = new JFrame("VProg2D Wrapper Frame");
        
        wrapperFrame.setBounds(bounds);
        wrapperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container frameContainer = wrapperFrame.getContentPane();
        
        // BorderLayout is the default layout for Swing elements. Specifying the
        // "NORTH" constant forces the menu bar into the top-left, as typical.
        frameContainer.add(menuBar, BorderLayout.NORTH);
        
        wrapperFrame.setVisible(true);
    }

    public static void main (String[] arg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startSwingGUI();
            }
        });
    }
}
