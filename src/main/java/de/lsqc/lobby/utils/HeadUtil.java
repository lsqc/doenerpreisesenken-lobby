package de.lsqc.lobby.utils;

import de.lsqc.lobby.Lobby;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

/**
 * Created: 1/31/26
 */
public class HeadUtil
{

    public static final String
            TV_TEXTURE_URL = "http://textures.minecraft.net/texture/5f4307870414885b0e8af76f003377036e8adcace58b3931ddb22790d209629f",
            SPAWNER_TEXTURE_URL = "http://textures.minecraft.net/texture/4dc0f6f4b81ed77abba5c88526f55b46e5a7d88b8c0935d8dd2ec22875587981";

    @SneakyThrows
    public static ItemStack createCustomPlayerHead(@NonNull String base64Texture)
    {

        PlayerProfile profile = getProfile(getUrlFromBase64(base64Texture).toString());

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwnerProfile(profile);

        skull.setItemMeta(skullMeta);

        return skull;
    }

    // stolen (>w<) https://blog.jeff-media.com/creating-custom-heads-in-spigot-1-18-1/
    @SneakyThrows
    private static PlayerProfile getProfile(String url)
    {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        URL urlObject = null;
        try
        {
            urlObject = new URL(url);
        }
        catch (MalformedURLException exception)
        {
            Lobby.getInstance().getLogger().info("Could not create profile from url: " + url);
        }

        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }

    public static URL getUrlFromBase64(String base64) throws MalformedURLException
    {
        String decoded = new String(Base64.getDecoder().decode(base64));
        return new URL(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length()));
    }

    @SneakyThrows
    public static ItemStack createCustomPlayerHeadFromUrl(@NonNull String url)
    {
        PlayerProfile profile = getProfile(url);

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwnerProfile(profile);

        skull.setItemMeta(skullMeta);

        return skull;
    }
}
