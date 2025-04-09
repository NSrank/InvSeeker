package org.plugin.invseeker.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.plugin.invseeker.InvSeeker;
import org.plugin.invseeker.utils.OfflinePlayerDataLoader;

public class EnderSeeCommand implements CommandExecutor {
    private final InvSeeker plugin;

    public EnderSeeCommand(InvSeeker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查发送者是否为玩家
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "[InvSeeker] 控制台无法打开 GUI！");
            return true;
        }

        Player viewer = (Player) sender; // 定义查看者
        if (!viewer.hasPermission("invseeker.view")) {
            viewer.sendMessage(plugin.getConfig().getString("messages.no-permission", "§c你没有权限执行此操作！"));
            return true;
        }

        // 检查参数是否为空
        if (args.length == 0 || args[0].isEmpty()) {
            viewer.sendMessage(plugin.getConfig().getString("messages.invalid-usage", "§c用法: /endersee <玩家名>"));
            return true;
        }

        // 获取目标玩家名称
        String targetName = args[0]; // 从命令参数中提取目标玩家名

        // 检查目标玩家是否存在（在线玩家）
        Player target = Bukkit.getPlayer(targetName);

        // 在线玩家
        if (target != null) {
            Inventory gui = createEnderChestGUI(target);
            viewer.openInventory(gui);
            return true;
        }

        // 离线玩家
        try {
            org.bukkit.@org.jetbrains.annotations.NotNull OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);
            if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                Inventory gui = OfflinePlayerDataLoader.loadOfflineEnderChest(offlinePlayer.getUniqueId());
                if (gui != null) {
                    viewer.openInventory(gui);
                    return true;
                } else {
                    viewer.sendMessage(ChatColor.RED + "[InvSeeker] §c无法加载离线玩家的末影箱！");
                    return true;
                }
            } else {
                String message = plugin.getConfig().getString("messages.player-not-found", "§c玩家 %player% 不存在！")
                        .replace("%player%", targetName);
                viewer.sendMessage(ChatColor.RED + "[InvSeeker] " + message);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            viewer.sendMessage(ChatColor.RED + "[InvSeeker] §c加载离线玩家数据时发生错误！");
            return true;
        }
    }

    /**
     * 创建在线玩家的末影箱 GUI
     *
     * @param target 目标玩家
     * @return 包含目标玩家末影箱内容的虚拟 GUI
     */
    private Inventory createEnderChestGUI(Player target) {
        // 创建一个自定义的末影箱 GUI
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + target.getName() + " 的末影箱");

        // 填充目标玩家的末影箱内容
        ItemStack[] enderChestContents = target.getEnderChest().getContents();
        for (int i = 0; i < enderChestContents.length; i++) {
            gui.setItem(i, enderChestContents[i]);
        }

        return gui;
    }
}
