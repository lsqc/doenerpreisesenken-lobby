package de.lsqc.lobby.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class PlayerInteractListener implements Listener
{

    public static final String NAVIGATION_INVENTORY_TITLE = "Navigation";
    public static final Material PLACEHOLDER_ITEM_TYPES[] = { Material.GREEN_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE };

    public static final Sound CLICK_SOUNDS[] = { Sound.ENTITY_CAT_PURREOW, Sound.BLOCK_GLASS_BREAK, Sound.BLOCK_STONE_PLACE, Sound.BLOCK_BREWING_STAND_BREW, Sound.ENTITY_GHAST_HURT, Sound.ENTITY_ENDERMAN_TELEPORT };

    @EventHandler
    public void onInteract(final PlayerInteractEvent event)
    {

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        if (!event.getItem().displayName().toString().contains("Navigator")) return;


        Inventory inventory = Bukkit.createInventory(null, 3*9, NAVIGATION_INVENTORY_TITLE);

        int randomIndex = new Random().nextInt(PLACEHOLDER_ITEM_TYPES.length);

        event.getPlayer().getInventory().setItem(4, PlayerJoinQuitListener.randomNavigatorItem());

        ItemStack placeholder = new ItemStack(PLACEHOLDER_ITEM_TYPES[randomIndex]);
        ItemMeta meta = placeholder.getItemMeta();
        meta.setDisplayName("§a");
        placeholder.setItemMeta(meta);

        for (int i = 0; i < inventory.getSize(); i++)
        {
            inventory.setItem(i, placeholder);
        }

        ItemStack survival = new ItemStack(Material.IRON_SWORD);
        meta = survival.getItemMeta();
        meta.displayName(Component.text("Survival").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD));
        survival.setItemMeta(meta);

        inventory.setItem(13, survival);

        event.getPlayer().openInventory(inventory);
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event)
    {

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(NAVIGATION_INVENTORY_TITLE)) return;

        int randomClickSoundIndex = new Random().nextInt(CLICK_SOUNDS.length);
        Sound s = CLICK_SOUNDS[randomClickSoundIndex];

        player.playSound(player.getLocation(), s, 1.0F, 1.0F);
    }
}
