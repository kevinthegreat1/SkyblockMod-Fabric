package com.kevinthegreat.skyblockmod.waypoint;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.mojang.brigadier.Command;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Waypoints {
    public static final Logger LOGGER = LoggerFactory.getLogger(Waypoints.class);
    private static final Codec<List<WaypointCategory>> CODEC = WaypointCategory.CODEC.listOf();
    private static final Codec<List<WaypointCategory>> SKYTILS_CODEC = WaypointCategory.SKYTILS_CODEC.listOf();
    private static final Path waypointsFile = FabricLoader.getInstance().getConfigDir().resolve(SkyblockMod.MOD_ID).resolve("waypoints.json");
    static final Multimap<String, WaypointCategory> waypoints = MultimapBuilder.hashKeys().arrayListValues().build();

    public static void init() {
        loadWaypoints();
        ClientLifecycleEvents.CLIENT_STOPPING.register(Waypoints::saveWaypoints);
        WorldRenderEvents.AFTER_TRANSLUCENT.register(Waypoints::render);
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("sbm").then(literal("waypoints").executes(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            SkyblockMod.skyblockMod.setNextScreen(new WaypointsScreen(client.currentScreen));
            return Command.SINGLE_SUCCESS;
        }))));
    }

    public static void loadWaypoints() {
        waypoints.clear();
        try (BufferedReader reader = Files.newBufferedReader(waypointsFile)) {
            List<WaypointCategory> waypoints = CODEC.parse(JsonOps.INSTANCE, SkyblockMod.GSON.fromJson(reader, JsonArray.class)).resultOrPartial(LOGGER::error).orElseThrow();
            waypoints.forEach(waypointCategory -> Waypoints.waypoints.put(waypointCategory.island(), waypointCategory));
        } catch (Exception e) {
            LOGGER.error("[Skyblocker Waypoints] Encountered exception while loading waypoints", e);
        }
    }

    public static List<WaypointCategory> fromSkytilsBase64(String base64, String defaultIsland) {
        try {
            if (base64.startsWith("<Skytils-Waypoint-Data>(V")) {
                int version = Integer.parseInt(base64.substring(26, base64.indexOf(')')));
                if (version == 1) {
                    return fromSkytilsJson(new String(Base64.getDecoder().decode(base64.substring(base64.indexOf(':') + 1))), defaultIsland);
                } else {
                    LOGGER.error("[Skyblocker Waypoints] Unknown Skytils waypoint data version: " + version);
                }
            } else return fromSkytilsJson(new String(Base64.getDecoder().decode(base64)), defaultIsland);
        } catch (NumberFormatException e) {
            LOGGER.error("[Skyblocker Waypoints] Encountered exception while parsing Skytils waypoint data version", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("[Skyblocker Waypoints] Encountered exception while decoding Skytils waypoint data", e);
        }
        return Collections.emptyList();
    }

    public static List<WaypointCategory> fromSkytilsJson(String waypointCategories, String defaultIsland) {
        JsonArray waypointCategoriesJson;
        try {
            waypointCategoriesJson = SkyblockMod.GSON.fromJson(waypointCategories, JsonObject.class).getAsJsonArray("categories");
        } catch (JsonSyntaxException e) {
            JsonObject waypointCategoryJson = new JsonObject();
            waypointCategoryJson.addProperty("name", "New Category");
            waypointCategoryJson.addProperty("island", defaultIsland);
            waypointCategoryJson.add("waypoints", SkyblockMod.GSON.fromJson(waypointCategories, JsonArray.class));
            waypointCategoriesJson = new JsonArray();
            waypointCategoriesJson.add(waypointCategoryJson);
        }
        return SKYTILS_CODEC.parse(JsonOps.INSTANCE, waypointCategoriesJson).resultOrPartial(LOGGER::error).orElseThrow();
    }

    public static String toSkytilsBase64(List<WaypointCategory> waypointCategories) {
        return Base64.getEncoder().encodeToString(toSkytilsJson(waypointCategories).getBytes());
    }

    public static String toSkytilsJson(List<WaypointCategory> waypointCategories) {
        JsonObject waypointCategoriesJson = new JsonObject();
        waypointCategoriesJson.add("categories", SKYTILS_CODEC.encodeStart(JsonOps.INSTANCE, waypointCategories).resultOrPartial(LOGGER::error).orElseThrow());
        return SkyblockMod.GSON_COMPACT.toJson(waypointCategoriesJson);
    }

    public static void saveWaypoints(MinecraftClient client) {
        try (BufferedWriter writer = Files.newBufferedWriter(waypointsFile)) {
            JsonElement waypointsJson = CODEC.encodeStart(JsonOps.INSTANCE, List.copyOf(waypoints.values())).resultOrPartial(LOGGER::error).orElseThrow();
            SkyblockMod.GSON.toJson(waypointsJson, writer);
            LOGGER.info("[Skyblocker Waypoints] Saved waypoints");
        } catch (Exception e) {
            LOGGER.error("[Skyblocker Waypoints] Encountered exception while saving waypoints", e);
        }
    }

    public static Multimap<String, WaypointCategory> waypointsDeepCopy() {
        return waypoints.values().stream().map(WaypointCategory::deepCopy).collect(Multimaps.toMultimap(WaypointCategory::island, Function.identity(), () -> MultimapBuilder.hashKeys().arrayListValues().build()));
    }

    public static void render(WorldRenderContext context) {
        Collection<WaypointCategory> categories = waypoints.get(SkyblockMod.skyblockMod.info.locationRaw);
        for (WaypointCategory category : categories) {
            if (category != null) {
                category.render(context);
            }
        }
    }
}
