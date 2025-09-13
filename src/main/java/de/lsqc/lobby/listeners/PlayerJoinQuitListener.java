package de.lsqc.lobby.listeners;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.lsqc.lobby.Lobby;
import lombok.NonNull;

public class PlayerJoinQuitListener implements Listener
{

    @EventHandler
    public void onJoin(final PlayerJoinEvent event)
    {
        event.setJoinMessage("§a» §e" + event.getPlayer().getDisplayName());

        this.join(event.getPlayer());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event)
    {
        event.setQuitMessage("§c« §e" + event.getPlayer().getDisplayName());
    }

    public void join(@NonNull Player player)
    {
        player.getInventory().clear();

        player.setGameMode(GameMode.SURVIVAL);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        player.setLevel(year);
        player.setExp(1);

        player.getInventory().setHeldItemSlot(4);

        Location spawnLocation = Lobby.getInstance().getLocationManager().getLocation("spawn");

        if (spawnLocation != null)
        {
            Bukkit.getRegionScheduler().execute(Lobby.getInstance(), player.getLocation(), () -> {
                player.teleportAsync(spawnLocation);
            });
        }
    }
}
