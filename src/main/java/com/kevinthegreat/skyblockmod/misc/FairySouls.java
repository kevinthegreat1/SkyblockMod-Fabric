package com.kevinthegreat.skyblockmod.misc;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.util.ChatListener;
import com.kevinthegreat.skyblockmod.util.RenderHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class FairySouls implements ChatListener {
    private CompletableFuture<Void> fairySoulsLoaded;
    private final Map<String, Set<BlockPos>> fairySouls = new HashMap<>();
    private final Map<String, Map<String, Set<BlockPos>>> foundFairies = new HashMap<>();

    public void loadFairySouls() {
        fairySoulsLoaded = SkyblockMod.skyblockMod.neuRepo.runAsyncAfterLoad(() -> {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(SkyblockMod.NEU_REPO_DIR.resolve("constants").resolve("fairy_souls.json").toFile()));
                for (Map.Entry<String, JsonElement> fairySoulJson : JsonParser.parseReader(reader).getAsJsonObject().asMap().entrySet()) {
                    if (fairySoulJson.getKey().equals("//") || fairySoulJson.getKey().equals("Max Souls")) {
                        continue;
                    }
                    ImmutableSet.Builder<BlockPos> fairySoulsForLocation = ImmutableSet.builder();
                    for (JsonElement fairySoul : fairySoulJson.getValue().getAsJsonArray().asList()) {
                        fairySoulsForLocation.add(parseBlockPos(fairySoul));
                    }
                    fairySouls.put(fairySoulJson.getKey(), fairySoulsForLocation.build());
                }
                reader = new BufferedReader(new FileReader(SkyblockMod.CONFIG_DIR.resolve("found_fairy_souls.json").toFile()));
                for (Map.Entry<String, JsonElement> foundFairiesForProfileJson : JsonParser.parseReader(reader).getAsJsonObject().asMap().entrySet()) {
                    Map<String, Set<BlockPos>> foundFairiesForProfile = new HashMap<>();
                    for (Map.Entry<String, JsonElement> foundFairiesForLocationJson : foundFairiesForProfileJson.getValue().getAsJsonObject().asMap().entrySet()) {
                        Set<BlockPos> foundFairiesForLocation = new HashSet<>();
                        for (JsonElement foundFairy : foundFairiesForLocationJson.getValue().getAsJsonArray().asList()) {
                            foundFairiesForLocation.add(parseBlockPos(foundFairy));
                        }
                        foundFairiesForProfile.put(foundFairiesForLocationJson.getKey(), foundFairiesForLocation);
                    }
                    foundFairies.put(foundFairiesForProfileJson.getKey(), foundFairiesForProfile);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                SkyblockMod.LOGGER.error("Failed to load found fairy souls.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private BlockPos parseBlockPos(JsonElement posJson) {
        String[] posArray = posJson.getAsString().split(",");
        return new BlockPos(Integer.parseInt(posArray[0]), Integer.parseInt(posArray[1]), Integer.parseInt(posArray[2]));
    }

    public void saveFoundFairySouls(MinecraftClient client) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(SkyblockMod.CONFIG_DIR.resolve("found_fairy_souls.json").toFile()));
            JsonObject foundFairiesJson = new JsonObject();
            for (Map.Entry<String, Map<String, Set<BlockPos>>> foundFairiesForProfile : foundFairies.entrySet()) {
                JsonObject foundFairiesForProfileJson = new JsonObject();
                for (Map.Entry<String, Set<BlockPos>> foundFairiesForLocation : foundFairiesForProfile.getValue().entrySet()) {
                    JsonArray foundFairiesForLocationJson = new JsonArray();
                    for (BlockPos foundFairy : foundFairiesForLocation.getValue()) {
                        foundFairiesForLocationJson.add(foundFairy.getX() + "," + foundFairy.getY() + "," + foundFairy.getZ());
                    }
                    foundFairiesForProfileJson.add(foundFairiesForLocation.getKey(), foundFairiesForLocationJson);
                }
                foundFairiesJson.add(foundFairiesForProfile.getKey(), foundFairiesForProfileJson);
            }
            SkyblockMod.GSON.toJson(foundFairiesJson, writer);
            writer.close();
        } catch (IOException e) {
            SkyblockMod.LOGGER.error("Failed to write found fairy souls to file.");
        }
    }

    public void render(WorldRenderContext context) {
        if (!SkyblockMod.skyblockMod.options.fairySouls.getValue()) {
            return;
        }
        if (!fairySoulsLoaded.isDone()) {
            SkyblockMod.LOGGER.warn("Fairy souls are not loaded yet.");
            return;
        }
        if (!fairySouls.containsKey(SkyblockMod.skyblockMod.info.location)) {
            return;
        }
        for (BlockPos fairySoul : fairySouls.get(SkyblockMod.skyblockMod.info.location)) {
            float[] colorComponents = isFairySoulNotFound(fairySoul) ? DyeColor.GREEN.getColorComponents() : DyeColor.RED.getColorComponents();
            RenderHelper.renderFilledThroughWalls(context, fairySoul, colorComponents, 0.5F);
            RenderHelper.renderBeaconBeam(context, fairySoul, colorComponents);
        }
    }

    private boolean isFairySoulNotFound(BlockPos fairySoul) {
        Map<String, Set<BlockPos>> foundFairiesForProfile = foundFairies.get(SkyblockMod.skyblockMod.info.profile);
        if (foundFairiesForProfile == null) {
            return true;
        }
        Set<BlockPos> foundFairiesForProfileAndLocation = foundFairiesForProfile.get(SkyblockMod.skyblockMod.info.location);
        if (foundFairiesForProfileAndLocation == null) {
            return true;
        }
        return !foundFairiesForProfileAndLocation.contains(fairySoul);
    }

    @Override
    public boolean onChatMessage(String message) {
        if (message.equals("You have already found that Fairy Soul!") || message.equals("SOUL! You found a Fairy Soul!")) {
            markClosestFairyFound();
        }
        return true;
    }

    private void markClosestFairyFound() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            SkyblockMod.LOGGER.warn("Failed to mark closest fairy soul as found because player is null.");
            return;
        }
        fairySouls.get(SkyblockMod.skyblockMod.info.location).stream().filter(this::isFairySoulNotFound).min(Comparator.comparingDouble(fairySoul -> fairySoul.getSquaredDistance(player.getPos()))).ifPresent(fairySoul -> {
            initializeFoundFairiesForCurrentProfileAndLocation();
            foundFairies.get(SkyblockMod.skyblockMod.info.profile).get(SkyblockMod.skyblockMod.info.location).add(fairySoul);
        });
    }

    public void markAllFairiesFound() {
        initializeFoundFairiesForCurrentProfileAndLocation();
        foundFairies.get(SkyblockMod.skyblockMod.info.profile).get(SkyblockMod.skyblockMod.info.location).addAll(fairySouls.get(SkyblockMod.skyblockMod.info.location));
    }

    public void markAllFairiesNotFound() {
        Map<String, Set<BlockPos>> foundFairiesForProfile = foundFairies.get(SkyblockMod.skyblockMod.info.profile);
        if (foundFairiesForProfile != null) {
            foundFairiesForProfile.remove(SkyblockMod.skyblockMod.info.location);
        }
    }

    private void initializeFoundFairiesForCurrentProfileAndLocation() {
        initializeFoundFairiesForProfileAndLocation(SkyblockMod.skyblockMod.info.profile, SkyblockMod.skyblockMod.info.location);
    }

    private void initializeFoundFairiesForProfileAndLocation(String profile, String location) {
        foundFairies.computeIfAbsent(profile, profileKey -> new HashMap<>());
        foundFairies.get(profile).computeIfAbsent(location, locationKey -> new HashSet<>());
    }
}
