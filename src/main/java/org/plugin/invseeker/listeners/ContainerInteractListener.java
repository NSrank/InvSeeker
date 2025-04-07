package org.plugin.invseeker.listeners;

import org.bukkit.event.inventory.InventoryAction;
import org.plugin.invseeker.InvSeeker;
import org.plugin.invseeker.utils.LogManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContainerInteractListener implements Listener {
    private final InvSeeker plugin;

    public ContainerInteractListener(InvSeeker plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!plugin.getConfig().getBoolean("enable-container-logging", false)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();

        // 确保是容器交互
        if (clickedInventory == null || !(clickedInventory.getHolder() instanceof Container)) {
            return;
        }

        // 获取容器位置
        Container container = (Container) clickedInventory.getHolder();
        Location location = container.getLocation();
        String world = location.getWorld().getName();
        String coordinates = String.format("%d,%d,%d",
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );

        // 判断交互类型
        String actionType = "";
        if (event.getAction().name().contains("PLACE")) {
            actionType = "+";
        } else if (event.getAction().name().contains("PICKUP")) {
            actionType = "-";
        }

        // 记录日志
        if (!actionType.isEmpty() && item != null && item.getType() != Material.AIR) {
            String logEntry = String.format("%s %s %s %s %s %s %d",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    player.getName(),
                    world,
                    coordinates,
                    actionType,
                    item.getType().getKey().getKey(),
                    item.getAmount()
            );
            plugin.getLogManager().writeLog(logEntry);
        }
    }
}
