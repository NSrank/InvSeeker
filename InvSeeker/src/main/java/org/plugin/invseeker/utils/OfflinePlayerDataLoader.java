package org.plugin.invseeker.utils;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class OfflinePlayerDataLoader {

    /**
     * 加载离线玩家的背包
     *
     * @param uuid 玩家的唯一 ID
     * @return 包含离线玩家背包内容的虚拟 Inventory
     */
    public static Inventory loadOfflineInventory(UUID uuid) {
        try {
            // 获取玩家数据文件路径
            File playerDataFile = getPlayerDataFile(uuid);
            if (playerDataFile == null || !playerDataFile.exists()) {
                Bukkit.getLogger().warning("玩家数据文件不存在: " + (playerDataFile != null ? playerDataFile.getAbsolutePath() : "未知路径"));
                return null;
            }

            // 读取玩家数据文件
            NBTFile nbtFile = new NBTFile(playerDataFile); // 确保传入有效的 File 对象
            NBTCompound data = getDataCompound(nbtFile); // 直接返回根节点
            if (data == null) {
                Bukkit.getLogger().warning("数据文件为空: " + playerDataFile.getAbsolutePath());
                return null;
            }

            // 创建一个虚拟 GUI
            Inventory gui = Bukkit.createInventory(null, 36, ChatColor.DARK_GRAY + "离线玩家的背包");

            // 填充背包内容
            NBTCompound inventory = data.getCompound("Inventory");
            if (inventory != null) {
                for (String key : inventory.getKeys()) {
                    NBTCompound itemCompound = inventory.getCompound(key);
                    if (itemCompound != null) {
                        ItemStack item = createItemFromNBT(itemCompound); // 使用自定义方法解析物品
                        if (item == null) {
                            Bukkit.getLogger().warning("无法解析物品数据: " + key);
                            continue; // 跳过无效物品
                        }
                        int slot = itemCompound.getInteger("Slot"); // 使用 "Slot" 键获取槽位编号
                        if (slot >= 0 && slot < 36) { // 确保槽位编号有效
                            gui.setItem(slot, item);
                        }
                    }
                }
            } else {
                Bukkit.getLogger().warning("数据文件中未找到 'Inventory' 组合: " + playerDataFile.getAbsolutePath());
            }

            // 填充装备栏
            NBTCompound armor = data.getCompound("ArmorItems");
            if (armor != null) {
                for (int i = 0; i < 4; i++) {
                    NBTCompound itemCompound = armor.getCompound(String.valueOf(i));
                    if (itemCompound != null) {
                        ItemStack item = createItemFromNBT(itemCompound); // 使用自定义方法解析物品
                        if (item != null) {
                            gui.setItem(36 + i, item); // 装备栏放置在第 4 行
                        }
                    }
                }
            } else {
                Bukkit.getLogger().warning("数据文件中未找到 'ArmorItems' 组合: " + playerDataFile.getAbsolutePath());
            }

            // 填充副手物品
            NBTCompound offHand = data.getCompound("Offhand");
            if (offHand != null) {
                NBTCompound itemCompound = offHand.getCompound("0");
                if (itemCompound != null) {
                    ItemStack item = createItemFromNBT(itemCompound); // 使用自定义方法解析物品
                    if (item != null) {
                        gui.setItem(40, item); // 副手物品放置在第 5 行中间
                    }
                }
            } else {
                Bukkit.getLogger().warning("数据文件中未找到 'Offhand' 组合: " + playerDataFile.getAbsolutePath());
            }

            return gui;
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("加载离线玩家背包时发生错误！");
            return null;
        }
    }

    /**
     * 加载离线玩家的末影箱
     *
     * @param uuid 玩家的唯一 ID
     * @return 包含离线玩家末影箱内容的虚拟 Inventory
     */
    public static Inventory loadOfflineEnderChest(UUID uuid) {
        try {
            // 获取玩家数据文件路径
            File playerDataFile = getPlayerDataFile(uuid);
            if (playerDataFile == null || !playerDataFile.exists()) {
                Bukkit.getLogger().warning("玩家数据文件不存在: " + (playerDataFile != null ? playerDataFile.getAbsolutePath() : "未知路径"));
                return null;
            }

            // 读取玩家数据文件
            NBTFile nbtFile = new NBTFile(playerDataFile); // 确保传入有效的 File 对象
            NBTCompound data = getDataCompound(nbtFile); // 直接返回根节点
            if (data == null) {
                Bukkit.getLogger().warning("数据文件为空: " + playerDataFile.getAbsolutePath());
                return null;
            }

            // 创建一个虚拟 GUI
            Inventory gui = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "离线玩家的末影箱");

            // 填充末影箱内容
            NBTCompound enderChest = data.getCompound("EnderItems");
            if (enderChest != null) {
                for (String key : enderChest.getKeys()) {
                    NBTCompound itemCompound = enderChest.getCompound(key);
                    if (itemCompound != null) {
                        ItemStack item = createItemFromNBT(itemCompound); // 使用自定义方法解析物品
                        if (item == null) {
                            Bukkit.getLogger().warning("无法解析物品数据: " + key);
                            continue; // 跳过无效物品
                        }
                        int slot = itemCompound.getInteger("Slot"); // 使用 "Slot" 键获取槽位编号
                        if (slot >= 0 && slot < 27) { // 确保槽位编号有效
                            gui.setItem(slot, item);
                        }
                    }
                }
            } else {
                Bukkit.getLogger().warning("数据文件中未找到 'EnderItems' 组合: " + playerDataFile.getAbsolutePath());
            }

            return gui;
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("加载离线玩家末影箱时发生错误！");
            return null;
        }
    }

    /**
     * 获取玩家数据文件路径
     *
     * @param uuid 玩家的唯一 ID
     * @return 玩家数据文件
     */
    private static File getPlayerDataFile(UUID uuid) {
        // 动态获取玩家所在的世界
        Optional<String> worldName = Bukkit.getServer().getWorlds().stream()
                .filter(world -> new File(world.getName() + "/playerdata/" + uuid.toString() + ".dat").exists())
                .findFirst()
                .map(world -> world.getName());

        String defaultWorldName = worldName.orElse("world"); // 默认使用 "world"
        File playerDataFile = new File(defaultWorldName + "/playerdata/" + uuid.toString() + ".dat");

        // 打印加载的文件路径
        Bukkit.getLogger().info("加载的玩家数据文件路径: " + playerDataFile.getAbsolutePath());
        return playerDataFile;
    }

    /**
     * 自动检测并获取数据组合（兼容 'Data' 和 'data'）
     *
     * @param nbtFile NBT 文件对象
     * @return 数据组合（如果存在），否则返回 null
     */
    private static NBTCompound getDataCompound(NBTFile nbtFile) {
        // 打印所有顶级键名
        Bukkit.getLogger().info("数据文件中的顶级键: " + nbtFile.getKeys());

        // 直接返回根节点
        return nbtFile;
    }

    /**
     * 从 NBT 数据中创建 ItemStack
     *
     * @param itemCompound 包含物品数据的 NBTCompound
     * @return 解析后的 ItemStack，如果解析失败则返回 null
     */
    private static ItemStack createItemFromNBT(NBTCompound itemCompound) {
        try {
            // 检查是否包含有效的物品数据
            if (itemCompound == null || !itemCompound.hasKey("id")) {
                return null;
            }

            // 获取物品的 Material
            String materialId = itemCompound.getString("id");
            Material material = Material.matchMaterial(materialId);
            if (material == null || material == Material.AIR) {
                Bukkit.getLogger().warning("无法识别的物品类型: " + materialId);
                return null;
            }

            // 获取物品的数量
            int count = itemCompound.getInteger("Count");
            if (count <= 0) {
                Bukkit.getLogger().warning("物品数量无效: " + count);
                return null;
            }

            // 创建 ItemStack
            ItemStack item = new ItemStack(material, count);

            // 如果存在附加数据（如耐久度、附魔等），解析并应用
            if (itemCompound.hasKey("tag")) {
                NBTCompound tag = itemCompound.getCompound("tag");
                if (tag != null) {
                    // 使用 NBT-API 将标签应用到 ItemStack
                    NBTItem nbtItem = new NBTItem(item); // 使用 NBTItem 包装 ItemStack
                    nbtItem.mergeCompound(tag); // 合并附加数据
                    item = nbtItem.getItem(); // 获取更新后的 ItemStack
                }
            }

            return item;
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("解析物品数据时发生错误！");
            return null;
        }
    }
}