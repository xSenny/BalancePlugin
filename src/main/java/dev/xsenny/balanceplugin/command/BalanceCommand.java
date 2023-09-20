package dev.xsenny.balanceplugin.command;

import dev.xsenny.balanceplugin.BalancePlugin;
import dev.xsenny.balanceplugin.manager.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements CommandExecutor, TabCompleter {

    private BalancePlugin plugin;

    public BalanceCommand(BalancePlugin plugin) {
        this.plugin = plugin;

        plugin.getCommand("bal").setExecutor(this);
        plugin.getCommand("bal").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {

            if (args.length != 1) {
                System.out.println("Use /bal <name>");
                return true;
            }
            String name = args[0];
            OfflinePlayer p = Bukkit.getOfflinePlayer(name);
            if (!plugin.getPlayerDataManager().doesAPlayerExist(p.getUniqueId())) {
                System.out.println("You cannot see this player's balance, because he does not have a balance, because he did not play on this server with this plugin on.");
                return true;
            }
            System.out.println(name + "'s balance is: " + plugin.getPlayerDataManager().getPlayerData(p.getUniqueId()).getMoney());
            return true;
        }

        Player p = (Player) sender;

        if (args.length > 1) {
            p.sendMessage(ChatColor.RED + "Use /bal <name> or /bal to see your balance.");
            return true;
        }

        if (args.length == 0) {
            long money = plugin.getPlayerDataManager().getPlayerData(p.getUniqueId()).getMoney();
            p.sendMessage(ChatColor.GREEN + "Your balance is " + money);
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (!plugin.getPlayerDataManager().doesAPlayerExist(player.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "You cannot see this player's balance, because he does not have a balance, because he did not play on this server with this plugin on.");
            return true;
        }
        long money = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId()).getMoney();
        p.sendMessage(ChatColor.GREEN + args[0] + "'s balance is: " + money);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return GiveCommand.getPlayerNames(args);
    }
}
