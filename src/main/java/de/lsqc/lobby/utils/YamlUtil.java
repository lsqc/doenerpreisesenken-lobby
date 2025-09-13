package de.lsqc.lobby.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.NonNull;

public final class YamlUtil 
{

    public static void writeLocation(@NonNull String identifier, @NonNull YamlConfiguration configuration, @NonNull Location location)
    {

        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        double yaw = location.getYaw();
        double pitch = location.getPitch();

        var locations = (Map<String, Object>) configuration.getMapList("locations");
        Map<String,Object> l = new HashMap<>();
        l.put("world", world);
        l.put("x", x);
        l.put("y", y);
        l.put("z", z);
        l.put("yaw", yaw);
        l.put("pitch", pitch);

        locations.put(identifier, l);
    }

    public static Location readLocation(@NonNull String identifier, @NonNull YamlConfiguration config)
    {
        var locs = (Map<String,Map<String,Object>>) config.getMapList("locations");
        var loc = (Map<String,Object>) locs.get(identifier);

        if (loc == null) return null;

        String world = loc.get("world").toString();
        double x = Double.parseDouble(loc.get("x").toString());
        double y = Double.parseDouble(loc.get("y").toString());
        double z = Double.parseDouble(loc.get("z").toString());
        double yaw = Double.parseDouble(loc.get("yaw").toString());
        double pitch = Double.parseDouble(loc.get("pitch").toString());

        Location location = new Location(Bukkit.getWorld(world), x, y, z);
        location.setYaw((float) yaw);
        location.setPitch((float) pitch);

        return location;
    }
	
}
