package net.idalya.core;

import fr.minuskube.inv.InventoryManager;
import net.idalya.API;
import net.idalya.core.commands.RankCommand;
import net.idalya.core.listeners.Listeners;
import net.idalya.core.player.manager.PlayerManager;
import net.idalya.core.player.notifications.NotificationManager;
import net.idalya.core.server.CreateServer;
import net.idalya.core.server.ServerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    public static Core instance;
    private API api;
    private PlayerManager playerManager;
    private InventoryManager inventoryManager;
    private ServerManager serverManager;
    private NotificationManager notificationManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        api = new API(this);
        api.initData();
        getCommand("rank").setExecutor(new RankCommand());
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", CreateServer.getInstance());
        playerManager = new PlayerManager(api);
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();
        serverManager = new ServerManager(this);
        notificationManager = new NotificationManager(this);
    }

    @Override
    public void onDisable() {

    }

    public static Core getInstance() {
        return instance;
    }

    public API getApi() {
        return api;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ServerManager getServerManager() {
       return serverManager;
   }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
}
