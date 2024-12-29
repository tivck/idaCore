package net.idalya;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;

public class API {


    private final JavaPlugin javaPlugin;
    private Jedis redis;
    private MongoDatabase mongoDatabase;

    public API(JavaPlugin plugin) {
        this.javaPlugin = plugin;
    }

    public void initData() {
        this.redis = new Jedis("localhost");

        MongoClient mongoClient = MongoClients.create("mongodb+srv://tick:cc123456@cluster0.zqf000m.mongodb.net/test");
        this.mongoDatabase = mongoClient.getDatabase("idalya");

        this.javaPlugin.getLogger().log(
                Level.INFO, "Les bases de données Redis/Mongo sont activés !"
        );
    }

    public Jedis getRedis() {
        return redis;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
