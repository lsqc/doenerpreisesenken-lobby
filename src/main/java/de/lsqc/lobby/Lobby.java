package de.lsqc.lobby;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.lsqc.lobby.listeners.PlayerJoinQuitListener;
import lombok.Getter;

public final class Lobby extends JavaPlugin
{

    @Getter
    private static Lobby instance;

    @Getter
    private YamlConfiguration config;

    @Override
    public void onEnable()
    {
        instance = this;

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "velocity:player");
        this.registerListeners();

        this.getDataFolder().mkdirs();

        this.config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
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
