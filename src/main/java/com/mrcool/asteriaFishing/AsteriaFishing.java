package com.mrcool.asteriaFishing;

import org.bukkit.plugin.java.JavaPlugin;

public class AsteriaFishing extends JavaPlugin {

    private static AsteriaFishing plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("AsteriaFishing enabled!");
        getServer().getPluginManager().registerEvents(new FishingListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("AsteriaFishing disabled!");
    }

    // needed for easy access to plugin.getLogger() in other files
    public static AsteriaFishing getPlugin() {
        return plugin;
    }
}
