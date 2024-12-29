package net.idalya.core.server;

import net.idalya.core.Core;
import org.bson.Document;
import org.bukkit.event.inventory.ClickType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

public class ServerManager {

    private final Core core;


    public ServerManager(Core core) {
        this.core = core;
    }

    public void addOrRefreshServer(IdaServer server) {
        Jedis jedis = this.getCore().getApi().getRedis();

        try {
            jedis.hset("servers", "uhc" + server.getPort(), server.toBson().toJson());
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    public void removeServer(int port) {
        Jedis jedis = this.getCore().getApi().getRedis();

        try {
            jedis.hdel("servers", "uhc" + port);
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    public List<IdaServer> getServers() {
        Jedis jedis = this.getCore().getApi().getRedis();
        Throwable var2 = null;

        try {
            List<IdaServer> serversList = new ArrayList();
            Iterator<Map.Entry<String, String>> var4 = jedis.hgetAll("servers").entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, String> entry = var4.next();
                serversList.add(getServerFromDocument(Document.parse(entry.getValue())));
            }

            return serversList;
        } catch (Throwable var13) {
            var2 = var13;
            throw var13;
        } finally {
            if (jedis != null) {
                if (var2 != null) {
                    try {
                        jedis.close();
                    } catch (Throwable var12) {
                        var2.addSuppressed(var12);
                    }
                } else {
                    jedis.close();
                }
            }

        }
    }

    public IdaServer get(int port) {
        Jedis jedis = this.getCore().getApi().getRedis();

        Document document = null;
        try {
            document = Document.parse(jedis.hget("servers", "uhc" + port));
        } catch (JedisException e) {
            e.printStackTrace();
        }

        if (document != null)
            return this.getServerFromDocument(document);

        return null;
    }

    public IdaServer getServerFromDocument(Document document) {
        return new IdaServer(ServerStatus.valueOf(document.getString("status")),
                document.getString("host"),
                ServerType.getByName(document.getString("type")),
                document.getInteger("port"),
                document.getInteger("slots"));
    }

    public Core getCore() {
        return core;
    }
}
