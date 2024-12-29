package net.idalya.core.player.notifications;

import net.idalya.core.Core;
import net.idalya.core.player.IdaPlayer;
import org.bson.Document;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

public class NotificationManager {

    private final Core core;

    public NotificationManager(Core core) {
        this.core = core;
    }

    public void registerNotification(Notification notification) {
        Jedis jedis = Core.getInstance().getApi().getRedis();

        try {
            jedis.hset("notifications:" + notification.getReceiver().toString(), "notif" + notification.getId(), notification.toBson().toJson());
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    public void unregisterNotification(Notification notification) {
        Jedis jedis = Core.getInstance().getApi().getRedis();

        try {
            jedis.hdel("notifications:" + notification.getReceiver().toString(), "notif" + notification.getId());
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getNotifications(IdaPlayer idaPlayer) {
        Jedis jedis = this.getCore().getApi().getRedis();
        Throwable var2 = null;

        try {
            List<Notification> notifList = new ArrayList();
            Iterator<Map.Entry<String, String>> var4 = jedis.hgetAll("notifications:" + idaPlayer.getUuid().toString()).entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, String> entry = var4.next();
                notifList.add(Notification.fromDocument(Document.parse(entry.getValue())));
            }

            return notifList;
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

    public Core getCore() {
        return core;
    }
}
