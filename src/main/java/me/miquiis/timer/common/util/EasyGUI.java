package me.miquiis.timer.common.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static class GUIElement {
        public MatrixStack elementMatrix;
        public MainWindow elementWindow;

        public Anchor elementAnchor;

        public float elementFixedPosX;
        public float elementFixedPosY;

        public int elementWidth;
        public int elementHeight;
        public float elementPosX;
        public float elementPosY;
        public float elementScale;
        public int elementColor;

        public List<GUIElement> elementChildren;

        public GUIElement(Anchor elementAnchor, MatrixStack elementMatrix, MainWindow elementWindow, float elementPosX, float elementPosY, int elementWidth, int elementHeight, float elementScale, int elementColor)
        {
            this.elementMatrix = elementMatrix;
            this.elementWindow = elementWindow;
            this.elementAnchor = elementAnchor;
            this.elementPosX = elementPosX;
            this.elementPosY = elementPosY;
            this.elementWidth = elementWidth;
            this.elementHeight = elementHeight;
            this.elementScale = elementScale;
            this.elementColor = elementColor;
            this.elementChildren = new ArrayList<>();
        }

        public GUIElement(GUIElement parent, Anchor elementAnchor, float elementPosX, float elementPosY, int elementWidth, int elementHeight, float elementScale, int elementColor)
        {
            this(elementAnchor, parent.elementMatrix, parent.elementWindow, elementPosX, elementPosY, elementWidth, elementHeight, elementScale, elementColor);
        }

        public void render(@Nullable GUIElement parent)
        {
            if (parent != null)
            {
                float anchorPointX = parent.elementScale * parent.elementFixedPosX;
                float anchorPointY = parent.elementScale * parent.elementFixedPosY;

                elementFixedPosX = getHorizontalAnchor(elementAnchor, anchorPointX, parent.elementWidth, elementScale) + getSelfHorizontalAnchor(elementAnchor, elementWidth);
                elementFixedPosY = getVerticalAnchor(elementAnchor, anchorPointY, parent.elementHeight, elementScale) + getSelfVerticalAnchor(elementAnchor, elementHeight) + getScaledOffset(elementAnchor.verticalAnchor, elementPosY, elementScale, elementWindow.getGuiScaleFactor());

                elementMatrix.push();
                elementMatrix.translate(elementFixedPosX, elementFixedPosY,0);
                selfRender();
                //this.elementChildren.forEach(guiElement -> guiElement.render(this));
            } else {
                elementScale /= elementWindow.getGuiScaleFactor();
                elementMatrix.push();
                elementMatrix.scale(elementScale, elementScale, elementScale);

                elementFixedPosX = getHorizontalAnchor(elementAnchor, elementWindow.getScaledWidth(), elementScale) + getSelfHorizontalAnchor(elementAnchor, elementWidth) + getScaledOffset(elementAnchor.horizontalAnchor, elementPosX, elementScale, elementWindow.getGuiScaleFactor());
                elementFixedPosY = getVerticalAnchor(elementAnchor, elementWindow.getScaledHeight(), elementScale) + getSelfVerticalAnchor(elementAnchor, elementHeight) + getScaledOffset(elementAnchor.verticalAnchor, elementPosY, elementScale, elementWindow.getGuiScaleFactor());

                elementMatrix.translate(elementFixedPosX,elementFixedPosY,0);

                selfRender();

                this.elementChildren.forEach(guiElement -> guiElement.render(this));

                elementMatrix.push();
                elementMatrix.translate(elementFixedPosX * elementScale,elementFixedPosY * elementScale,0);
                AbstractGui.fill(elementMatrix, 0, 0, 2, 2, Color.CYAN.getRGB());
                elementMatrix.pop();
            }
        }

        protected void selfRender()
        {
            elementMatrix.pop();
        }

        public GUIElement assignChildren(GUIElement guiElement)
        {
            this.elementChildren.add(guiElement);
            return this;
        }

        public GUIElement assignChildren(GUIElement... guiElements)
        {
            this.elementChildren.addAll(new ArrayList<>(Arrays.asList(guiElements)));
            return this;
        }

    }

    public static class BoxGUIElement extends GUIElement {

        public BoxGUIElement(Anchor elementAnchor, MatrixStack elementMatrix, MainWindow elementWindow, float elementPosX, float elementPosY, int elementWidth, int elementHeight, float elementScale, int elementColor) {
            super(elementAnchor, elementMatrix, elementWindow, elementPosX, elementPosY, elementWidth, elementHeight, elementScale, elementColor);
        }

        public BoxGUIElement(GUIElement parent, Anchor elementAnchor, float elementPosX, float elementPosY, int elementWidth, int elementHeight, float elementScale, int elementColor) {
            super(parent, elementAnchor, elementPosX, elementPosY, elementWidth, elementHeight, elementScale, elementColor);
        }

        @Override
        protected void selfRender() {
            AbstractGui.fill(elementMatrix, 0, 0, elementWidth, elementHeight, elementColor);
            super.selfRender();
        }
    }

