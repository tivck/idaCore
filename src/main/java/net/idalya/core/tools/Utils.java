package net.idalya.core.tools;

import com.google.common.base.Preconditions;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static ItemStack ITEM_HOST = new ItemBuilder(Material.PAINTING).setName("§f§lDashboard").toItemStack();
    public static ItemStack ITEM_NAVIGATION = new ItemBuilder(Material.NETHER_STAR).setName("§f§lNavigation").toItemStack();
    public static ItemStack ITEM_TEAMS = new ItemBuilder(Material.WOOL).setName("§f§léquipes").toItemStack();

    public static void clearPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        player.setLevel(0);
        player.setExp(0);
        player.setFoodLevel(20);
        player.getActivePotionEffects().forEach(
                potionEffect ->
                        player.removePotionEffect(potionEffect.getType())
        );
    }

    public static String getTime(int s) {
        int m = 0;
        int h = 0;

        while (s >= 60) {
            m++;
            s -= 60;
        }
        while (m >= 60) {
            h++;
            m -= 60;
        }

        String msg = "";
        if (h > 0) {
            if (h < 10) {
                msg += "0" + h + ":";
            } else {
                msg += h + ";";
            }
        }
        if (m < 10) {
            msg += "0" + m + ":";
        } else {
            msg += m + ":";
        }
        if (s < 10) {
            msg += "0" + s;
        } else {
            msg += s;
        }

        return msg;
    }

    public static String getArrow(Location from, Location to) {
        if (from != null && to != null) {
            if (!from.getWorld().getName().equals(to.getWorld().getName())) {
                return "?";
            } else {
                from.setY(0.0);
                to.setY(0.0);
                String[] arrows = new String[]{"⬆", "⬈", "➡", "⬊", "⬇", "⬋", "⬅", "⬉", "⬆"};
                Vector d = from.getDirection();
                Vector v = to.subtract(from).toVector().normalize();
                double a = Math.toDegrees(Math.atan2(d.getX(), d.getZ()));
                a -= Math.toDegrees(Math.atan2(v.getX(), v.getZ()));
                a = (double)((int)(a + 22.5) % 360);
                if (a < 0.0) {
                    a += 360.0;
                }

                return String.valueOf(arrows[(int)a / 45]);
            }
        } else {
            return "?";
        }
    }

    public static Player getTarget(Player player, int maxRange, double aiming, boolean wallHack) {
        Player target = null;
        double distance = 0.0;
        Location playerEyes = player.getEyeLocation();
        Vector direction = playerEyes.getDirection().normalize();
        List<Player> targets = new ArrayList();
        Iterator var11 = Bukkit.getOnlinePlayers().iterator();

        while(var11.hasNext()) {
            Player online = (Player)var11.next();
            if (online != player && online.getWorld().equals(player.getWorld()) && !(online.getLocation().distanceSquared(playerEyes) > (double)(maxRange * maxRange)) && !online.getGameMode().equals(GameMode.SPECTATOR)) {
                targets.add(online);
            }
        }

        if (targets.size() > 0) {
            Location loc = playerEyes.clone();
            Vector progress = direction.clone().multiply(0.7);
            maxRange = 100 * maxRange / 70;
            int loop = 0;

            while(loop < maxRange) {
                ++loop;
                loc.add(progress);
                Block block = loc.getBlock();
                if (!wallHack && block.getType().isSolid()) {
                    break;
                }

                double lx = loc.getX();
                double ly = loc.getY();
                double lz = loc.getZ();
                Iterator var21 = targets.iterator();

                while(var21.hasNext()) {
                    Player possibleTarget = (Player)var21.next();
                    if (possibleTarget != player) {
                        Location testLoc = possibleTarget.getLocation().add(0.0, 0.85, 0.0);
                        double px = testLoc.getX();
                        double py = testLoc.getY();
                        double pz = testLoc.getZ();
                        boolean dX = Math.abs(lx - px) < 0.7 * aiming;
                        boolean dY = Math.abs(ly - py) < 1.7 * aiming;
                        boolean dZ = Math.abs(lz - pz) < 0.7 * aiming;
                        if (dX && dY && dZ) {
                            target = possibleTarget;
                            break;
                        }
                    }
                }

                if (target != null) {
                    distance = (double)(loop * 70 / 100);
                    break;
                }
            }
        }

        return target != null ? target : null;
    }

    public static void respawnPlayer(Player player) {
        player.setNoDamageTicks(20*5);
        int b = (int) Bukkit.getWorld("world").getWorldBorder().getSize() - 5;
        int x = (new Random().nextInt(2) == 0 ? +1 : -1) * new Random().nextInt(  b / 2 );
        int z = (new Random().nextInt(2) == 0 ? +1 : -1) * new Random().nextInt(  b / 2 );
        int highestBlock = 100;
        player.teleport(new Location(Bukkit.getWorld("world"), x, highestBlock, z));
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static Location getRandomLocation(Location loc1, Location loc2) {
        Preconditions.checkArgument(loc1.getWorld() == loc2.getWorld());
        double minX = Math.min(loc1.getX(), loc2.getX());
        double minY = Math.min(loc1.getY(), loc2.getY());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());

        double maxX = Math.max(loc1.getX(), loc2.getX());
        double maxY = Math.max(loc1.getY(), loc2.getY());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());

        return new Location(loc1.getWorld (), randomDouble (minX, maxX), randomDouble (minY, maxY), randomDouble (minZ, maxZ));
    }

    public static double randomDouble(double min, double max) {
        return min + ThreadLocalRandom.current().nextDouble(Math.abs(max - min + 1));
    }

    public static void makePlayerSeePlayersHealthAboveHead(Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.getObjective("HP") == null ? scoreboard.registerNewObjective("HP", "health") : scoreboard.getObjective("HP");
        objective.setDisplayName(ChatColor.RED + "❤");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        player.setScoreboard(scoreboard);
    }

    public static void stopSeeHealthHead(Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = player.getScoreboard();
        player.getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
    }
}
