package org.plugin.invseeker.utils;

import org.plugin.invseeker.InvSeeker;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {
    private final InvSeeker plugin;
    private File logFile;
    private FileWriter writer;
    private String currentLogDate;

    public LogManager(InvSeeker plugin) {
        this.plugin = plugin;
        initializeLogger();
    }

    private void initializeLogger() {
        File logDir = new File(plugin.getDataFolder(), "logs");
        if (!logDir.exists()) logDir.mkdirs();

        currentLogDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        logFile = new File(logDir, currentLogDate + ".log");
        try {
            writer = new FileWriter(logFile, true);
        } catch (IOException e) {
            plugin.getLogger().severe("无法创建日志文件: " + e.getMessage());
        }
    }

    public void writeLog(String entry) {
        if (!plugin.getConfig().getBoolean("enable-container-logging", false)) return;

        // 检查是否需要新建日志文件（每日零点）
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (!today.equals(currentLogDate)) {
            close();
            initializeLogger();
        }

        try {
            writer.write(entry + "\n");
            writer.flush();
        } catch (IOException e) {
            plugin.getLogger().severe("日志写入失败: " + e.getMessage());
        }
    }

    public void flushLogs() {
        try {
            if (writer != null) writer.flush();
        } catch (IOException e) {
            plugin.getLogger().warning("日志刷新失败: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (writer != null) writer.close();
        } catch (IOException e) {
            plugin.getLogger().warning("日志关闭失败: " + e.getMessage());
        }
    }
}
