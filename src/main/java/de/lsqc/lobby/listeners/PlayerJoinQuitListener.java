package de.lsqc.lobby.listeners;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import de.lsqc.lobby.Lobby;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public final class PlayerJoinQuitListener implements Listener
{

    // materials than can be used for the navigator item
    public static final Material[] NAVIGATOR_MATERIALS = { Material.PRISMARINE_SHARD, Material.BEACON, Material.COMPASS, Material.MELON_SLICE, Material.COMPARATOR, Material.GLISTERING_MELON_SLICE };
    public static final String[] NAVIGATOR_COLORS = { "#eb4034", "#0ca2ed", "#a38f1c", "#d94711", "#100669", "#0cc938" };

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

        double randomHealth = 1 + new Random().nextInt(20);

        player.setMaxHealth(randomHealth);
        player.setHealth(randomHealth);

        player.getInventory().setHeldItemSlot(4);

        int randomItemIndex = new Random().nextInt(NAVIGATOR_MATERIALS.length);
        int randomColorIndex = new Random().nextInt(NAVIGATOR_COLORS.length);

        // navigator item 
        ItemStack navigator = new ItemStack(NAVIGATOR_MATERIALS[randomItemIndex]);
        ItemMeta meta = navigator.getItemMeta();
        meta.displayName(
                Component.text("Navigator").decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).color(TextColor.fromHexString(NAVIGATOR_COLORS[randomColorIndex]))
                .append(Component.text("§r ● ").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.BOLD, false)
                    .append(Component.text("(rechtsklick)").color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC)))
        );
        meta.setEnchantable(1);

        navigator.addUnsafeEnchantment(Enchantment.EFFICIENCY, 1); // TODO fix
        navigator.setItemMeta(meta);

        player.getInventory().setItem(4, navigator);

        Location spawnLocation = Lobby.getInstance().getLocationManager().getLocation("spawn");

        if (spawnLocation != null)
        {
            Bukkit.getRegionScheduler().execute(Lobby.getInstance(), player.getLocation(), () -> {
                player.teleportAsync(spawnLocation);
            });
        }
    }
}
