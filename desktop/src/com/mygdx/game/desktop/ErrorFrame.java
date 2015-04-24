package com.mygdx.game.desktop;

import com.mygdx.game.WindowBoundsChecker;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

public class ErrorFrame extends JFrame
{
    public ErrorFrame(Exception exception)
    {
        super("VProg2D Error!");
        
        Rectangle bounds = WindowBoundsChecker.getCenteredBounds(220, 300);
        setBounds(bounds);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        EmptyBorder margin = new EmptyBorder(10, 10, 10, 10);
        
        ImageIcon logoIcon = new ImageIcon("logo_small.png");
        JLabel logo = new JLabel(logoIcon);
        logo.setBorder(margin);
        
        JLabel label = new JLabel(exception.getLocalizedMessage());
        label.setBorder(margin);
        // Need this to ensure proper word-wrapping.
        label.setSize(new Dimension(bounds.width, bounds.height));
        
        // TODO: Also provide a stack trace inside a collapsable (and collapsed
        // by default) extra text box.
        // Possibly syntactically messed up but generally right code to do it:
        // StackTraceElement[] traceSteps = exception.getStackTrace();
        // String traceString = "Stack Trace:\n";
        // for(int i = 0, len = traceSteps.length; i < len; i += 1)
        // {
        //     traceString += traceSteps[i].toString();
        // }

        add(label, BorderLayout.CENTER);
        setVisible(true);
    }
}
