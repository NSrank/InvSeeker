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
import org.plugin.invseeker.gui.InventoryGUI;


public class InvSeeCommand implements CommandExecutor {
    private final InvSeeker plugin;

    public InvSeeCommand(InvSeeker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("invseeker.view")) {
            sender.sendMessage(ChatColor.RED + "[InvSeeker] 你没有权限执行此操作！");
            return true;
        }

        // 处理子命令
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("invseeker.admin")) {
                    sender.sendMessage(ChatColor.RED + "[InvSeeker] 你没有权限重载配置！");
                    return true;
                }
                reloadConfig(sender);
                return true;

            default:
                // 查看玩家背包
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "[InvSeeker] 控制台无法打开 GUI！");
                    return true;
                }

                Player viewer = (Player) sender;
                String targetName = args[0];
                Player target = Bukkit.getPlayer(targetName);

                if (target == null) {
                    viewer.sendMessage(plugin.getConfig().getString("messages.player-not-found").replace("%player%", targetName));
                    return true;
                }

                Inventory gui = target.getInventory();
                viewer.openInventory(gui);
                return true;
        }
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "===== [InvSeeker] 插件帮助 =====");
        sender.sendMessage(ChatColor.YELLOW + "/invsee help" + ChatColor.WHITE + " - 显示帮助信息");
        sender.sendMessage(ChatColor.YELLOW + "/invsee reload" + ChatColor.WHITE + " - 重载配置文件（需要管理员权限）");
        sender.sendMessage(ChatColor.YELLOW + "/invsee <玩家名>" + ChatColor.WHITE + " - 查看玩家背包");
        sender.sendMessage(ChatColor.YELLOW + "/endersee <玩家名>" + ChatColor.WHITE + " - 查看玩家末影箱");
    }

    private void reloadConfig(CommandSender sender) {
        plugin.reloadConfig(); // 重载配置文件
        sender.sendMessage(ChatColor.GREEN + "[InvSeeker] 配置文件已成功重载！");
    }

    private Inventory createPlayerInventoryGUI(Player target) {
        // 创建一个箱子类型的 GUI（6 行，54 格）
        Inventory gui = Bukkit.createInventory(null, 54, "§8" + target.getName() + " 的背包");

        // 填充玩家背包内容（9x3 格）
        ItemStack[] playerInventory = target.getInventory().getContents();
        for (int i = 0; i < 27; i++) {
            gui.setItem(i, playerInventory[i]);
        }

        // 填充装备栏（最后 9 格）
        ItemStack[] armorContents = target.getInventory().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            gui.setItem(45 + i, armorContents[i]); // 放入第 5-6 行
        }

        // 填充手持物品栏（中间 9 格）
        ItemStack offHand = target.getInventory().getItemInOffHand();
        gui.setItem(36, offHand); // 放入手持物品栏

        return gui;
    }

}
