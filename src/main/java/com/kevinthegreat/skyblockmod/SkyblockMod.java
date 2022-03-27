package com.kevinthegreat.skyblockmod;

import com.kevinthegreat.skyblockmod.dungeons.DungeonMap;
import com.kevinthegreat.skyblockmod.dungeons.DungeonScore;
import com.kevinthegreat.skyblockmod.dungeons.LividColor;
import com.kevinthegreat.skyblockmod.dungeons.Reparty;
import com.kevinthegreat.skyblockmod.util.Config;
import com.kevinthegreat.skyblockmod.util.Message;
import com.kevinthegreat.skyblockmod.util.Util;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyblockMod implements ModInitializer {

    public static final String MOD_ID = "skyblockmod";
    public static final String MOD_NAME = "SkyblockMod";
    public static SkyblockMod skyblockMod;
    public final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public int tick = 0;

    public final Config config = new Config();
    public final Message message = new Message();
    public final Util util = new Util();

    public final DungeonScore dungeonScore = new DungeonScore();
    public final DungeonMap dungeonMap = new DungeonMap();
    public final Fishing fishing = new Fishing();
    public final LividColor lividColor = new LividColor();
    public final ModifyMessage modifyMessage = new ModifyMessage();
    public final QuiverWarning quiverWarning = new QuiverWarning();
    public final Reparty reparty = new Reparty();

    @Override
    public void onInitialize() {
        skyblockMod = this;
        config.load();
        LOGGER.info(MOD_NAME + " initialized.");
    }

    public void tick() {
        if (tick % 20 == 0) {
            util.check();
            tick = 0;
        }
        message.tick();
        lividColor.tick();
        tick++;
    }
}
