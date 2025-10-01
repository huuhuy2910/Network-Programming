package client.util;

import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;

public final class UITheme {
    public static final Color PRIMARY = new Color(0x0A6CF7);
    public static final Color PRIMARY_DARK = new Color(0x0A3981);
    public static final Color ACCENT = new Color(0xFF8A34);
    public static final Color DANGER = new Color(0xD64545);
    public static final Color SUCCESS = new Color(0x2BA84A);
    public static final Color BACKGROUND = new Color(0xF4F7FB);
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color TEXT_PRIMARY = new Color(0x1F2933);
    public static final Color TEXT_SECONDARY = new Color(0x52606D);

    private UITheme() {
    }

    private static Font baseFont() {
        Font font = UIManager.getFont("Label.font");
        if (font == null) {
            font = new Font("SansSerif", Font.PLAIN, 14);
        }
        return font;
    }

    public static Font headerFont() {
        Font base = baseFont();
        return base.deriveFont(Font.BOLD, base.getSize2D() + 8f);
    }

    public static Font subHeaderFont() {
        Font base = baseFont();
        return base.deriveFont(Font.BOLD, base.getSize2D() + 4f);
    }

    public static Font bodyFont() {
        Font base = baseFont();
        return base.deriveFont(Font.PLAIN, base.getSize2D() + 2f);
    }

    public static Font smallFont() {
        Font base = baseFont();
        return base.deriveFont(Font.PLAIN, base.getSize2D());
    }

    public static Font buttonFont() {
        Font base = baseFont();
        return base.deriveFont(Font.BOLD, base.getSize2D() + 2f);
    }
}
