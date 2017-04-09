package stevekung.mods.indicatorutils.window;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class WindowGameXYZ extends JFrame
{
    public static JLabel label;

    public WindowGameXYZ()
    {
        this.setSize(260, 95);
        this.setLayout(new FlowLayout());
        WindowGameXYZ.label = new JLabel("XYZ Startup");
        WindowGameXYZ.label.setFont(new Font("Dialog", Font.PLAIN, 16));
        this.add(WindowGameXYZ.label);
    }
}