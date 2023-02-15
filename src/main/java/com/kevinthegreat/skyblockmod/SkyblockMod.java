package com.kevinthegreat.skyblockmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kevinthegreat.skyblockmod.dungeons.DungeonMap;
import com.kevinthegreat.skyblockmod.dungeons.DungeonScore;
import com.kevinthegreat.skyblockmod.dungeons.LividColor;
import com.kevinthegreat.skyblockmod.dungeons.Reparty;
import com.kevinthegreat.skyblockmod.misc.Experiments;
import com.kevinthegreat.skyblockmod.misc.FairySouls;
import com.kevinthegreat.skyblockmod.misc.Fishing;
import com.kevinthegreat.skyblockmod.misc.QuiverWarning;
import com.kevinthegreat.skyblockmod.util.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class SkyblockMod implements ModInitializer {
    public static final String MOD_ID = "skyblockmod";
    public static final String MOD_NAME = "SkyblockMod";
    public static SkyblockMod skyblockMod;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);
    public static final Path NEU_REPO_DIR = CONFIG_DIR.resolve("NEU-repo");
    public int tick = 0;

    public final Commands commands = new Commands();
    public final Config config = new Config();
    public final DungeonScore dungeonScore = new DungeonScore();
    public final DungeonMap dungeonMap = new DungeonMap();
    public final Experiments experiments = new Experiments();
    public final FairySouls fairySouls = new FairySouls();
    public final Fishing fishing = new Fishing();
    public final LividColor lividColor = new LividColor();
    public final Message message = new Message();
    public final NEURepo neuRepo = new NEURepo();
    public final QuiverWarning quiverWarning = new QuiverWarning();
    public final Reparty reparty = new Reparty();
    public final Info info = new Info();

    @Override
    public void onInitialize() {
        skyblockMod = this;
        config.load();
        fairySouls.loadFairySouls();
        registerEvents();
        LOGGER.info(MOD_NAME + " initialized.");
    }

    private void registerEvents() {
        ClientCommandRegistrationCallback.EVENT.register(commands::registerCommands);
        ClientLifecycleEvents.CLIENT_STOPPING.register(config::save);
        ClientLifecycleEvents.CLIENT_STOPPING.register(fairySouls::saveFoundFairySouls);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        WorldRenderEvents.AFTER_ENTITIES.register(fairySouls::render);
        HudRenderCallback.EVENT.register(dungeonMap::render);
        ScreenEvents.AFTER_INIT.register(experiments::start);
    }

    private void tick(MinecraftClient minecraftClient) {
        if (tick % 10 == 0) {
            info.update(minecraftClient);
            quiverWarning.check(minecraftClient);
            tick = 0;
        }
        lividColor.tick(minecraftClient);
        message.tick();
        tick++;
    }
}
