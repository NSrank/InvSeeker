package org.plugin.invseeker.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.plugin.invseeker.InvSeeker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContainerInteractListener implements Listener {
    private final InvSeeker plugin;

    public ContainerInteractListener(InvSeeker plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 检查日志功能是否启用
        if (!plugin.getConfig().getBoolean("enable-container-logging", false)) {
            return; // 如果未启用，直接返回
        }

        // 获取事件相关对象
        Player player = (Player) event.getWhoClicked();
        Inventory topInventory = event.getView().getTopInventory(); // 容器库存
        Inventory clickedInventory = event.getClickedInventory(); // 被点击的库存

        // 确保是容器交互
        if (!(topInventory.getHolder() instanceof Container)) {
            return; // 如果顶部库存不是容器，直接返回
        }

        // 忽略玩家背包操作
        if (clickedInventory != null && clickedInventory.equals(event.getView().getBottomInventory())) {
            return; // 如果点击的是玩家背包，直接返回
        }

        // 获取容器位置
        Container container = (Container) topInventory.getHolder();
        Location location = container.getLocation();
        String world = location.getWorld().getName();
        String coordinates = formatLocation(location);

        // 判断交互类型
        String actionType = "";
        ItemStack item = null;

        switch (event.getAction()) {
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
                actionType = "+"; // 放入物品
                item = event.getCursor(); // 光标上的物品
                break;

            case PICKUP_ALL:
            case PICKUP_ONE:
            case PICKUP_SOME:
            case PICKUP_HALF:
                actionType = "-"; // 取出物品
                item = event.getCurrentItem(); // 当前槽位的物品
                break;

            case MOVE_TO_OTHER_INVENTORY:
                // Shift + 点击时，物品从一个库存移动到另一个库存
                if (clickedInventory == event.getView().getBottomInventory()) {
                    actionType = "+"; // 移入容器
                    item = event.getCurrentItem();
                } else if (clickedInventory == topInventory) {
                    actionType = "-"; // 从容器移出
                    item = event.getCurrentItem();
                }
                break;

            case COLLECT_TO_CURSOR:
                // 拖动收集物品到光标
                if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                    actionType = "+"; // 放入物品
                    item = event.getCursor(); // 光标上的物品
                } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                    actionType = "+"; // 放入物品
                    item = event.getCurrentItem(); // 当前槽位的物品
                }
                break;

            default:
                return; // 忽略其他未知操作
        }

        // 确保物品有效
        if (item == null || item.getType() == Material.AIR) {
            return; // 如果物品无效，直接返回
        }

        // 记录日志
        String logEntry = String.format("%s %s %s %s %s %s %d",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                player.getName(),
                world,
                coordinates,
                actionType,
                item.getType().getKey().getKey(),
                item.getAmount());
        plugin.getLogManager().writeLog(logEntry);
    }

    private String formatLocation(Location location) {
        return String.format("%d,%d,%d",
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ());
    }
}