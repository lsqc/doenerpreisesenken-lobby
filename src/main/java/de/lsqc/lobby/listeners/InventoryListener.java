package de.lsqc.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InventoryListener implements Listener
{

    @EventHandler
    public void onOpen(final InventoryOpenEvent event)
    {

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, PotionEffect.INFINITE_DURATION, 10));
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 1));
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event)
    {
        event.getPlayer().removePotionEffect(PotionEffectType.NAUSEA);
        event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
    }
}
