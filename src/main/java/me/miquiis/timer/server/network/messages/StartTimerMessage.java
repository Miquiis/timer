package me.miquiis.timer.server.network.messages;

import me.miquiis.timer.Timer;
import me.miquiis.timer.common.managers.TimerManager;
import me.miquiis.timer.common.models.CountdownTimer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StartTimerMessage {

    private CountdownTimer timer;

    public StartTimerMessage(CountdownTimer timer) {
        this.timer = timer;
    }

    public static void encode(StartTimerMessage message, PacketBuffer buffer)
    {
        buffer.writeLong(message.timer.getTargetTicks());
    }

    public static StartTimerMessage decode(PacketBuffer buffer)
    {
        return new StartTimerMessage(new CountdownTimer(buffer.readLong()));
    }

    public static void handle(StartTimerMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> StartTimerMessage.handlePacket(message, contextSupplier));
        });
        context.setPacketHandled(true);
    }

    public static void handlePacket(StartTimerMessage msg, Supplier<NetworkEvent.Context> ctx) {
        TimerManager timerManager = Timer.getInstance().getTimerManager();
        timerManager.startTimer(msg.timer);
    }


}
