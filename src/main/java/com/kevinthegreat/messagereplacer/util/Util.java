package com.kevinthegreat.messagereplacer.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//from skyblocker / skyfabric
public class Util {
    public boolean skyblock = false;
    public boolean catacombs = false;

    public void check() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.world == null || minecraftClient.isInSingleplayer()) {
            return;
        }
        Scoreboard scoreboard = minecraftClient.world.getScoreboard();
        if (scoreboard == null) {
            return;
        }
        ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(1);
        if (scoreboardObjective == null) {
            return;
        }
        Collection<ScoreboardPlayerScore> scores = scoreboard.getAllPlayerScores(scoreboardObjective);
        List<ScoreboardPlayerScore> scoreList = scores.stream().filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName().startsWith("#")).collect(Collectors.toList());
        if (scoreList.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(scoreList, scores.size() - 15));
        } else {
            scores = scoreList;
        }
        List<String> list = new ArrayList<>();
        for (ScoreboardPlayerScore score : scores) {
            Team team = scoreboard.getPlayerTeam(score.getPlayerName());
            if (team == null) {
                return;
            }
            String text = team.getPrefix().getString() + team.getSuffix().getString();
            if (text.trim().length() > 0) {
                list.add(text);
            }
        }
        list.add(scoreboardObjective.getDisplayName().getString());
        Collections.reverse(list);
        String scoreboardString = list.toString();
        if (list.get(list.size() - 1).equals("www.hypixel.net")) {
            if (list.get(0).contains("SKYBLOCK")) {
                skyblock = true;
                catacombs = scoreboardString.contains("The Catacombs");
//                crystalHollows = scoreboardString.contains("Precursor Remnants") || scoreboardString.contains("Khazad-dûm") || scoreboardString.contains("Jungle") || scoreboardString.contains("Mithril Deposits") || scoreboardString.contains("Goblin Holdout") || scoreboardString.contains("Goblin Queen's Den") || scoreboardString.contains("Lost Precursor City") || scoreboardString.contains("Crystal Nucleus") || scoreboardString.contains("Crystal Hollows") || scoreboardString.contains("Magma Fields") || scoreboardString.contains("Fairy Grotto") || scoreboardString.contains("Dragon's Lair");
            } else {
                skyblock = false;
                catacombs = false;
            }
        }
    }
}
