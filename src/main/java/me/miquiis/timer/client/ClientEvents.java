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

    private static Color redColor = new Color(217, 67, 67);
    private static Color greenColor = new Color(67, 217, 107);

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
        if (timerManager.getCurrentTimer() == null) return;
        if (timerManager.getCurrentTimer().canShutdown())
        {
            timerManager.endTimer();
            return;
        }
        EasyGUI.drawAnchoredText(new EasyGUI.Anchor(EasyGUI.VAnchor.BOTTOM, EasyGUI.HAnchor.RIGHT), event.getMatrixStack(), fontRenderer, event.getWindow(), timerManager.getCurrentTimer().getFormattedTime(), 0, 0, 3, shiftColors(greenColor, redColor, timerManager.getCurrentTimer().getPercentage()).getRGB());
    }

    private static Color shiftColors(Color one, Color two, float percentage)
    {
        return new Color(getShift(one.getRed(), two.getRed(), percentage), getShift(one.getGreen(), two.getGreen(), percentage), getShift(one.getBlue(), two.getBlue(), percentage));
    }

    private static int getShift(int from, int to, float percentage)
    {
        return (int) ((to - from) * percentage + from);
    }

}
