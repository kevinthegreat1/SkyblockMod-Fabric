package com.kevinthegreat.skyblockmod.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.mixins.accessors.GameOptionsInvoker;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.Util;

import java.io.*;
import java.util.List;

public class SkyblockModOptions {
    private static final MaxSuppliableIntSliderCallbacks screenWidthCallback = new MaxSuppliableIntSliderCallbacks(0, SkyblockModOptions::getScreenWidth);
    private static final MaxSuppliableIntSliderCallbacks screenHeightCallback = new MaxSuppliableIntSliderCallbacks(0, SkyblockModOptions::getScreenHeight);
    private final File optionsFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), SkyblockMod.MOD_ID + ".json");
    public final SimpleOption<Boolean> dungeonMap = SimpleOption.ofBoolean("skyblockmod:dungeonMap", true);
    public final SimpleOption<Double> dungeonMapScale = new SimpleOption<>("skyblockmod:dungeonMap.scale", SimpleOption.emptyTooltip(), SkyblockModOptions::getGenericValueText, SimpleOption.DoubleSliderCallbacks.INSTANCE, 1D, value -> {});
    public final SimpleOption<Integer> dungeonMapX = new SimpleOption<>("skyblockmod:dungeonMap.offset.x", SimpleOption.emptyTooltip(), GameOptionsInvoker::getPixelValueText, screenWidthCallback, 0, value -> {});
    public final SimpleOption<Integer> dungeonMapY = new SimpleOption<>("skyblockmod:dungeonMap.offset.y", SimpleOption.emptyTooltip(), GameOptionsInvoker::getPixelValueText, screenHeightCallback, 0, value -> {});
    @SuppressWarnings("SuspiciousNameCombination")
    public final List<List<Pair<String, SimpleOption<?>>>> optionsList = List.of(List.of(new Pair<>("dungeonMap", dungeonMap), new Pair<>("dungeonMap.scale", dungeonMapScale), new Pair<>("dungeonMap.offset.x", dungeonMapX), new Pair<>("dungeonMap.offset.y", dungeonMapY)));

    public static Text getGenericValueText(Text prefix, double value) {
        return GameOptions.getGenericValueText(prefix, Text.literal(String.format("%.2f", value)));
    }

    public static int getScreenWidth() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        return screen == null ? Integer.MAX_VALUE : screen.width;
    }

    public static int getScreenHeight() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        return screen == null ? Integer.MAX_VALUE : screen.height;
    }

    /**
     * Loads options from {@code /config/skyblockmod.json} with Gson.
     */
    public void load() {
        if (!optionsFile.exists()) {
            loadLegacyOptions();
            return;
        }
        JsonObject optionsJson;
        try (BufferedReader reader = new BufferedReader(new FileReader(optionsFile))) {
            optionsJson = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (FileNotFoundException e) {
            SkyblockMod.LOGGER.warn("Options file not found", e);
            return;
        } catch (IOException e) {
            SkyblockMod.LOGGER.error("Failed to load options", e);
            return;
        }
        for (List<Pair<String, SimpleOption<?>>> optionRow : optionsList) {
            for (Pair<String, SimpleOption<?>> namedOption : optionRow) {
                parseOption(optionsJson, namedOption.getLeft(), namedOption.getRight());
            }
        }
    }

    /**
     * Parses an option from a {@link JsonObject} with Codec.
     *
     * @param optionsJson the {@link JsonObject} to parse from
     * @param name        the name of the option
     * @param option      the option to parse to
     */
    private <T> void parseOption(JsonObject optionsJson, String name, SimpleOption<T> option) {
        DataResult<T> dataResult = option.getCodec().parse(JsonOps.INSTANCE, optionsJson.get(name));
        dataResult.error().ifPresent(error -> SkyblockMod.LOGGER.error("Error parsing option value " + optionsJson.get(name) + " for option " + name + ": " + error));
        dataResult.result().ifPresent(option::setValue);
    }

    /**
     * Loads legacy options from {@code /config/skyblockmod.txt}.
     */
    @Deprecated(since = "1.8.2")
    private void loadLegacyOptions() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(FabricLoader.getInstance().getConfigDir().toFile(), SkyblockMod.MOD_ID + ".txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(":");
                try {
                    switch (args[0]) {
                        case "experimentChronomatron" ->
                                SkyblockMod.skyblockMod.experiments.toggleChronomatron = Boolean.parseBoolean(args[1]);
                        case "experimentSuperpairs" ->
                                SkyblockMod.skyblockMod.experiments.toggleSuperpairs = Boolean.parseBoolean(args[1]);
                        case "experimentUltrasequencer" ->
                                SkyblockMod.skyblockMod.experiments.toggleUltrasequencer = Boolean.parseBoolean(args[1]);
                        case "fairySouls" -> SkyblockMod.skyblockMod.fairySouls.on = Boolean.parseBoolean(args[1]);
                        case "fishing" -> SkyblockMod.skyblockMod.fishing.on = Boolean.parseBoolean(args[1]);
                        case "lividColor" -> SkyblockMod.skyblockMod.lividColor.on = Boolean.parseBoolean(args[1]);
                        case "lividColorText" -> SkyblockMod.skyblockMod.lividColor.text = args[1];
                        case "map" -> dungeonMap.setValue(Boolean.parseBoolean(args[1]));
                        case "mapScale" -> dungeonMapScale.setValue(Double.parseDouble(args[1]));
                        case "mapOffsetX" -> dungeonMapX.setValue(Integer.parseInt(args[1]));
                        case "mapOffsetY" -> dungeonMapY.setValue(Integer.parseInt(args[1]));
                        case "quiverWarning" ->
                                SkyblockMod.skyblockMod.quiverWarning.on = Boolean.parseBoolean(args[1]);
                        case "reparty" -> SkyblockMod.skyblockMod.reparty.on = Boolean.parseBoolean(args[1]);
                        case "score270" -> SkyblockMod.skyblockMod.dungeonScore.on270 = Boolean.parseBoolean(args[1]);
                        case "score270Text" -> SkyblockMod.skyblockMod.dungeonScore.text270 = args[1];
                        case "score300" -> SkyblockMod.skyblockMod.dungeonScore.on300 = Boolean.parseBoolean(args[1]);
                        case "score300Text" -> SkyblockMod.skyblockMod.dungeonScore.text300 = args[1];
                    }
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    SkyblockMod.LOGGER.error("Unable to parse configuration \"" + args[0] + "\".");
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            SkyblockMod.LOGGER.info("Configuration file not found.");
        } catch (IOException e) {
            SkyblockMod.LOGGER.error("Error while reading configuration file.");
        }
    }

    @SuppressWarnings("unused")
    public void save(MinecraftClient client) {
        save();
    }

    /**
     * Saves options to {@code /config/skyblockmod.json} with Gson.
     */
    public void save() {
        JsonObject optionsJson = new JsonObject();
        for (List<Pair<String, SimpleOption<?>>> optionRow : optionsList) {
            for (Pair<String, SimpleOption<?>> namedOption : optionRow) {
                saveOption(optionsJson, namedOption.getLeft(), namedOption.getRight());
            }
        }
        File tempFile;
        try {
            tempFile = File.createTempFile(SkyblockMod.MOD_ID, ".json", optionsFile.getParentFile());
        } catch (IOException e) {
            SkyblockMod.LOGGER.error("Failed to save options file", e);
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            SkyblockMod.GSON.toJson(optionsJson, writer);
        } catch (IOException e) {
            SkyblockMod.LOGGER.error("Failed to write options", e);
        }
        File backup = new File(optionsFile.getParentFile(), SkyblockMod.MOD_ID + ".json_old");
        Util.backupAndReplace(optionsFile, tempFile, backup);
    }

    /**
     * Saves an option to a {@link JsonObject} with Codec.
     *
     * @param optionsJson the {@link JsonObject} to save to
     * @param name        the name of the option
     * @param option      the option to save
     */
    private <T> void saveOption(JsonObject optionsJson, String name, SimpleOption<T> option) {
        DataResult<JsonElement> dataResult = option.getCodec().encodeStart(JsonOps.INSTANCE, option.getValue());
        dataResult.error().ifPresent(error -> SkyblockMod.LOGGER.error("Error encoding option value " + option.getValue() + " for option " + name + ": " + error));
        dataResult.result().ifPresent(optionJson -> optionsJson.add(name, optionJson));
    }
}
