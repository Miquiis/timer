package me.miquiis.timer.server.network;

import me.miquiis.timer.Timer;
import me.miquiis.timer.server.network.messages.StartTimerMessage;
import me.miquiis.timer.server.network.messages.SyncTimerMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class TimerNetwork {

    public static final String NETWORK_VERSION = "0.1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Timer.MOD_ID, "network"), () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION)
    );

    public static void init() {
        CHANNEL.registerMessage(0, StartTimerMessage.class, StartTimerMessage::encode, StartTimerMessage::decode, StartTimerMessage::handle);
        CHANNEL.registerMessage(1, SyncTimerMessage.class, SyncTimerMessage::encode, SyncTimerMessage::decode, SyncTimerMessage::handle);
    }

}
