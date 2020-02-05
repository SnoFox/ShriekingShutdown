package net.snofox.bungeecord.shriekingshutdown;

import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public final class ShriekingShutdown extends Plugin {

    private static ShriekingShutdown instance;
    public static ShriekingShutdown getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getProxy().getPluginManager().registerCommand(this, new ShutdownCommand("shutdown", "bungeecord.shutdown"));
        getLogger().info("Shutdowns have scary shrieking");
    }

    public void shutdown(final int seconds) {
        getProxy().getScheduler().schedule(this, new ShutdownTask(this, seconds), 1, 1, TimeUnit.SECONDS);
    }

}
