package dev.xsenny.balanceplugin.command;

import dev.xsenny.balanceplugin.BalancePlugin;
import dev.xsenny.balanceplugin.db.PlayerData;
import dev.xsenny.balanceplugin.manager.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Logger;

public class SetBalCommand implements CommandExecutor, TabCompleter {

    private BalancePlugin plugin;
    public SetBalCommand(BalancePlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("setbal").setExecutor(this);
        plugin.getCommand("setbal").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        PlayerDataManager pdm = plugin.getPlayerDataManager();
        if (!(sender instanceof Player p)) {
            if (args.length != 2) {
                System.out.println("Use /setbal <player> <amount>");
                return true;
            }
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println(args[1] + " is not a number.");
                return true;
            }

            if (amount <= 0) {
                System.out.println("The amount of money should be positive.");
                return true;
            }

            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            if (!pdm.doesAPlayerExist(player.getUniqueId())) {
                System.out.println(args[0] + " does not have a balance at the moment.");
                return true;
            }

            PlayerData playerData = pdm.getPlayerData(player.getUniqueId());

            playerData.setMoney(amount);

            System.out.println("You successfully set " + args[0] + " balance to " + amount);

            return true;
        }

        if (!p.isOp()) {
            p.sendMessage(ChatColor.RED + "You cannot use this command");
            return true;
        }

        if (args.length != 2) {
            p.sendMessage(ChatColor.RED + "Use /setbal <player> <amount>");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            p.sendMessage(ChatColor.RED + args[1] + " is not a number.");
            return true;
        }

        if (amount <= 0) {
            p.sendMessage(ChatColor.RED + "The amount of money should be positive.");
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (!pdm.doesAPlayerExist(player.getUniqueId())) {
            p.sendMessage(ChatColor.RED + args[0] + " does not have a balance at the moment.");
            return true;
        }

        PlayerData playerData = pdm.getPlayerData(player.getUniqueId());

        playerData.setMoney(amount);

        p.sendMessage(ChatColor.GREEN + "You successfully set " + args[0] + " balance to " + amount);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return GiveCommand.getPlayerNames(args);
    }
}