//    public static void drawAnchoredText(Anchor anchor, MatrixStack stack, FontRenderer fontRenderer, MainWindow window, String text, float xOffset, float yOffset, float scale, int color)
//    {
//        scale /= window.getGuiScaleFactor();
//        stack.push();
//        stack.scale(scale, scale, scale);
//
//        float baseX = getHorizontalAnchor(anchor, window.getScaledWidth(), scale) + getSelfHorizontalAnchor(anchor, fontRenderer.getStringWidth(text)) + getScaledOffset(anchor.horizontalAnchor, xOffset, scale, window.getGuiScaleFactor());
//        float baseY = getVerticalAnchor(anchor, window.getScaledHeight(), scale) + getSelfVerticalAnchor(anchor, fontRenderer.FONT_HEIGHT) + getScaledOffset(anchor.verticalAnchor, yOffset, scale, window.getGuiScaleFactor());
//
//        stack.translate(baseX, baseY, 0);
//
//        fontRenderer.drawTextWithShadow(stack, new StringTextComponent(text), 0, 0, color);
//        stack.pop();
//    }

//    public static void drawAnchoredBox(Anchor anchor, MatrixStack stack, MainWindow window, int width, int height, int xOffset, int yOffset, int scale, int color)
//    {
//        scale /= window.getGuiScaleFactor();
//        stack.push();
//        stack.scale(scale, scale, scale);
//
//        int baseX = (int)getHorizontalAnchor(anchor, window.getScaledWidth(), scale) + (int)getSelfHorizontalAnchor(anchor, width) + (int)getScaledOffset(xOffset, scale, window.getGuiScaleFactor());
//        int baseY = (int)getVerticalAnchor(anchor, window.getScaledHeight(), scale) + (int)getSelfVerticalAnchor(anchor, height) + (int)getScaledOffset(yOffset, scale, window.getGuiScaleFactor());
//
//        AbstractGui.fill(stack,
//                baseX,
//                baseY,
//                baseX + width,
//                baseY + height,
//                color);
//        stack.pop();
//    }

//        public static void drawAnchoredBox(Anchor anchor, MatrixStack stack, MainWindow window, int width, int height, int xOffset, int yOffset, float scale, int color)
//    {
//        scale /= window.getGuiScaleFactor();
//        stack.push();
//        stack.scale(scale, scale, scale);
//
//        float baseX = getHorizontalAnchor(anchor, window.getScaledWidth(), scale) + getSelfHorizontalAnchor(anchor, width) + getScaledOffset(anchor.horizontalAnchor, xOffset, scale, window.getGuiScaleFactor());
//        float baseY = getVerticalAnchor(anchor, window.getScaledHeight(), scale) + getSelfVerticalAnchor(anchor, height) + getScaledOffset(anchor.verticalAnchor, yOffset, scale, window.getGuiScaleFactor());
//
//        stack.translate(baseX,baseY,0);
//
//        AbstractGui.fill(stack,
//                0,
//                0,
//                width,
//                height,
//                color);
//        stack.pop();
//    }

    private static float getHorizontalAnchor(Anchor anchor, float width, float scale) {
        if (anchor.horizontalAnchor == null) return 0f;
        if (anchor.horizontalAnchor == HAnchor.RIGHT) {
            return width / scale;
        } else if (anchor.horizontalAnchor == HAnchor.CENTER) {
            return width / 2 / scale;
        }
        return 0f;
    }

    private static float getHorizontalAnchor(Anchor anchor, float startingPoint, float width, float scale) {
        if (anchor.horizontalAnchor == null) return startingPoint;
        width += startingPoint;
        if (anchor.horizontalAnchor == HAnchor.RIGHT) {
            return width / scale;
        } else if (anchor.horizontalAnchor == HAnchor.CENTER) {
            return width / 2;
        }
        return startingPoint;
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

    private static float getVerticalAnchor(Anchor anchor, float startingPoint, float height, float scale) {
        if (anchor.verticalAnchor == null) return startingPoint;
        if (anchor.verticalAnchor == VAnchor.BOTTOM) {
            return height / scale;
        } else if (anchor.verticalAnchor == VAnchor.CENTER) {
            return height / 2 / scale;
        }
        return startingPoint;
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

    private static float getScaledOffset(HAnchor anchor, float offset, float scale, double guiScale)
    {
        return (float) ((anchor == HAnchor.RIGHT ? -offset : offset) / scale / guiScale);
    }

    private static float getScaledOffset(VAnchor anchor, float offset, float scale, double guiScale)
    {
        return (float) ((anchor == VAnchor.BOTTOM ? -offset : offset) / scale / guiScale);
    }

    public static String color(String message)
    {
        return message.replace("&", "\u00a7");
    }

}
