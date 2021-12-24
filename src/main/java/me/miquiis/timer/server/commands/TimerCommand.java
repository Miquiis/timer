package me.miquiis.timer.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.miquiis.timer.Timer;
import me.miquiis.timer.common.managers.TimerManager;
import me.miquiis.timer.common.models.CountdownTimer;
import me.miquiis.timer.server.network.TimerNetwork;
import me.miquiis.timer.server.network.messages.StartTimerMessage;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntitySummonArgument;
import net.minecraft.command.arguments.SuggestionProviders;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimerCommand {
    public TimerCommand(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("timer").then(Commands.literal("start").then(Commands.argument("ticks", LongArgumentType.longArg()).executes(context -> {
            long ticks = LongArgumentType.getLong(context, "ticks");

            final TimerManager timerManager = Timer.getInstance().getTimerManager();

            context.getSource().asPlayer().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> {
                CountdownTimer timer = new CountdownTimer(ticks);
                timerManager.getTimers().put(serverPlayerEntity.getUniqueID(), timer);
                TimerNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayerEntity), new StartTimerMessage(timer));
            });

            return 1;
        })))
        );
    }

}
