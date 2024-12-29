package net.idalya.core.server;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.idalya.core.Core;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateServer implements PluginMessageListener {

    public static CreateServer instance;

    public void createServer(ServerType type, Player player) {
        int port = (int) (Math.random() * 200);

        while (port == 22 || port == 25) {
            port = (int) (Math.random() * 200);
        }

        int finalPort = port;
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
            copyDirectory("C:\\Users\\esper\\Desktop\\Idalya\\hosting\\templates\\" + type.getName(), "C:\\Users\\esper\\Desktop\\Idalya\\hosting\\servers\\uhc-" + finalPort);
            modifyServerProperties(type, new File("C:\\Users\\esper\\Desktop\\Idalya\\hosting\\servers\\uhc-" + finalPort), finalPort);
        });
        Bukkit.getScheduler().runTaskLaterAsynchronously(Core.getInstance(), () -> {
            try {
                Runtime.getRuntime().exec("cmd /c start.bat", null, new File("C:\\Users\\esper\\Desktop\\Idalya\\hosting\\servers\\uhc-" + finalPort));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5 * 20L);

        player.sendMessage("§8| §fVotre serveur est en cours de création, veuillez patientier... §5§l(20 SECONDES)");
        createInstance(player, finalPort);
        Core.getInstance().getServerManager().addOrRefreshServer(new IdaServer(ServerStatus.CREATING,
                player.getName(), type, finalPort, 30));

        new BukkitRunnable() {
            int number = 0;
            int timer = 0;

            @Override
            public void run() {
                if (player == null) {
                    cancel();
                    return;
                }

                if (Bukkit.getPlayer(player.getUniqueId()) == null) {
                    cancel();
                    return;
                }
                if (Core.getInstance().getServerManager().get(finalPort).getStatus().equals(ServerStatus.WAITING)) {
                    send(player, "uhc-" + finalPort);
                }

                number++;
                timer++;
            }
        }.runTaskTimerAsynchronously(Core.getInstance(), 0, 20);
    }

    public void modifyServerProperties(ServerType type, File path, int port) {

        File entree = new File("C:\\Users\\esper\\Desktop\\Idalya\\hosting\\templates\\" + type.getName() + "/server.properties");
        File sortie = new File(path.getAbsolutePath() + "/server.properties");
        try {
            BufferedReader br = new BufferedReader(new FileReader(entree));
            BufferedWriter bw = new BufferedWriter(new FileWriter(sortie));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("server-port")) {
                    bw.write("server-port=" + port + "\n");
                } else {
                    bw.write(line + "\n");
                }
                bw.flush();
            }
            bw.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) {
        try {
            Files.walk(Paths.get(sourceDirectoryLocation))
                    .forEach(source -> {
                        Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                                .substring(sourceDirectoryLocation.length()));
                        try {
                            Files.copy(source, destination);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createInstance(Player p, int port) {
        final ByteArrayDataOutput out =  ByteStreams.newDataOutput();
        out.writeUTF("CreateInstance");
        out.writeInt(port);
        p.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
    }

    public void send(final Player player, final String serverName) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static CreateServer getInstance() {
        if (instance == null)
            instance = new CreateServer();
        return instance;
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if(!s.equalsIgnoreCase("BungeeCord"))
            return;


    }
}
