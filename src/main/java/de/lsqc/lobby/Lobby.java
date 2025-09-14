package de.lsqc.lobby;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.lsqc.lobby.listeners.InventoryListener;
import de.lsqc.lobby.listeners.PlayerInteractListener;
import de.lsqc.lobby.listeners.PlayerJoinQuitListener;
import de.lsqc.lobby.listeners.PlayerProtectionListener;
import de.lsqc.lobby.listeners.WorldProtectionListener;
import de.lsqc.lobby.utils.LocationManager;
import lombok.Getter;

public final class Lobby extends JavaPlugin
{

    @Getter
    private static Lobby instance;

    @Getter
    private LocationManager locationManager; 

    private File configFile, locationsFile;

    @Override
    public void onEnable()
    {
        instance = this;

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "velocity:player");
        this.registerListeners();

        this.getDataFolder().mkdirs();
        this.configFile = new File(getDataFolder(), "config.yml");
        this.locationsFile = new File(getDataFolder(), "locations.yml");

        this.locationManager = new LocationManager(this.locationsFile);
        this.locationManager.loadConfig();

    }

    @Override
    public void onDisable()
    {
        this.locationManager.saveConfig();
    }

    public void registerListeners()
    {

        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerJoinQuitListener(), this);
        pluginManager.registerEvents(new WorldProtectionListener(), this);
        pluginManager.registerEvents(new PlayerProtectionListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
    }

    public void registerCommands()
    {
    }

}
