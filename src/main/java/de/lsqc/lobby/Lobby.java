package de.lsqc.lobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.lsqc.lobby.listeners.PlayerJoinQuitListener;

public final class Lobby extends JavaPlugin
{

    @Override
    public void onEnable()
    {

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "velocity:player");

        this.registerListeners();
    }

    @Override
    public void onDisable()
    {
    }

    public void registerListeners()
    {
    
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinQuitListener(), this);
    }

    public void registerCommands()
    {
    }

}
