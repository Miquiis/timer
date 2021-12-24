package me.miquiis.timer.client;

import me.miquiis.timer.Timer;
import me.miquiis.timer.common.util.EasyGUI;
import me.miquiis.timer.common.util.RenderUtil;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Timer.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    private static String text = "Test Text";
    private static int color = 0xFFFFFF;
    private static FontRenderer fontRenderer;

    private static void verifyRenderer() {
        if (fontRenderer != null) return;
        fontRenderer = Minecraft.getInstance().fontRenderer;
    }

    @SubscribeEvent
    public static void render(RenderGameOverlayEvent.Text event)
    {
        verifyRenderer();
        EasyGUI.drawAnchoredText(null, event.getMatrixStack(), fontRenderer, event.getWindow(), "Test", 0, 0, 2, color);
    }

}
