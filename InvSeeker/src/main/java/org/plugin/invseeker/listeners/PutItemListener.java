package org.plugin.invseeker.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.plugin.invseeker.InvSeeker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PutItemListener implements Listener {

    private final InvSeeker plugin;

    // 存储玩家的容器操作数据
    private final Map<UUID, ContainerData> playerContainerData = new HashMap<>();

    public PutItemListener(InvSeeker plugin) {
        this.plugin = plugin;
    }

    /**
     * 当玩家打开容器时触发
     */
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory container = event.getInventory();

        // 检查是否为容器
        if (!(container.getHolder() instanceof Container)) {
            return; // 如果不是容器，直接返回
        }

        // 强制刷新玩家背包数据
        player.updateInventory();

        // 记录玩家背包和容器的初始状态
        ItemStack[] playerInventory = player.getInventory().getContents().clone();
        ItemStack[] containerInventory = container.getContents().clone();

        // 存储数据
        Location location = ((Container) container.getHolder()).getLocation();
        playerContainerData.put(player.getUniqueId(), new ContainerData(playerInventory, containerInventory, location));
    }

    /**
     * 当玩家点击容器时触发
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        handleInventoryChange(event);
    }

    /**
     * 当玩家拖动物品时触发
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        handleInventoryChange(event);
    }

    /**
     * 处理库存变化事件
     */
    private void handleInventoryChange(Object event) {
        Player player = null;
        Inventory inventory = null;

        if (event instanceof InventoryClickEvent clickEvent) {
            player = (Player) clickEvent.getWhoClicked();
            inventory = clickEvent.getInventory();
        } else if (event instanceof InventoryDragEvent dragEvent) {
            player = (Player) dragEvent.getWhoClicked();
            inventory = dragEvent.getInventory();
        }

        if (player == null || !(inventory.getHolder() instanceof Container)) {
            return; // 如果没有玩家或不是容器，则跳过
        }

        UUID playerId = player.getUniqueId();
        ContainerData initialData = playerContainerData.get(playerId);
        if (initialData == null) {
            return; // 如果没有记录初始数据，则跳过
        }

        // 使用异步任务延迟执行库存对比
        Player finalPlayer = player;
        Inventory finalInventory = inventory;
        new BukkitRunnable() {
            @Override
            public void run() {
                // 强制刷新玩家背包和容器的库存数据
                finalPlayer.updateInventory();

                // 获取当前玩家背包和容器的状态
                ItemStack[] currentPlayerInventory = finalPlayer.getInventory().getContents();
                ItemStack[] currentContainerInventory = finalInventory.getContents();

                // 对比容器的变化
                Map<Material, Integer> containerChanges = compareInventories(initialData.containerInventory, currentContainerInventory);

                // 推断玩家的操作并记录日志
                for (Material material : containerChanges.keySet()) {
                    int containerDelta = containerChanges.getOrDefault(material, 0);

                    if (containerDelta > 0) {
                        // 玩家向容器中添加了物品
                        Bukkit.getLogger().info("[InvSeeker] 玩家 " + finalPlayer.getName() + " 向容器中添加了物品: " + material + " 数量: " + containerDelta);
                        logContainerInteraction(finalPlayer, initialData.location, "+", material, containerDelta);
                    }
                }

                // 更新存储的库存状态
                initialData.containerInventory = currentContainerInventory.clone();
            }
        }.runTaskLater(plugin, 5L); // 延迟 5 ticks 执行
    }

    /**
     * 当玩家关闭容器时触发
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID playerId = player.getUniqueId();

        // 获取玩家的容器操作数据
        playerContainerData.remove(playerId);
    }

    /**
     * 对比两个库存，返回每种物品的数量变化
     *
     * @param initialInventory 初始库存
     * @param currentInventory 当前库存
     * @return 每种物品的数量变化（正数表示增加，负数表示减少）
     */
    private Map<Material, Integer> compareInventories(ItemStack[] initialInventory, ItemStack[] currentInventory) {
        Map<Material, Integer> changes = new HashMap<>();

        // 遍历初始库存
        for (ItemStack item : initialInventory) {
            if (item != null && item.getType() != Material.AIR) { // 忽略 AIR
                Material material = item.getType();
                int amount = item.getAmount();
                changes.put(material, changes.getOrDefault(material, 0) - amount);
            }
        }

        // 遍历当前库存
        for (ItemStack item : currentInventory) {
            if (item != null && item.getType() != Material.AIR) { // 忽略 AIR
                Material material = item.getType();
                int amount = item.getAmount();
                changes.put(material, changes.getOrDefault(material, 0) + amount);
            }
        }

        // 移除无效的零值变化
        changes.entrySet().removeIf(entry -> entry.getValue() == 0);

        return changes;
    }

    /**
     * 记录容器交互日志
     *
     * @param player   玩家
     * @param location 容器位置
     * @param action   操作类型（+ 或 -）
     * @param material 物品类型
     * @param amount   物品数量
     */
    private void logContainerInteraction(Player player, Location location, String action, Material material, int amount) {
        String world = location.getWorld().getName();
        String coordinates = formatLocation(location);

        // 构建日志条目
        String logEntry = String.format("%s %s %s %s %s %s %d",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                player.getName(),
                world,
                coordinates,
                action,
                material.getKey().getKey(),
                amount);

        // 写入日志
        plugin.getLogManager().writeLog(logEntry);
    }

    /**
     * 格式化容器位置
     *
     * @param location 容器位置
     * @return 格式化的坐标字符串
     */
    private String formatLocation(Location location) {
        return String.format("%d,%d,%d",
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ());
    }

    /**
     * 存储玩家的容器操作数据
     */
    private static class ContainerData {
        public ItemStack[] containerInventory;
        public final Location location;

        public ContainerData(ItemStack[] playerInventory, ItemStack[] containerInventory, Location location) {
            this.containerInventory = containerInventory;
            this.location = location;
        }
    }
}