package com.kevinthegreat.skyblockmod;

import com.kevinthegreat.skyblockmod.dungeons.DungeonMap;
import com.kevinthegreat.skyblockmod.dungeons.DungeonScore;
import com.kevinthegreat.skyblockmod.dungeons.LividColor;
import com.kevinthegreat.skyblockmod.dungeons.Reparty;
import com.kevinthegreat.skyblockmod.misc.Experiments;
import com.kevinthegreat.skyblockmod.misc.Fishing;
import com.kevinthegreat.skyblockmod.misc.QuiverWarning;
import com.kevinthegreat.skyblockmod.util.Commands;
import com.kevinthegreat.skyblockmod.util.Config;
import com.kevinthegreat.skyblockmod.util.Message;
import com.kevinthegreat.skyblockmod.util.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyblockMod implements ModInitializer {
    public static final String MOD_ID = "skyblockmod";
    public static final String MOD_NAME = "SkyblockMod";
    public static SkyblockMod skyblockMod;
    public final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public int tick = 0;

    public final Commands commands = new Commands();
    public final Config config = new Config();
    public final DungeonScore dungeonScore = new DungeonScore();
    public final DungeonMap dungeonMap = new DungeonMap();
    public final Experiments experiments = new Experiments();
    public final Fishing fishing = new Fishing();
    public final LividColor lividColor = new LividColor();
    public final Message message = new Message();
    public final QuiverWarning quiverWarning = new QuiverWarning();
    public final Reparty reparty = new Reparty();
    public final Util util = new Util();

    @Override
    public void onInitialize() {
        skyblockMod = this;
        config.load();
        registerEvents();
        LOGGER.info(MOD_NAME + " initialized.");
    }

    private void registerEvents() {
        ClientCommandRegistrationCallback.EVENT.register(commands::registerCommands);
        ClientLifecycleEvents.CLIENT_STOPPING.register(config::save);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        HudRenderCallback.EVENT.register(dungeonMap::render);
        ScreenEvents.AFTER_INIT.register(experiments::start);
    }

    private void tick(MinecraftClient minecraftClient) {
        if (tick % 20 == 0) {
            util.check(minecraftClient);
            quiverWarning.check(minecraftClient);
            tick = 0;
        }
        experiments.tick(minecraftClient);
        lividColor.tick(minecraftClient);
        message.tick();
        tick++;
    }
}
