package org.plugin.invseeker.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.plugin.invseeker.InvSeeker;
import org.plugin.invseeker.gui.InventoryGUI;

public class InvSeeCommand implements CommandExecutor {
    private final InvSeeker plugin;

    public InvSeeCommand(InvSeeker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c控制台无法打开 GUI！");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("invsee.view")) {
            player.sendMessage(plugin.getPluginConfig().getString("messages.no-permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(plugin.getPluginConfig().getString("messages.invalid-usage"));
            return true;
        }

        String targetName = args[0];
        Inventory gui = InventoryGUI.createInventory(targetName, "背包");
        if (gui == null) {
            player.sendMessage(plugin.getPluginConfig().getString("messages.player-not-found").replace("%player%", targetName));
            return true;
        }
        player.openInventory(gui);
        return true;
    }
}
