package net.idalya.bungee;

import net.idalya.bungee.commands.HubCommand;
import net.idalya.bungee.listeners.Listeners;
import net.md_5.bungee.api.plugin.Plugin;

public class Bungee extends Plugin {

    public static Bungee instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        getProxy().registerChannel("CreateInstance");
        getProxy().getPluginManager().registerCommand(this, new HubCommand());
        getProxy().getPluginManager().registerListener(this, new Listeners());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static Bungee getInstance() {
        return instance;
    }
}
