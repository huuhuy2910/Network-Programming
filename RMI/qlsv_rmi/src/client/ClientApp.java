package client;

import client.ui.LoginFrame;
import client.util.FlatLafSetup;

import javax.swing.SwingUtilities;

public class ClientApp {
    public static void main(String[] args) {
        FlatLafSetup.install();
        SwingUtilities.invokeLater(LoginFrame::showLogin);
    }
}
