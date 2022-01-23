package com.kevinthegreat.skyblockmod;

import com.kevinthegreat.skyblockmod.util.Config;
import com.kevinthegreat.skyblockmod.util.Util;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.LogManager;

import java.util.LinkedList;
import java.util.Queue;

public class SkyblockMod implements ModInitializer {

    public static final String MOD_ID = "skyblockmod";
    public static final String MOD_NAME = "SkyblockMod";
    public static SkyblockMod skyblockMod;
    public int tick = 0;
    public Queue<Pair<String, Integer>> messageQueue = new LinkedList<>();

    public final Config config = new Config();
    public final Util util = new Util();
    public final DungeonMap dungeonMap = new DungeonMap();
    public final Fishing fishing = new Fishing();
    public final ModifyMessage modifyMessage = new ModifyMessage();
    public final Reparty reparty = new Reparty();

    @Override
    public void onInitialize() {
        skyblockMod = this;
        config.load();
        LogManager.getLogger().info(MOD_NAME + " initialized.");
    }

    public void tick() {
        if (tick % 20 == 0) {
            util.check();
            tick = 0;
        }
        if (!messageQueue.isEmpty()) {
            messageQueue.peek().setRight(messageQueue.peek().getRight() - 1);
            assert messageQueue.peek() != null;
            if (messageQueue.peek().getRight() <= 0) {
                assert MinecraftClient.getInstance().player != null;
                MinecraftClient.getInstance().player.sendChatMessage(messageQueue.poll().getLeft()); //messageQueue.peek() already asserted
            }
        }
        tick++;
    }
}
