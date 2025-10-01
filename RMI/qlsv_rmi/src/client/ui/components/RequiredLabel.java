package client.ui.components;

import javax.swing.JLabel;
import java.awt.Color;

public class RequiredLabel extends JLabel {
    public RequiredLabel(String text) {
        super(text + " *");
        setForeground(new Color(0x0A3981));
    }
}
