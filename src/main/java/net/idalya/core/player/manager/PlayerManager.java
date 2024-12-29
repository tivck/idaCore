package net.idalya.core.player.manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.idalya.API;
import net.idalya.core.Core;
import net.idalya.core.player.IdaPlayer;
import net.idalya.core.player.notifications.Notification;
import net.idalya.core.player.notifications.NotificationType;
import net.idalya.core.player.rank.Rank;
import org.bson.Document;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import javax.print.Doc;
import java.util.*;

public class PlayerManager {

    private final API api;
    private final List<IdaPlayer> playersCache;

    public PlayerManager(API api) {
        this.api = api;
        this.playersCache = new ArrayList<>();
    }



    public IdaPlayer get(UUID uuid) {
        Iterator<? extends IdaPlayer> iterator = getPlayersCache().iterator();
        while (iterator.hasNext()) {
           IdaPlayer next = iterator.next();
           if(next.getUuid().equals(uuid))
               return next;
        }

        return null;
    }

    public IdaPlayer fromDocument(Document document) {
        return new IdaPlayer(UUID.fromString(document.getString("uuid")),
                Rank.getByName(document.getString("rank")),
                document.getInteger("money"),
                document.getInteger("coins"),
                document.getInteger("hosts"),
                document.getInteger("tickets"));
    }

    public boolean registeredInMongo(UUID uuid) {
        MongoCollection<Document> accountsCollections = api.getMongoDatabase().getCollection("accounts");
        Document document = accountsCollections.find().filter(
                Filters.eq("uuid", uuid.toString())).first();
        if(document == null) {
            return false;
        }

        return true;
    }

    public void saveCache(IdaPlayer idaPlayer) {
        if(idaPlayer == null)
            return;
        try {
            this.getApi().getRedis().hset("accounts", idaPlayer.getUuid().toString(), idaPlayer.toBson().toJson());
        } catch (JedisException e) {
            e.printStackTrace();
        } finally {
            System.out.println("CACHE SAVED FOR" + idaPlayer.getUuid().toString());
        }

    }

    public void loadPlayer(UUID uuid) {
        IdaPlayer idaPlayer;

        MongoCollection<Document> accounts = this.getApi().getMongoDatabase().getCollection("accounts");
        Document document = accounts.find().filter(Filters.eq("uuid", uuid.toString())).first();

        if(document == null) {
            idaPlayer = new IdaPlayer(uuid, Rank.JOUEUR, 0, 0, 0, 0);
        } else {
            idaPlayer = this.fromDocument(document);
        }

        assert idaPlayer != null;
        this.playersCache.add(idaPlayer);

        this.saveCache(idaPlayer);
    }

    public void saveData(IdaPlayer idaPlayer) {
        if(idaPlayer == null)
            return;

        Document document = null;
        try {
            document = Document.parse(this.getApi().getRedis().hget("accounts", idaPlayer.getUuid().toString()));
        } catch (JedisException e) {
            e.printStackTrace();
        }

        if(document == null)
            return;

        MongoCollection<Document> accounts = this.api.getMongoDatabase().getCollection("accounts");
        Document playerAccount = accounts.find().filter(Filters.eq("uuid", idaPlayer.getUuid().toString())).first();
        if(playerAccount == null) {
            accounts.insertOne(document);
        } else {
            accounts.replaceOne(playerAccount, document);
        }

        saveCache(idaPlayer);
    }

    public API getApi() {
        return api;
    }

    public List<IdaPlayer> getPlayersCache() {
        return playersCache;
    }
}
