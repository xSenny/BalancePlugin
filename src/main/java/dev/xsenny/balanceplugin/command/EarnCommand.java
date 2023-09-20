package dev.xsenny.balanceplugin.command;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.xsenny.balanceplugin.BalancePlugin;
import dev.xsenny.balanceplugin.db.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class EarnCommand implements CommandExecutor {

    private BalancePlugin plugin;

    Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();

    public EarnCommand(BalancePlugin plugin) {
        this.plugin = plugin;

        plugin.getCommand("earn").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player p)) {
            System.out.println("You cannot use this command with the console.");
            return true;
        }

        if (cooldown.asMap().containsKey(p.getUniqueId())) {
            p.sendMessage("You can use this command once in a minute.");
            long distance = cooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis();
            p.sendMessage("You must wait " + TimeUnit.MILLISECONDS.toSeconds(distance) + " seconds.");
            return true;
        }

        PlayerData pd = plugin.getPlayerDataManager().getPlayerData(p.getUniqueId());
        int amount = new Random().nextInt(5) + 1;
        pd.setMoney(pd.getMoney() + amount);
        p.sendMessage(ChatColor.GREEN + "You have won " + amount + " money!");
        cooldown.asMap().put(p.getUniqueId(), System.currentTimeMillis() + 60000);

        return true;
    }
}
