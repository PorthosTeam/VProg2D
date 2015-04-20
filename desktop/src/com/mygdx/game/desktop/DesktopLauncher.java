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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

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
        String labelText = bounds.x + " " + bounds.y + " " + bounds.width + " "
            + bounds.height;
        JLabel testJLabel = new JLabel(labelText);
        
        JFrame testJFrame = new JFrame("Test JFrame");
        testJFrame.setBounds(bounds);
        testJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        testJFrame.getContentPane().add(testJLabel);
        testJFrame.setVisible(true);
    }
    
    public static void main (String[] arg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startSwingGUI();
            }
        });
        LwjglApplicationConfiguration config
            = new LwjglApplicationConfiguration();
        config.title = "VProg2D";
        config.height = 720;
        config.width = 1280;
        config.resizable = false;
        //new LwjglApplication(new vprog(), config);
    }
}
