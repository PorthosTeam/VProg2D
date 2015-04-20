package com.mygdx.game;

// These modules are needed to ascertain available screen "real estate".
import java.awt.Rectangle;
import java.awt.Insets;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;

public final class WindowBoundsChecker {
    // Forcing the constructor to private because this is not meant to be
    // used as anything other than a utility class.
    private WindowBoundsChecker(){}
    
    public static Rectangle getWindowBounds() {
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
}
