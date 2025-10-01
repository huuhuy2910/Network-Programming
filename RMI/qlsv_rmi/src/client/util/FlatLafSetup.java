package client.util;

import javax.swing.UIManager;
import java.lang.reflect.Method;

public final class FlatLafSetup {
    private FlatLafSetup() {
    }

    public static void install() {
        try {
            Class<?> flatLight = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            Method setup = flatLight.getMethod("setup");
            setup.invoke(null);
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                // ignore
            }
        }
    }
}
