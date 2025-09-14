package de.lsqc.lobby;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.lsqc.lobby.listeners.InventoryListener;
import de.lsqc.lobby.listeners.PlayerInteractListener;
import de.lsqc.lobby.listeners.PlayerJoinQuitListener;
import de.lsqc.lobby.listeners.PlayerProtectionListener;
import de.lsqc.lobby.listeners.WorldProtectionListener;
import de.lsqc.lobby.utils.LocationManager;
import lombok.Getter;
import lombok.SneakyThrows;

public final class Lobby extends JavaPlugin
{

    @Getter
    private static Lobby instance;

    @Getter
    private LocationManager locationManager; 

    private YamlConfiguration config;

    private File configFile, locationsFile;

    @Override @SneakyThrows
    public void onEnable()
    {
        instance = this;


        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.registerListeners();

        this.getDataFolder().mkdirs();
        this.configFile = new File(getDataFolder(), "config.yml");
        this.locationsFile = new File(getDataFolder(), "locations.yml");

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        if (!this.config.contains("survival_server")) this.config.set("survival_server", "prod-0");
        this.config.save(this.configFile);

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
