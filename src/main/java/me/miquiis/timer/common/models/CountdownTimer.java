package me.miquiis.timer.common.models;

public class CountdownTimer {

    private long targetTicks;
    private long currentTicks;
    private long lastSync;

    private long waitForShutdown;

    public CountdownTimer(long targetTicks)
    {
        this.targetTicks = targetTicks;
        this.currentTicks = 0L;
        this.lastSync = 0L;
    }

    public void tick()
    {
        currentTicks++;
    }

    public void clientSync(long currentTicks)
    {
        this.currentTicks = currentTicks;
    }

    public void serverSync()
    {
        lastSync = currentTicks;
    }

    public void shutdown()
    {
        this.waitForShutdown = System.currentTimeMillis();
    }

    public boolean shouldSync()
    {
        return currentTicks >= lastSync + 20L;
    }

    public boolean canShutdown()
    {
        return waitForShutdown > 0 & System.currentTimeMillis() >= waitForShutdown + 3000;
    }

    public boolean shouldBlink()
    {
        return waitForShutdown > 0 & ((System.currentTimeMillis() - waitForShutdown) / 500) % 2 != 0;
    }

    public String getFormattedTime()
    {
        return String.format("%02d", getHours()) + ":" + String.format("%02d", getMinutes()) + ":" + String.format("%02d", getSeconds());
    }

    public float getPercentage()
    {
        return (float) currentTicks / targetTicks;
    }

    public long getTimeLeft()
    {
        return targetTicks - currentTicks;
    }

    public int getSeconds()
    {
        return (int) (getTimeLeft() / 20) % 60;
    }

    public int getMinutes()
    {
        return (int) (getTimeLeft() / 20) / 60;
    }

    public int getHours()
    {
        return (int) (getTimeLeft() / 20) / 3600;
    }

    public long getCurrentTicks() {
        return currentTicks;
    }

    public long getTargetTicks() {
        return targetTicks;
    }

    public long getWaitForShutdown() {
        return waitForShutdown;
    }
}
