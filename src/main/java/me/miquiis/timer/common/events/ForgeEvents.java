package me.miquiis.timer.common.events;

import me.miquiis.timer.Timer;
import me.miquiis.timer.common.managers.TimerManager;
import me.miquiis.timer.common.models.CountdownTimer;
import me.miquiis.timer.server.commands.TimerCommand;
import me.miquiis.timer.server.network.TimerNetwork;
import me.miquiis.timer.server.network.messages.SyncTimerMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Timer.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event)
    {
        new TimerCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event)
    {
        if (event.side == LogicalSide.CLIENT) return;
        if (event.phase != TickEvent.Phase.END) return;

        final TimerManager timerManager = Timer.getInstance().getTimerManager();

        for (Map.Entry<UUID, CountdownTimer> entry : timerManager.getTimers().entrySet()) {
            entry.getValue().tick();
            if (entry.getValue().getPercentage() >= 1f) {
                timerManager.getTimers().remove(entry.getKey());
            }
            if (entry.getValue().shouldSync()) {
                ServerPlayerEntity playerEntity = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(entry.getKey());
                if (playerEntity == null) return;
                TimerNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> playerEntity), new SyncTimerMessage(entry.getValue()));
                entry.getValue().serverSync();
            }
        }
    }

}
