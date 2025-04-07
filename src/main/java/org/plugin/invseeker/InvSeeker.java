package org.plugin.invseeker;

import org.bukkit.Bukkit;
import org.plugin.invseeker.listeners.InventoryClickListener;
import org.plugin.invseeker.command.InvSeeCommand;
import org.plugin.invseeker.command.EnderSeeCommand;
import org.plugin.invseeker.listeners.ContainerInteractListener;
import org.plugin.invseeker.utils.LogManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class InvSeeker extends JavaPlugin {
    private static InvSeeker instance;
    private FileConfiguration config;
    private String serverVersion;
    public static InvSeeker getInstance() {
        return instance;
    }
    public String getServerVersion() {
        return serverVersion;
    }
    private LogManager logManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        config = getConfig();
        // 注册命令
        if (getCommand("invsee") != null) {
            getCommand("invsee").setExecutor(new InvSeeCommand(this));
        } else {
            getLogger().severe("命令 'invsee' 未在 plugin.yml 中定义！");
        }

        if (getCommand("endersee") != null) {
            getCommand("endersee").setExecutor(new EnderSeeCommand(this));
        } else {
            getLogger().severe("命令 'endersee' 未在 plugin.yml 中定义！");
        }
        getLogger().info("InvSeeker v" + VERSION + " 已成功加载！");

        this.logManager = new LogManager(this); // 初始化日志管理器
        getServer().getPluginManager().registerEvents(new ContainerInteractListener(this), this);
        getLogger().info("容器交互日志功能已加载！");
    }

    @Override
    public void onDisable() {
        getLogger().info("InvSeeker v" + VERSION + " 已成功卸载！");
        if (this.logManager != null) {
            this.logManager.flushLogs(); // 关闭时刷新日志
        }
        getLogger().info("当日日志已保存！");
    }

    public FileConfiguration getPluginConfig() {
        return config;
    }

    public static final String VERSION = "1.5.1"; // 插件版本号

    public LogManager getLogManager() {
        return this.logManager;
    }
}