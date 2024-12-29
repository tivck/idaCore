package net.idalya.core.listeners;

import net.idalya.core.Core;
import net.idalya.core.player.IdaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

    private final Core core;

    public Listeners(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        IdaPlayer idaPlayer = getCore().getPlayerManager().get(player.getUniqueId());
        getCore().getPlayerManager().saveData(idaPlayer);
        event.setQuitMessage(null);
    }

    public Core getCore() {
        return core;
    }
}
