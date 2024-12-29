package net.idalya.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
        if(!proxiedPlayer.getServer().getInfo().getName().equals("lobby")) {
            proxiedPlayer.connect(ProxyServer.getInstance().getServerInfo("lobby"));
            proxiedPlayer.sendMessage("§cTéléportation au lobby en cours..");
        } else {
            proxiedPlayer.sendMessage("§cVous êtes déjà dans un lobby !");
        }
    }
}
