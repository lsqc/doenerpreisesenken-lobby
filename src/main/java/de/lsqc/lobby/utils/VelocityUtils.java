package de.lsqc.lobby.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import de.lsqc.lobby.Lobby;

public class VelocityUtils 
{

    public static void sendPlayer(Player player, String server)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);
        try {
            dataOut.writeUTF("connect");
            dataOut.writeUTF(server);

        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        player.sendPluginMessage(Lobby.getInstance(), "velocity:player", out.toByteArray());
    }
}
