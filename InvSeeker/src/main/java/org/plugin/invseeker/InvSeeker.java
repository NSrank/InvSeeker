package org.plugin.invseeker;

import org.plugin.invseeker.listeners.InventoryClickListener;
import org.plugin.invseeker.command.InvSeeCommand;
import org.plugin.invseeker.command.EnderSeeCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class InvSeeker extends JavaPlugin {
    private static InvSeeker instance;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getCommand("invsee").setExecutor(new InvSeeCommand(this));
        getCommand("endersee").setExecutor(new EnderSeeCommand(this));
        getLogger().info("InvSeeker v" + VERSION + " 已加载！");
    }

    public static InvSeeker getInstance() {
        return instance;
    }

    public FileConfiguration getPluginConfig() {
        return config;
    }

    public static final String VERSION = "1.0.0"; // 插件版本号
}