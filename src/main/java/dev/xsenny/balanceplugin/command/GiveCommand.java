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
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GiveCommand implements CommandExecutor, TabCompleter {

    private BalancePlugin plugin;

    public GiveCommand(BalancePlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("give").setExecutor(this);
        plugin.getCommand("give").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        PlayerDataManager pdm = plugin.getPlayerDataManager();

        if (!(sender instanceof Player p)) {
            System.out.println("This command should be used only by a player.");
            return true;
        }

        if (args.length != 2) {
            p.sendMessage(ChatColor.RED + "You should use the command like this: /give <player> <amount>");
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

        PlayerData pd = pdm.getPlayerData(p.getUniqueId());

        if (amount > pd.getMoney()) {
            p.sendMessage(ChatColor.RED + "You have less than " + amount + " money.");
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (!pdm.doesAPlayerExist(player.getUniqueId())) {
            p.sendMessage(ChatColor.RED + args[0] + " does not have a balance at the moment.");
            return true;
        }

        PlayerData playerData = pdm.getPlayerData(player.getUniqueId());

        playerData.setMoney(playerData.getMoney() + amount);
        pd.setMoney(pd.getMoney() - amount);

        p.sendMessage(ChatColor.GREEN + "You successfully added gave " + amount + " money to " + args[0] + "!");
        if (player.isOnline() && player.getPlayer() != null) {
            player.getPlayer().sendMessage(ChatColor.GREEN + p.getName() + " gave you " + amount + " money!.");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return getPlayerNames(args);
    }

    @NotNull
    static List<String> getPlayerNames(@NotNull String[] args) {
        List<String> c = new ArrayList<>();

        if (args.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                c.add(p.getName());
            }
            return StringUtil.copyPartialMatches(args[0], c, new ArrayList<>());
        }

        return c;
    }
}
