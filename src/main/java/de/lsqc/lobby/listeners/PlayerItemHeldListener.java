package de.lsqc.lobby.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerItemHeldListener implements Listener
{

  @EventHandler
  public void onItemSwitch(final PlayerItemHeldEvent event)
  {

    Player player = event.getPlayer();
    player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 2f);;
    
  }
}
