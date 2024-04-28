package com.kevinthegreat.skyblockmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kevinthegreat.skyblockmod.dungeons.DungeonMap;
import com.kevinthegreat.skyblockmod.dungeons.DungeonScore;
import com.kevinthegreat.skyblockmod.dungeons.LividColor;
import com.kevinthegreat.skyblockmod.dungeons.Reparty;
import com.kevinthegreat.skyblockmod.misc.*;
import com.kevinthegreat.skyblockmod.option.SkyblockModOptions;
import com.kevinthegreat.skyblockmod.util.Commands;
import com.kevinthegreat.skyblockmod.util.Info;
import com.kevinthegreat.skyblockmod.util.Message;
import com.kevinthegreat.skyblockmod.util.NEURepo;
import com.kevinthegreat.skyblockmod.waypoint.Waypoints;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class SkyblockMod implements ModInitializer {
    public static final String MOD_ID = "skyblockmod";
    public static final String MOD_NAME = "Skyblock Mod";
    public static SkyblockMod skyblockMod;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Gson GSON_COMPACT = new GsonBuilder().create();
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);
    public static final Path NEU_REPO_DIR = CONFIG_DIR.resolve("NEU-repo");
    public static final SystemToast.Type TOAST_TYPE = new SystemToast.Type();

    public final Commands commands = new Commands();
    public final DungeonScore dungeonScore = new DungeonScore();
    public final DungeonMap dungeonMap = new DungeonMap();
    public final Experiments experiments = new Experiments();
    public final FairySouls fairySouls = new FairySouls();
    public final Fishing fishing = new Fishing();
    public final LividColor lividColor = new LividColor();
    public final Message message = new Message();
    public final MythologicalRitual mythologicalRitual = new MythologicalRitual();
    public final NEURepo neuRepo = new NEURepo();
    public final SkyblockModOptions options = new SkyblockModOptions();
    public final QuiverWarning quiverWarning = new QuiverWarning();
    public final Reparty reparty = new Reparty();
    public final Info info = new Info();
    private int tick = 0;
    private Screen nextScreen;

    public void setNextScreen(Screen nextScreen) {
        this.nextScreen = nextScreen;
    }

    @Override
    public void onInitialize() {
        skyblockMod = this;
        options.load();
        fairySouls.loadFairySouls();
        registerEvents();
        LOGGER.info(MOD_NAME + " initialized.");
    }

    private void registerEvents() {
        ClientCommandRegistrationCallback.EVENT.register(commands::registerCommands);
        ClientLifecycleEvents.CLIENT_STOPPING.register(options::save);
        ClientLifecycleEvents.CLIENT_STOPPING.register(fairySouls::saveFoundFairySouls);
        ClientPlayConnectionEvents.JOIN.register(info::onClientWorldJoin);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        WorldRenderEvents.AFTER_TRANSLUCENT.register(fairySouls::render);
        mythologicalRitual.init();
        HudRenderCallback.EVENT.register(dungeonMap::render);
        ClientReceiveMessageEvents.ALLOW_GAME.register(this::onChatMessage);
        ClientReceiveMessageEvents.GAME_CANCELED.register(this::onChatMessage);
        ScreenEvents.AFTER_INIT.register(experiments::start);
        Waypoints.init();
    }

    private void tick(MinecraftClient minecraftClient) {
        if (tick % 10 == 0) {
            info.update(minecraftClient);
            quiverWarning.check(minecraftClient);
            tick = 0;
        }
        lividColor.tick(minecraftClient);
        message.tick();
        if (nextScreen != null) {
            minecraftClient.setScreen(nextScreen);
            nextScreen = null;
        }
        tick++;
    }

    private boolean onChatMessage(Text text, boolean overlay) {
        String message = text.getString();
        return SkyblockMod.skyblockMod.dungeonScore.onChatMessage(message) && SkyblockMod.skyblockMod.fairySouls.onChatMessage(message) && SkyblockMod.skyblockMod.info.onChatMessage(message) && SkyblockMod.skyblockMod.lividColor.onChatMessage(message) && SkyblockMod.skyblockMod.quiverWarning.onChatMessage(message) && SkyblockMod.skyblockMod.reparty.onChatMessage(message);
    }
}
