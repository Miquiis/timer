package me.miquiis.timer.common.managers;

import me.miquiis.timer.Timer;
import me.miquiis.timer.common.models.CountdownTimer;
import me.miquiis.timer.server.network.TimerNetwork;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerManager {

    private Timer mod;

    private CountdownTimer currentTimer;

    private Map<UUID, CountdownTimer> timers;

    public TimerManager(Timer mod)
    {
        this.mod = mod;
        timers = new HashMap<>();
    }

    public Map<UUID, CountdownTimer> getTimers() {
        return timers;
    }

    @OnlyIn(Dist.CLIENT)
    public void startTimer(CountdownTimer currentTimer)
    {
        this.currentTimer = currentTimer;
    }

    @OnlyIn(Dist.CLIENT)
    public void endTimer()
    {
        this.currentTimer = null;
    }

    @OnlyIn(Dist.CLIENT)
    public CountdownTimer getCurrentTimer() {
        return currentTimer;
    }
}
