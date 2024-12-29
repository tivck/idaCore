package net.idalya.core.player;

import net.idalya.core.player.rank.Rank;
import org.bson.Document;

import java.util.UUID;

public class IdaPlayer {

    private UUID uuid;
    private Rank rank;
    private int money;
    private int coins;
    private int hosts;
    private int tickets;

    public IdaPlayer(UUID uuid, Rank rank, int money, int coins, int hosts, int tickets) {
        this.uuid = uuid;
        this.rank = rank;
        this.money = money;
        this.coins = coins;
        this.hosts = hosts;
        this.tickets = tickets;
    }

    public Document toBson() {
        return new Document()
                .append("uuid", this.uuid.toString())
                .append("rank", this.rank.getName())
                .append("money", this.money)
                .append("coins", this.coins)
                .append("hosts", this.hosts)
                .append("tickets", this.tickets);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Rank getRank() {
        return rank;
    }

    public int getMoney() {
        return money;
    }

    public int getCoins() {
        return coins;
    }

    public int getHosts() {
        return hosts;
    }

    public int getTickets() {
        return tickets;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setHosts(int hosts) {
        this.hosts = hosts;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }
}
