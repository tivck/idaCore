package net.idalya.core.commands;

import net.idalya.core.Core;
import net.idalya.core.commands.gui.RankGui;
import net.idalya.core.player.IdaPlayer;
import net.idalya.core.player.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        IdaPlayer iPlayer = Core.getInstance().getPlayerManager().get(player.getUniqueId());

        if(iPlayer.getRank().getPower() >= Rank.RESPONSABLE.getPower() || player.isOp()) {
            if(strings.length == 1) {
                Player target = Bukkit.getPlayer(strings[0]);
                if(target != null) {
                    IdaPlayer iTarget = Core.getInstance().getPlayerManager().get(target.getUniqueId());
                    RankGui.getInventory(iTarget).open(player);
                } else {
                    player.sendMessage("§cCe joueur n'est pas connecté !");
                }
            } else {
                player.sendMessage("§c/rank (joueur)");
            }
        } else {
            player.sendMessage("§cVous n'avez pas la permission");
        }
        return false;
    }
}
