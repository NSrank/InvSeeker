package org.plugin.invseeker.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("InvSeeker")) {
            event.setCancelled(true); // 禁止修改物品

            if (event.getSlot() == 36) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
            }
        }
    }
}
