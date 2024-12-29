package net.idalya.newsb;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public abstract class IdaScoreboard {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private final List<Score> scores;

    private final HashMap<UUID, IdaScoreboard> map;


    public IdaScoreboard(ScoreboardManager scoreboardManager) {
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective(this.getName(), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(this.getName());
        scores = new ArrayList<>();
        map = new HashMap<>();
        setup();;
    }

    public abstract String getName();

    public abstract void setup();

    public void addLine(String str) {
        Score score = objective.getScore(str);
        score.setScore(this.scores.size() - 1);
        this.scores.add(score);
    }

    public void reload() {
        Iterator<UUID> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            Player player = Bukkit.getPlayer(iterator.next());
            if(player != null) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                apply(player);
            }

            return;
        }
    }

    public void apply(Player player) {
        if(player != null) {
            player.setScoreboard(this.scoreboard);
            this.map.put(player.getUniqueId(), this);
        }

        assert player != null;
        System.out.println("Scoreboard apply for " + player.getName());
    }

    public HashMap<UUID, IdaScoreboard> getMap() {
        return map;
    }
}
