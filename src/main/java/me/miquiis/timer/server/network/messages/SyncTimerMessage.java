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

public class SyncTimerMessage {

    private long currentTicks;

    public SyncTimerMessage(CountdownTimer timer) {
        this.currentTicks = timer.getCurrentTicks();
    }

    public SyncTimerMessage(long currentTicks)
    {
        this.currentTicks = currentTicks;
    }

    public static void encode(SyncTimerMessage message, PacketBuffer buffer)
    {
        buffer.writeLong(message.currentTicks);
    }

    public static SyncTimerMessage decode(PacketBuffer buffer)
    {
        return new SyncTimerMessage(buffer.readLong());
    }

    public static void handle(SyncTimerMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> SyncTimerMessage.handlePacket(message, contextSupplier));
        });
        context.setPacketHandled(true);
    }

    public static void handlePacket(SyncTimerMessage msg, Supplier<NetworkEvent.Context> ctx) {
        TimerManager timerManager = Timer.getInstance().getTimerManager();
        if (timerManager.getCurrentTimer() == null) return;
        timerManager.getCurrentTimer().clientSync(msg.currentTicks);
        if (timerManager.getCurrentTimer().getPercentage() >= 1.0f) timerManager.getCurrentTimer().shutdown();

    }

}
