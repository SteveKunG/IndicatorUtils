/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.window;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class WindowGameXYZ extends JFrame
{
    public static JLabel label;

    public WindowGameXYZ()
    {
        this.setSize(260, 95);
        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e) {}
        });
        WindowGameXYZ.label = new JLabel("XYZ Startup");
        WindowGameXYZ.label.setFont(new Font("Dialog", Font.PLAIN, 16));
        this.add(WindowGameXYZ.label);
    }
}