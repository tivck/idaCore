package net.idalya.core.player.rank;

import org.bukkit.ChatColor;
import redis.clients.jedis.Jedis;

public enum Rank {

    ADMIN("Admin", "§c", "ADMIN", 100, "§a§cRank"),
    RESPONSABLE("Responsable", "§6", "RESP", 90, "§b§cRank"),
    DEV("Développeur", "§e", "DEV", 80, "§c§cRank"),
    MODPLUS("Mod+", "§b", "MOD+", 75,"§dRank"),
    MOD("Modérateur", "§b", "MOD", 70, "§e§cRank"),
    STAFF("Equipe", "§a", "EQUIPE", 60, "§f§cRank"),
    HOST("Host", "§5", "HOST", 50, "§g§cRank"),
    AMI("Ami", "§d", "AMI", 40, "§h§cRank"),
    JOUEUR("Joueur", "§7", "", 1, "§i§cRank");

    private String name;
    private String color;
    private String prefix;
    private int power;
    private String order;

    Rank(String name, String color, String prefix, int power, String order) {
        this.name = name;
        this.color = color;
        this.prefix = prefix;
        this.power = power;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public String getColor() {
        return color;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getOrder() {
        return order;
    }

    public static Rank getByName(String name) {
        for(Rank rank : Rank.values()) {
            if(rank.getName().equals(name)) {
                return rank;
            }
        }
        return null;
    }
}
