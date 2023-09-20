package dev.xsenny.balanceplugin.listener;

import dev.xsenny.balanceplugin.BalancePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private BalancePlugin plugin;

    public PlayerJoinListener(BalancePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!plugin.getPlayerDataManager().doesAPlayerExist(p.getUniqueId())) {
            plugin.getPlayerDataManager().newPlayerData(p.getUniqueId());
        }
    }

}
