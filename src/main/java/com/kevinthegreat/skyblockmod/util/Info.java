package com.kevinthegreat.skyblockmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Info {
    public boolean hypixel = false;
    public boolean skyblock = false;
    public boolean catacombs = false;
    public boolean crystalHollows = false;

    public void update(MinecraftClient client) {
        if (client == null || client.world == null || client.isInSingleplayer()) {
            reset();
            return;
        }
        updateFromScoreboard(client);
    }

    private void updateFromScoreboard(MinecraftClient client) {
        Scoreboard scoreboard = client.world.getScoreboard();
        if (scoreboard == null) {
            reset();
            return;
        }
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(1);
        if (objective == null) {
            reset();
            return;
        }
        List<String> list = new ArrayList<>();
        for (ScoreboardPlayerScore score : scoreboard.getAllPlayerScores(objective)) {
            if (score == null || score.getPlayerName() == null || score.getPlayerName().startsWith("#")) {
                reset();
                return;
            }
            Team team = scoreboard.getPlayerTeam(score.getPlayerName());
            if (team == null) {
                reset();
                return;
            }
            String text = team.getPrefix().getString() + team.getSuffix().getString();
            if (text.trim().length() > 0) {
                list.add(text);
            }
        }
        list.add(objective.getDisplayName().getString());
        Collections.reverse(list);
        String scoreboardString = list.toString();
        if (list.get(list.size() - 1).equals("www.hypixel.net")) {
            hypixel = true;
            if (list.get(0).contains("SKYBLOCK")) {
                skyblock = true;
                catacombs = scoreboardString.contains("The Catacombs");
                crystalHollows = scoreboardString.contains("Precursor Remnants") || scoreboardString.contains("Khazad-dûm") || scoreboardString.contains("Jungle") || scoreboardString.contains("Mithril Deposits") || scoreboardString.contains("Goblin Holdout") || scoreboardString.contains("Goblin Queen's Den") || scoreboardString.contains("Lost Precursor City") || scoreboardString.contains("Crystal Nucleus") || scoreboardString.contains("Crystal Hollows") || scoreboardString.contains("Magma Fields") || scoreboardString.contains("Fairy Grotto") || scoreboardString.contains("Dragon's Lair");
            } else {
                skyblock = false;
                catacombs = false;
                crystalHollows = false;
            }
        } else {
            reset();
        }
    }

    private void reset(){
        hypixel = false;
        skyblock = false;
        catacombs = false;
        crystalHollows = false;
    }
}
