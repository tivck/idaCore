package net.idalya.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.idalya.bungee.Bungee;
import net.idalya.core.player.IdaPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

import java.net.InetSocketAddress;

public class Listeners implements Listener {

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if(!event.getTag().equalsIgnoreCase("BungeeCord")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        String chann = input.readUTF();

        if(chann.equalsIgnoreCase("CreateInstance")) {
            int port = input.readInt();
            Bungee.getInstance().getProxy().getServers().put("uhc-" + port, Bungee.getInstance().getProxy().constructServerInfo("uhc-" + port, new InetSocketAddress(port), "uhc-" + port, false));
        }

        if(chann.equalsIgnoreCase("GetAllProxys")) {
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.write(Bungee.getInstance().getProxy().getOnlineCount());
        }
    }
}
