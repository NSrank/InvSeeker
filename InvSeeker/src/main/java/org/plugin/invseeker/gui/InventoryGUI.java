package org.plugin.invseeker.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryGUI {
    public static Inventory createInventory(String playerName, String type) {
        Inventory gui = Bukkit.createInventory(null, 45, "§8[§bInvSee§8] §7" + playerName + " 的" + type);

        // 添加返回按钮
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        ((ItemMeta) backMeta).setDisplayName("§c返回");
        backButton.setItemMeta(backMeta);
        gui.setItem(36, backButton);

        // 添加玩家头像
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();
        headMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        headMeta.setDisplayName("§6" + playerName);
        playerHead.setItemMeta(headMeta);
        gui.setItem(40, playerHead);

        return gui;
    }
}