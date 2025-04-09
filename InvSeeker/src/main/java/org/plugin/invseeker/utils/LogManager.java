package org.plugin.invseeker.utils;

import org.bukkit.Bukkit;
import org.plugin.invseeker.InvSeeker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {
    private final InvSeeker plugin;
    private FileWriter writer;
    private File logFile;
    private String currentLogDate;

    public LogManager(InvSeeker plugin) {
        this.plugin = plugin;
        initializeLogger();
    }

    private void initializeLogger() {
        // 创建日志目录
        File logDir = new File(plugin.getDataFolder(), "logs");
        if (!logDir.exists()) {
            if (!logDir.mkdirs()) {
                plugin.getLogger().severe("无法创建日志目录！");
                return;
            }
        }

        // 获取当前日期
        currentLogDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        logFile = new File(logDir, currentLogDate + ".log");

        try {
            // 打开文件写入流（追加模式）
            writer = new FileWriter(logFile, true);
            plugin.getLogger().info("日志文件已初始化：" + logFile.getAbsolutePath());
        } catch (IOException e) {
            plugin.getLogger().severe("无法创建日志文件: " + e.getMessage());
        }
    }

    public void writeLog(String entry) {
        if (!plugin.getConfig().getBoolean("enable-container-logging", false)) {
            return; // 如果日志记录功能未启用，直接返回
        }

        // 检查是否需要切换日志文件
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (!today.equals(currentLogDate)) {
            close(); // 关闭旧文件
            initializeLogger(); // 初始化新文件
        }

        try {
            // 写入日志条目
            writer.write(entry + "\n");
            writer.flush(); // 确保缓冲区数据写入文件
        } catch (IOException e) {
            plugin.getLogger().severe("日志写入失败: " + e.getMessage());
        }
    }

    public void flushLogs() {
        try {
            if (writer != null) {
                writer.flush(); // 刷新缓冲区
            }
        } catch (IOException e) {
            plugin.getLogger().warning("日志刷新失败: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.flush(); // 刷新缓冲区
                writer.close(); // 关闭文件句柄
                plugin.getLogger().info("日志文件已关闭。");
            }
        } catch (IOException e) {
            plugin.getLogger().warning("日志关闭失败: " + e.getMessage());
        }
    }
}