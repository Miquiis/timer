package me.miquiis.timer.common.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.StringTextComponent;

public class EasyGUI {

    public enum VAnchor {
        LEFT,
        RIGHT
    }

    public enum HAnchor {
        TOP,
        BOTTOM
    }

    public static class Anchor {
        public HAnchor horizontalAnchor;
        public VAnchor verticalAnchor;

        public Anchor(HAnchor horizontalAnchor, VAnchor verticalAnchor)
        {
            this.horizontalAnchor = horizontalAnchor;
            this.verticalAnchor = verticalAnchor;
        }

        public Anchor(HAnchor horizontalAnchor)
        {
            this(horizontalAnchor, null);
        }

        public Anchor(VAnchor verticalAnchor)
        {
            this(null, verticalAnchor);
        }

    }

    public static void drawAnchoredText(Anchor anchor, MatrixStack stack, FontRenderer fontRenderer, MainWindow window, String text, float xOffset, float yOffset, float scale, int color)
    {
        scale *= window.getGuiScaleFactor() / 2;
        stack.push();
        stack.scale(scale, scale, scale);
        float horizontalAnchor = ((window.getScaledWidth()) / scale);
        float verticalAnchor = ((window.getScaledHeight() * 0) / scale);
        float horizontalSelf = -(fontRenderer.getStringWidth(text));
        float verticalSelf = 0;
        float xCalc = horizontalAnchor + horizontalSelf + xOffset;
        float yCalc = verticalAnchor + verticalSelf + yOffset;
        System.out.println("xCalc: " + xCalc);
        System.out.println("yCalc: " + yCalc);
        fontRenderer.drawTextWithShadow(stack, new StringTextComponent(text),
                xCalc,
                yCalc,
                color
        );
        stack.pop();
    }

    private static float getHorizontalAnchor(Anchor anchor, float width, float scale) {
        if (anchor.horizontalAnchor == null) return 0f;
        switch (anchor.horizontalAnchor)
        {
            case TOP:
            {
                return 0f;
            }
            case BOTTOM:
            {
                return 1f;
            }
            default:
            {
                return 2f;
            }
        }
    }

}
