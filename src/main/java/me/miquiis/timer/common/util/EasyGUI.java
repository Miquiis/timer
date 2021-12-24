package me.miquiis.timer.common.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.StringTextComponent;

public class EasyGUI {

    public enum HAnchor {
        LEFT,
        RIGHT,
        CENTER
    }

    public enum VAnchor {
        TOP,
        BOTTOM,
        CENTER
    }

    public static class Anchor {
        public HAnchor horizontalAnchor;
        public VAnchor verticalAnchor;

        public Anchor(VAnchor verticalAnchor, HAnchor horizontalAnchor)
        {
            this.horizontalAnchor = horizontalAnchor;
            this.verticalAnchor = verticalAnchor;
        }

        public Anchor(HAnchor horizontalAnchor)
        {
            this(null, horizontalAnchor);
        }

        public Anchor(VAnchor verticalAnchor)
        {
            this(verticalAnchor, null);
        }

    }

    public static void drawAnchoredText(Anchor anchor, MatrixStack stack, FontRenderer fontRenderer, MainWindow window, String text, float xOffset, float yOffset, float scale, int color)
    {
        scale /= window.getGuiScaleFactor();
        stack.push();
        stack.scale(scale, scale, scale);
        fontRenderer.drawTextWithShadow(stack, new StringTextComponent(text),
                getHorizontalAnchor(anchor, window.getScaledWidth(), scale) + getSelfHorizontalAnchor(anchor, fontRenderer.getStringWidth(text)) + getScaledOffset(xOffset, scale, window.getGuiScaleFactor()),
                getVerticalAnchor(anchor, window.getScaledHeight(), scale) + getSelfVerticalAnchor(anchor, fontRenderer.FONT_HEIGHT) + getScaledOffset(yOffset, scale, window.getGuiScaleFactor()),
                color
        );
        stack.pop();
    }

    private static float getHorizontalAnchor(Anchor anchor, float width, float scale) {
        if (anchor.horizontalAnchor == null) return 0f;
        if (anchor.horizontalAnchor == HAnchor.RIGHT) {
            return width / scale;
        } else if (anchor.horizontalAnchor == HAnchor.CENTER) {
            return width / 2 / scale;
        }
        return 0f;
    }

    private static float getVerticalAnchor(Anchor anchor, float height, float scale) {
        if (anchor.verticalAnchor == null) return 0f;
        if (anchor.verticalAnchor == VAnchor.BOTTOM) {
            return height / scale;
        } else if (anchor.verticalAnchor == VAnchor.CENTER) {
            return height / 2 / scale;
        }
        return 0f;
    }

    private static float getSelfHorizontalAnchor(Anchor anchor, float width) {
        if (anchor.horizontalAnchor == null) return 0f;
        if (anchor.horizontalAnchor == HAnchor.RIGHT) {
            return -width;
        } else if (anchor.horizontalAnchor == HAnchor.CENTER) {
            return -width / 2;
        }
        return 0f;
    }

    private static float getSelfVerticalAnchor(Anchor anchor, float height) {
        if (anchor.verticalAnchor == null) return 0f;
        if (anchor.verticalAnchor == VAnchor.BOTTOM) {
            return -height;
        } else if (anchor.verticalAnchor == VAnchor.CENTER) {
            return -height / 2;
        }
        return 0f;
    }

    private static float getScaledOffset(float offset, float scale, double guiScale)
    {
        return (float) (offset / scale / guiScale);
    }

    public static String color(String message)
    {
        return message.replace("&", "\u00a7");
    }

}
