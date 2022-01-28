package com.kevinthegreat.skyblockmod;

import com.kevinthegreat.skyblockmod.dungeons.DungeonMap;
import com.kevinthegreat.skyblockmod.dungeons.Reparty;
import com.kevinthegreat.skyblockmod.util.Config;
import com.kevinthegreat.skyblockmod.util.Message;
import com.kevinthegreat.skyblockmod.util.Util;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;

public class SkyblockMod implements ModInitializer {

    public static final String MOD_ID = "skyblockmod";
    public static final String MOD_NAME = "SkyblockMod";
    public static SkyblockMod skyblockMod;
    public int tick = 0;

    public final Config config = new Config();
    public final Message message = new Message();
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
        message.tick();
        tick++;
    }
}
