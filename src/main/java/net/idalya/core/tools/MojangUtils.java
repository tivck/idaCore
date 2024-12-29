package net.idalya.core.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.UUID;

public class MojangUtils {

    public static UUID getPlayerUuid(String name){
        if (Bukkit.isPrimaryThread()){
            throw new RuntimeException("Requesting player UUID is not allowed on the primary thread!");
        }

        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            JsonParser parser = new JsonParser();
            JsonElement json = parser.parse(new InputStreamReader(connection.getInputStream()));

            connection.disconnect();

            if (!json.isJsonObject()){
                return null;
            }

            String stringUuid = json.getAsJsonObject().get("id").getAsString();
            return insertDashUUID(stringUuid);
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static String getPlayerName(String name){
        if (Bukkit.isPrimaryThread()){
            throw new RuntimeException("Requesting player UUID is not allowed on the primary thread!");
        }

        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            JsonParser parser = new JsonParser();
            JsonElement json = parser.parse(new InputStreamReader(connection.getInputStream()));

            connection.disconnect();

            if (!json.isJsonObject()){
                return name;
            }

            return json.getAsJsonObject().get("name").getAsString();
        }catch (IOException ex){
            ex.printStackTrace();
            return name;
        }
    }

    public static String getName(String uuid) {
        String url = "https://api.mojang.com/user/profiles/"+uuid.replace("-", "")+"/names";
        try {
            @SuppressWarnings("deprecation")
            String nameJson = IOUtils.toString(new URL(url));
            JSONArray nameValue = (JSONArray) JSONValue.parse(nameJson);
            String playerSlot = nameValue.get(nameValue.size()-1).toString();
            JSONObject nameObject = (JSONObject) JSONValue.parse(playerSlot);
            return nameObject.get("name").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    private static UUID insertDashUUID(String uuid) {
        StringBuffer sb = new StringBuffer(uuid);
        sb.insert(8, "-");

        sb = new StringBuffer(sb.toString());
        sb.insert(13, "-");

        sb = new StringBuffer(sb.toString());
        sb.insert(18, "-");

        sb = new StringBuffer(sb.toString());
        sb.insert(23, "-");

        return UUID.fromString(sb.toString());
    }

}
