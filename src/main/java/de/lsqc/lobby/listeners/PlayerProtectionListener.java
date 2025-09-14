package de.lsqc.lobby.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;

public class PlayerProtectionListener implements Listener
{

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event)
    {
        if (!(event.getEntity() instanceof Player player)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerExperiencePickup(final PlayerPickupExperienceEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(final FoodLevelChangeEvent event)
    {
        event.setCancelled(true);
        event.getEntity().setFoodLevel(20);
        event.getEntity().setSaturation(20);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event)
    {
        if (event.getWhoClicked() instanceof Player player) event.setCancelled(WorldProtectionListener.cancel(player));
    }
}
