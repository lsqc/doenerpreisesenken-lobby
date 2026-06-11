package de.lsqc.lobby;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.lsqc.lobby.listeners.InventoryListener;
import de.lsqc.lobby.listeners.PlayerInteractListener;
import de.lsqc.lobby.listeners.PlayerItemHeldListener;
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

    @Getter
    private Properties serverProperties = new Properties();

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

        try (FileInputStream fis = new FileInputStream("server.properties"))
        {
            serverProperties.load(fis);
        }
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
        pluginManager.registerEvents(new PlayerItemHeldListener(), this);
    }

    public void registerCommands()
    {
    }

}
