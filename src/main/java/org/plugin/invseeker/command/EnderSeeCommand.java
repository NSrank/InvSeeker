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
import org.plugin.invseeker.data.PlayerDataLoader;
import org.plugin.invseeker.gui.InventoryGUI;

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
            viewer.sendMessage(plugin.getConfig().getString("messages.no-permission"));
            return true;
        }

        // 检查参数是否为空
        if (args.length == 0 || args[0].isEmpty()) {
            viewer.sendMessage(plugin.getConfig().getString("messages.invalid-usage"));
            return true;
        }

        // 获取目标玩家名称
        String targetName = args[0]; // 从命令参数中提取目标玩家名

        // 检查目标玩家是否存在
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            viewer.sendMessage(plugin.getConfig().getString("messages.player-not-found").replace("%player%", targetName));
            return true;
        }

        // 创建一个自定义的末影箱 GUI
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + target.getName() + " 的末影箱");

        // 填充目标玩家的末影箱内容
        ItemStack[] enderChestContents = target.getEnderChest().getContents();
        for (int i = 0; i < enderChestContents.length; i++) {
            gui.setItem(i, enderChestContents[i]);
        }

        // 打开 GUI
        viewer.openInventory(gui);
        return true;
    }
}
