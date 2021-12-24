package me.miquiis.timer.common.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.StringTextComponent;

public class RenderUtil {

    private static int HEIGHT_REFERENCE = 1080;

    public static void renderTextWithShadow(MatrixStack stack, FontRenderer fontRenderer, MainWindow window, String text, float xOffset, float yOffset, float scale, int color)
    {
        scale *= window.getHeight() / (float) HEIGHT_REFERENCE;
        scale /= window.getGuiScaleFactor();
        stack.push();
        stack.scale(scale, scale, scale);
        fontRenderer.drawTextWithShadow(stack, new StringTextComponent(text), ((window.getScaledWidth()) / 2f / scale) - (fontRenderer.getStringWidth(text) / 2f) + xOffset, ((window.getScaledHeight()) / 2f / scale) - (fontRenderer.FONT_HEIGHT / 2f) + yOffset, color);
        stack.pop();
    }

}
