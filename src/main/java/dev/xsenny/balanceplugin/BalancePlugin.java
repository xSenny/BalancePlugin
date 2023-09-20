package dev.xsenny.balanceplugin;

import dev.xsenny.balanceplugin.command.BalanceCommand;
import dev.xsenny.balanceplugin.command.EarnCommand;
import dev.xsenny.balanceplugin.command.GiveCommand;
import dev.xsenny.balanceplugin.command.SetBalCommand;
import dev.xsenny.balanceplugin.db.Database;
import dev.xsenny.balanceplugin.listener.PlayerJoinListener;
import dev.xsenny.balanceplugin.manager.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class BalancePlugin extends JavaPlugin {

    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("The balance plugin has load up.");
        System.out.println("Made by xSenny_ for Orbital Studios.");

        Database.connect(this);
        Database.onUpdate("CREATE TABLE IF NOT EXISTS players (uuid VARCHAR(40), money LONG)");
        System.out.println("Created table");

        //Managers:
        playerDataManager = new PlayerDataManager(this);

        //Commands:
        new BalanceCommand(this);
        new EarnCommand(this);
        new GiveCommand(this);
        new SetBalCommand(this);

        //Listeners:
        new PlayerJoinListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Database.disconnect();
    }

    public PlayerDataManager getPlayerDataManager() { return playerDataManager; }

}
