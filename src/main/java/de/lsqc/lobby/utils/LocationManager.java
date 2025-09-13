package de.lsqc.lobby.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.NonNull;
import lombok.SneakyThrows;

public class LocationManager 
{

    private final File file;
    private YamlConfiguration yamlConfiguration;

    public LocationManager(File file)
    {
        this.file = file;
    }


    public void loadConfig() 
    {
        if (!file.getParentFile().exists()) 
        {
            try 
            {
                file.createNewFile();
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }

        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (yamlConfiguration.getLocation("spawn") == null) 
        {
            yamlConfiguration.set("spawn", new Location(Bukkit.getWorlds().get(0), 100, 100, 100));

            this.saveConfig();
        }
    }

    public void saveConfig() 
    {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Location getLocation(String key) {

        final Location result = yamlConfiguration.getLocation(key);

        return result;
    }


    public void saveLocation(String key, Location location) {

        yamlConfiguration.set(key, location);

        this.saveConfig();
    }

}
