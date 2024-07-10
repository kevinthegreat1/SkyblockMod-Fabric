package com.kevinthegreat.skyblockmod.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Info implements ChatListener {
    private static final String PROFILE_PREFIX = "§r§e§lProfile: §r§a";
    public static final boolean SKYBLOCKER_LOADED = FabricLoader.getInstance().isModLoaded("skyblocker");
    public boolean hypixel = false;
    public boolean skyblock = false;
    public boolean catacombs = false;
    public boolean crystalHollows = false;
    public String profile = "";
    public String server = "";
    public String gameType = "";
    public String locationRaw = "";
    public String map = "";
    private long clientWorldJoinTime = 0;
    private boolean sentLocRaw = false;
    private long lastLocRaw = 0;

    public void onClientWorldJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        clientWorldJoinTime = System.currentTimeMillis();
        resetLocRawInfo();
    }

    public void update(MinecraftClient client) {
        if (client == null || client.isInSingleplayer()) {
            resetScoreboardInfo();
            return;
        }
        updateFromPlayerList(client);
        updateFromScoreboard(client);
        updateLocRaw();
    }

    private void updateFromPlayerList(MinecraftClient client) {
        if (client.getNetworkHandler() == null) {
            return;
        }
        for (PlayerListEntry playerListEntry : client.getNetworkHandler().getPlayerList()) {
            if (playerListEntry.getDisplayName() == null) {
                continue;
            }
            String name = playerListEntry.getDisplayName().getString();
            if (name.startsWith(PROFILE_PREFIX)) {
                profile = name.substring(PROFILE_PREFIX.length());
            }
        }
    }

    private void updateFromScoreboard(MinecraftClient client) {
        if (client.world == null) {
            resetScoreboardInfo();
            return;
        }
        Scoreboard scoreboard = client.world.getScoreboard();
        if (scoreboard == null) {
            resetScoreboardInfo();
            return;
        }
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.FROM_ID.apply(1));
        if (objective == null) {
            resetScoreboardInfo();
            return;
        }
        List<String> list = new ArrayList<>();
        for (ScoreHolder scoreHolder : scoreboard.getKnownScoreHolders()) {
            if (scoreboard.getScoreHolderObjectives(scoreHolder).containsKey(objective)) {
                Team team = scoreboard.getScoreHolderTeam(scoreHolder.getNameForScoreboard());
                if (team == null) {
                    resetScoreboardInfo();
                    return;
                }
                String text = team.getPrefix().getString() + team.getSuffix().getString();
                if (!text.trim().isEmpty()) {
                    list.add(text);
                }
            }
        }
        list.add(objective.getDisplayName().getString());
        Collections.reverse(list);
        String scoreboardString = list.toString();
        if (list.contains("www.hypixel.net")) {
            hypixel = true;
            if (list.get(0).contains("SKYBLOCK") || list.get(0).contains("SKIBLOCK")) {
                skyblock = true;
                catacombs = scoreboardString.contains("The Catacombs");
                crystalHollows = scoreboardString.contains("Precursor Remnants") || scoreboardString.contains("Khazad-dûm") || scoreboardString.contains("Jungle") || scoreboardString.contains("Mithril Deposits") || scoreboardString.contains("Goblin Holdout") || scoreboardString.contains("Goblin Queen's Den") || scoreboardString.contains("Lost Precursor City") || scoreboardString.contains("Crystal Nucleus") || scoreboardString.contains("Crystal Hollows") || scoreboardString.contains("Magma Fields") || scoreboardString.contains("Fairy Grotto") || scoreboardString.contains("Dragon's Lair");
            } else {
                skyblock = false;
                catacombs = false;
                crystalHollows = false;
            }
        } else {
            resetScoreboardInfo();
        }
    }

    private void updateLocRaw() {
        if (hypixel) {
            long currentTime = System.currentTimeMillis();
            if (!sentLocRaw && currentTime > clientWorldJoinTime + 1000 && currentTime > lastLocRaw + 15000) {
                SkyblockMod.skyblockMod.message.sendMessageAfterCooldown("/locraw");
                sentLocRaw = true;
                lastLocRaw = currentTime;
            }
        } else {
            resetLocRawInfo();
        }
    }

    @Override
    public boolean onChatMessage(String message) {
        if (message.startsWith("{\"server\":") && message.endsWith("}")) {
            JsonObject locRaw = JsonParser.parseString(message).getAsJsonObject();
            if (locRaw.has("server")) {
                server = locRaw.get("server").getAsString();
                if (locRaw.has("gameType")) {
                    gameType = locRaw.get("gameType").getAsString();
                }
                if (locRaw.has("mode")) {
                    locationRaw = locRaw.get("mode").getAsString();
                }
                if (locRaw.has("map")) {
                    map = locRaw.get("map").getAsString();
                }
                return !sentLocRaw;
            }
        }
        return true;
    }

    private void resetScoreboardInfo() {
        hypixel = false;
        skyblock = false;
        catacombs = false;
        crystalHollows = false;
    }

    private void resetLocRawInfo() {
        sentLocRaw = false;
        server = "";
        gameType = "";
        locationRaw = "";
        map = "";
    }
}
