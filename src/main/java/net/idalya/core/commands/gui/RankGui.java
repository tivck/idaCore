package net.idalya.core.commands.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.idalya.core.Core;
import net.idalya.core.player.IdaPlayer;
import net.idalya.core.player.rank.Rank;
import net.idalya.core.tools.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class RankGui implements InventoryProvider {

    private final IdaPlayer idaPlayer;

    public RankGui(IdaPlayer idaPlayer) {
        this.idaPlayer = idaPlayer;
    }

    public static SmartInventory getInventory(IdaPlayer idaPlayer) {
        return SmartInventory.builder()
                .provider(new RankGui(idaPlayer))
                .title("Changement de grade")
                .size(3, 9)
                .manager(Core.getInstance().getInventoryManager())
                .id("grade")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        for(Rank rank : Rank.values()) {
            contents.add(ClickableItem.of(new ItemBuilder(Material.STRING).setName(rank.getColor() + rank.getName()).toItemStack(), event -> {
                player.closeInventory();
                getIdaPlayer().setRank(rank);
                Core.getInstance().getPlayerManager().saveCache(getIdaPlayer());
                player.sendMessage("§cVous avez bien changé le grade de " + Bukkit.getPlayer(getIdaPlayer().getUuid()).getName() + " en " + rank.getName());
            }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public IdaPlayer getIdaPlayer() {
        return idaPlayer;
    }
}
