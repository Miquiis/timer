package me.miquiis.timer.client;

import me.miquiis.timer.Timer;
import me.miquiis.timer.common.managers.TimerManager;
import me.miquiis.timer.common.util.EasyGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = Timer.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    private static Color redColor = new Color(255, 80, 80);
    private static Color yellowColor = new Color(255, 246, 80);
    private static Color orangeColor = new Color(255, 176, 80);
    private static Color blinkColor = new Color(144, 45, 45);
    private static Color greenColor = new Color(74, 255, 125);

    private static FontRenderer fontRenderer;
    private static TimerManager timerManager;

    private static void verifyRenderer() {
        if (fontRenderer != null) return;
        fontRenderer = Minecraft.getInstance().fontRenderer;
    }

    private static void verifyTimerManager() {
        if (timerManager != null) return;
        timerManager = Timer.getInstance().getTimerManager();
    }

    @SubscribeEvent
    public static void render(RenderGameOverlayEvent.Text event)
    {
        verifyRenderer();
        verifyTimerManager();
        //debugRender(event);
        //renderTimer(event);
        //debugRenderBox(event);
        //debugRenderText(event);
    }

    private static void debugRenderText(RenderGameOverlayEvent.Text event) {
        EasyGUI.StringGUIElement stringGUIElement = new EasyGUI.StringGUIElement(new EasyGUI.Anchor(EasyGUI.VAnchor.TOP, EasyGUI.HAnchor.CENTER), event.getMatrixStack(), event.getWindow(), fontRenderer, "\u00a7bHello World", true, 0f, 0f, 1f, true, greenColor.getRGB());
        stringGUIElement.render(null);
    }

//    private static void renderTimer(RenderGameOverlayEvent.Text event) {
//        if (timerManager.getCurrentTimer() == null) return;
//        if (timerManager.getCurrentTimer().canShutdown())
//        {
//            timerManager.endTimer();
//            return;
//        }
//        int color = shiftColors(timerManager.getCurrentTimer().getPercentage()).getRGB();
//        if (timerManager.getCurrentTimer().shouldBlink())
//        {
//            color = blinkColor.getRGB();
//        }
//        EasyGUI.drawAnchoredText(new EasyGUI.Anchor(EasyGUI.VAnchor.TOP, EasyGUI.HAnchor.RIGHT), event.getMatrixStack(), fontRenderer, event.getWindow(), timerManager.getCurrentTimer().getFormattedTime(), 25, 25, 5, color);
//    }

//    private static void debugRender(RenderGameOverlayEvent.Text event)
//    {
//        EasyGUI.drawAnchoredText(new EasyGUI.Anchor(EasyGUI.VAnchor.TOP, EasyGUI.HAnchor.LEFT), event.getMatrixStack(), fontRenderer, event.getWindow(), "99:99:99", 50, 50, 5, shiftColors(0.0f).getRGB());
//    }

//    private static void debugRenderBox(RenderGameOverlayEvent.Text event)
//    {
//        EasyGUI.drawAnchoredBox(new EasyGUI.Anchor(EasyGUI.VAnchor.TOP, EasyGUI.HAnchor.RIGHT), event.getMatrixStack(), event.getWindow(), 50, 50, 0,0,1, shiftColors(0.0f).getRGB());
//    }

    private static void debugRenderBox(RenderGameOverlayEvent.Text event)
    {
        EasyGUI.BoxGUIElement boxGUIElement = new EasyGUI.BoxGUIElement(new EasyGUI.Anchor(EasyGUI.VAnchor.TOP, EasyGUI.HAnchor.LEFT), event.getMatrixStack(), event.getWindow(), 0, 0, 20, 20, 1f, false, greenColor.getRGB());

        EasyGUI.BoxGUIElement insideBox = new EasyGUI.BoxGUIElement(boxGUIElement, new EasyGUI.Anchor(EasyGUI.VAnchor.TOP, EasyGUI.HAnchor.LEFT), 0, 0, 5, 5, 1f, false, redColor.getRGB());

        boxGUIElement.assignChildren(insideBox).render(null);
    }

    private static Color shiftColors(float percentage)
    {
        if (percentage <= 0.5f)
        {
            return greenColor;
        } else if (percentage <= 0.75f)
        {
            return yellowColor;
        } else if (percentage < 1f)
        {
            return orangeColor;
        } else return redColor;
    }

    private static int getShift(int from, int to, float percentage)
    {
        return (int) ((to - from) * percentage + from);
    }

}
