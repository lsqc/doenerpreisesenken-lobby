package de.lsqc.lobby.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class WorldProtectionListener implements Listener
{

	@EventHandler
    public void onPlace(final BlockPlaceEvent event)
    {
        event.setCancelled(cancel(event.getPlayer()));
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event)
    {

        event.setCancelled(cancel(event.getPlayer()));
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event)
    {

        event.setCancelled(cancel(event.getPlayer()));
    }

    @EventHandler
    public void onInteractAtEntity(final PlayerInteractAtEntityEvent event)
    {

        event.setCancelled(cancel(event.getPlayer()));
    }

    @EventHandler
    public void onEntityChangeBlockEvent(final EntityChangeBlockEvent event)
    {
        if (!(event.getEntity() instanceof Player player)) return;
        event.setCancelled(cancel(player));
    }

    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent event)
    {
        event.setCancelled(cancel(event.getPlayer()));
    }

    public static boolean cancel(Player player)
    {
        return player.getGameMode() != GameMode.CREATIVE;
    }
}
