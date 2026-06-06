package de.lsqc.lobby.listeners;

import java.io.FileInputStream;
import java.util.*;

import com.lsdevcloud.cloud.api.core.CloudAPI;
import com.lsdevcloud.cloud.api.group.Group;
import com.lsdevcloud.cloud.api.service.Service;
import com.lsdevcloud.cloud.spigot.GameService;
import de.lsqc.lobby.utils.HeadUtil;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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

import de.lsqc.lobby.Lobby;
import de.lsqc.lobby.utils.VelocityUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.meta.SkullMeta;

public final class PlayerInteractListener<T extends Service> implements Listener
{

    public static final String NAVIGATION_INVENTORY_TITLE = "Navigation";
    public static final String LOBBY_SWITCHER_INVENTORY_TITLE = "Lobbies";
    public static final Material PLACEHOLDER_ITEM_TYPES[] = { Material.GREEN_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE };

    public static final Sound CLICK_SOUNDS[] = { Sound.ENTITY_CAT_PURREOW, Sound.BLOCK_GLASS_BREAK, Sound.BLOCK_STONE_PLACE, Sound.BLOCK_BREWING_STAND_BREW, Sound.ENTITY_GHAST_HURT, Sound.ENTITY_ENDERMAN_TELEPORT };

    @EventHandler
    @SneakyThrows
    public void onInteract(final PlayerInteractEvent event)
    {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        if (event.getItem().displayName().toString().contains("Navigator"))
        {
            Inventory inventory = Bukkit.createInventory(null, 3 * 9, NAVIGATION_INVENTORY_TITLE);

            int randomIndex = new Random().nextInt(PLACEHOLDER_ITEM_TYPES.length);

            event.getPlayer().getInventory().setItem(4, PlayerJoinQuitListener.randomNavigatorItem());

            ItemStack placeholder = new ItemStack(PLACEHOLDER_ITEM_TYPES[randomIndex]);
            ItemMeta meta = placeholder.getItemMeta();
            meta.displayName(Component.text("§a"));
            placeholder.setItemMeta(meta);

            for (int i = 0; i < inventory.getSize(); i++)
            {
                inventory.setItem(i, placeholder);
            }

            ItemStack survival = new ItemStack(Material.CAMPFIRE), spawn = HeadUtil.createCustomPlayerHeadFromUrl(HeadUtil.SPAWNER_TEXTURE_URL), tv = HeadUtil.createCustomPlayerHeadFromUrl(HeadUtil.TV_TEXTURE_URL);
            meta = survival.getItemMeta();
            meta.displayName(Component.text("Survival").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD));
            survival.setItemMeta(meta);

            meta = spawn.getItemMeta();
            meta.displayName(Component.text("Spawn").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD));
            spawn.setItemMeta(meta);

            meta = tv.getItemMeta();
            meta.displayName(Component.text("???").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD));
            tv.setItemMeta(meta);

            inventory.setItem(10, spawn);
            inventory.setItem(13, survival);
            inventory.setItem(16, tv);

            event.getPlayer().openInventory(inventory);
        }
        else if (Objects.requireNonNull(event.getItem().getItemMeta().displayName()).toString().contains("Lobbies"))
        {
            Inventory inventory = Bukkit.createInventory(null, 3 * 9, LOBBY_SWITCHER_INVENTORY_TITLE);
            CloudAPI cloudAPI = CloudAPI.getInstance();;

            List<Service> lobbyServices = new ArrayList<>(cloudAPI.getServiceProvider().getServices().values().stream().filter(service ->
            {
                Group group = cloudAPI.getGroupProvider().getGroups().get(service.getGroup());
                return group != null && group.isLobbyGroup() && cloudAPI.getServiceProvider().isOnline(service.getUniqueId());
            }).toList());

            Collections.sort(lobbyServices, (a,b) -> a.getId() - b.getId());
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("server.properties")) {
                props.load(fis);
            }
            for (int i = 0; i < lobbyServices.size(); i++)
            {
                var lobbyService = lobbyServices.get(i);
                var isCurrent = props.getProperty("server-name").equals(lobbyService.getName());

                ItemStack lobbyItem;
                ItemMeta meta;

                if (isCurrent)
                {
                    lobbyItem = new ItemStack(Material.PLAYER_HEAD);
                    meta = lobbyItem.getItemMeta();
                    ((SkullMeta) meta).setOwningPlayer(Bukkit.getOfflinePlayer(event.getPlayer().getUniqueId()));
                }
                else
                {
                    lobbyItem = HeadUtil.createCustomPlayerHeadFromUrl(HeadUtil.TV_TEXTURE_URL);
                    meta = lobbyItem.getItemMeta();
                    meta.setLocalizedName(lobbyService.getName());
                }

                meta.displayName(Component.text(lobbyService.getName()).decoration(TextDecoration.ITALIC, false));
                meta.lore(List.of(
                        Component.text(""),
                        Component.text("➜ ").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, isCurrent).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(isCurrent ? "You are here!" : "Click to join").color(TextColor.color(isCurrent ? 0x32A852 : 0xFFFF00)))
                ));

                lobbyItem.setItemMeta(meta);
                inventory.setItem(9 + i, lobbyItem);
            }
            event.getPlayer().openInventory(inventory);
        }
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event)
    {

        if (event.getCurrentItem() == null) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (event.getView().getTitle().equals(NAVIGATION_INVENTORY_TITLE))
        {
            event.setCancelled(true);
            int randomClickSoundIndex = new Random().nextInt(CLICK_SOUNDS.length);
            Sound s = CLICK_SOUNDS[randomClickSoundIndex];

            player.playSound(player.getLocation(), s, 1.0F, 1.0F);

            if (event.getCurrentItem().getType() == Material.CAMPFIRE)
            {
                player.sendMessage(Component.text("§8[§e*§8] ").append(Component.text("Connecting...").color(TextColor.color(0xc2f9ff))));
                VelocityUtils.sendPlayer(player, String.valueOf(Lobby.getInstance().getConfig().get("survival_server")));
                player.closeInventory();
            }
            else if (Objects.equals(event.getCurrentItem().getItemMeta().displayName(), Component.text("Spawn").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD)))
            {
                PlayerJoinQuitListener.teleportToSpawn(player);
                player.closeInventory();
            }
        }
        else if (event.getView().getTitle().equals(LOBBY_SWITCHER_INVENTORY_TITLE))
        {
            if (event.getCurrentItem().getType() != Material.PLAYER_HEAD && !event.getCurrentItem().getItemMeta().hasLocalizedName()) return;
            event.setCancelled(true);
            player.sendMessage(Component.text("§8[§e*§8] ").append(Component.text("Connecting to ").append(Component.text(event.getCurrentItem().getItemMeta().getDisplayName()).color(TextColor.color(NamedTextColor.AQUA)).append(Component.text("...").color(TextColor.color(NamedTextColor.GRAY))))));
            VelocityUtils.sendPlayer(player, event.getCurrentItem().getItemMeta().getLocalizedName());
            player.closeInventory();
        }
    }
}
