package org.plugin.invseeker.data;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.io.File;
import java.util.UUID;

public class PlayerDataLoader {
    public static Inventory loadOfflineInventory(String playerName, String type) {
        File playerDataFolder = new File(Bukkit.getWorlds().get(0).getName() + "/playerdata");
        File[] files = playerDataFolder.listFiles((dir, name) -> name.endsWith(".dat"));

        if (files == null) return null;

        for (File file : files) {
            String uuid = file.getName().replace(".dat", "");
            String playerNameFromUUID = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
            if (playerNameFromUUID != null && playerNameFromUUID.equalsIgnoreCase(playerName)) {
                try {
                    // 通过 NMS 解析玩家数据（此处需适配 Paper 1.20.1）
                    // 示例代码（需处理混淆）：
                    Object nmsPlayer = getPlayerHandle(playerNameFromUUID);
                    if (nmsPlayer == null) return null;

                    Inventory inventory;
                    if (type.equals("背包")) {
                        inventory = (Inventory) nmsPlayer.getClass().getMethod("getInventory").invoke(nmsPlayer);
                    } else {
                        inventory = (Inventory) nmsPlayer.getClass().getMethod("getEnderChest").invoke(nmsPlayer);
                    }
                    return inventory;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    private static Object getPlayerHandle(String playerName) {
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(Bukkit.getPlayer(playerName));
            return craftPlayer.getClass().getMethod("getHandle").invoke(craftPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
