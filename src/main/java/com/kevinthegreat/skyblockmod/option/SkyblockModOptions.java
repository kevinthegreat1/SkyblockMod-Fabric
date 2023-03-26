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
import net.minecraft.util.math.MathHelper;

import java.io.*;
import java.util.List;

public class SkyblockModOptions {
    private static final MaxSuppliableIntSliderCallbacks screenWidthCallback = new MaxSuppliableIntSliderCallbacks(0, SkyblockModOptions::getScreenWidth);
    private static final MaxSuppliableIntSliderCallbacks screenHeightCallback = new MaxSuppliableIntSliderCallbacks(0, SkyblockModOptions::getScreenHeight);
    private final File optionsFile = new File(FabricLoader.getInstance().getConfigDir().resolve(SkyblockMod.MOD_ID).toFile(), SkyblockMod.MOD_ID + ".json");
    public final SimpleOption<Boolean> dungeonMap = SimpleOption.ofBoolean("skyblockmod:dungeonMap", true);
    public final SimpleOption<Double> dungeonMapScale = new SimpleOption<>("skyblockmod:dungeonMap.scale", SimpleOption.emptyTooltip(), SkyblockModOptions::getGenericValueText, SimpleOption.DoubleSliderCallbacks.INSTANCE.withModifier(value -> MathHelper.square(value) * 10, value -> Math.sqrt(value / 10)), 0.1D, SkyblockModOptions::emptyConsumer);
    public final SimpleOption<Integer> dungeonMapX = new SimpleOption<>("skyblockmod:dungeonMap.offset.x", SimpleOption.emptyTooltip(), GameOptionsInvoker::getPixelValueText, screenWidthCallback, 0, SkyblockModOptions::emptyConsumer);
    public final SimpleOption<Integer> dungeonMapY = new SimpleOption<>("skyblockmod:dungeonMap.offset.y", SimpleOption.emptyTooltip(), GameOptionsInvoker::getPixelValueText, screenHeightCallback, 0, SkyblockModOptions::emptyConsumer);
    public final SimpleOption<Boolean> dungeonScore270 = SimpleOption.ofBoolean("skyblockmod:dungeonScore.270", true);
    public final SimpleOption<String> dungeonScore270Text = new SimpleOption<>("skyblockmod:dungeonScore.270.text", SimpleOption.emptyTooltip(), (optionText, value) -> Text.of(value), StringTextFieldCallbacks.INSTANCE_256, "270 score", SkyblockModOptions::emptyConsumer);
    public final SimpleOption<Boolean> dungeonScore300 = SimpleOption.ofBoolean("skyblockmod:dungeonScore.300", true);
    public final SimpleOption<String> dungeonScore300Text = new SimpleOption<>("skyblockmod:dungeonScore.300.text", SimpleOption.emptyTooltip(), (optionText, value) -> Text.of(value), StringTextFieldCallbacks.INSTANCE_256, "300 score", SkyblockModOptions::emptyConsumer);
    public final SimpleOption<Boolean> experimentChronomatron = SimpleOption.ofBoolean("skyblockmod:experiments.chronomatron", true);
    public final SimpleOption<Boolean> experimentSuperpairs = SimpleOption.ofBoolean("skyblockmod:experiments.superpairs", true);
    public final SimpleOption<Boolean> experimentUltrasequencer = SimpleOption.ofBoolean("skyblockmod:experiments.ultrasequencer", true);
    public final SimpleOption<Boolean> fairySouls = SimpleOption.ofBoolean("skyblockmod:fairySouls", false);
    public final SimpleOption<Boolean> fishing = SimpleOption.ofBoolean("skyblockmod:fishing", true);
    public final SimpleOption<Boolean> lividColor = SimpleOption.ofBoolean("skyblockmod:lividColor", true);
    public final SimpleOption<String> lividColorText = new SimpleOption<>("skyblockmod:lividColor.text", SimpleOption.emptyTooltip(), (optionText, value) -> Text.of(value), StringTextFieldCallbacks.INSTANCE_256, "[color]", SkyblockModOptions::emptyConsumer);
    public final SimpleOption<Boolean> quiver = SimpleOption.ofBoolean("skyblockmod:quiver", true);
    public final SimpleOption<Boolean> reparty = SimpleOption.ofBoolean("skyblockmod:reparty", true);
    @SuppressWarnings("SuspiciousNameCombination")
    public final List<List<Pair<String, SimpleOption<?>>>> optionsList = List.of(List.of(new Pair<>("dungeonMap", dungeonMap), new Pair<>("dungeonMap.scale", dungeonMapScale), new Pair<>("dungeonMap.offset.x", dungeonMapX), new Pair<>("dungeonMap.offset.y", dungeonMapY), new Pair<>("dungeonScore.270", dungeonScore270), new Pair<>("dungeonScore.270.text", dungeonScore270Text), new Pair<>("dungeonScore.300", dungeonScore300), new Pair<>("dungeonScore.300.text", dungeonScore300Text), new Pair<>("experiments.chronomatron", experimentChronomatron), new Pair<>("experiments.superpairs", experimentSuperpairs), new Pair<>("experiments.ultrasequencer", experimentUltrasequencer), new Pair<>("fairySouls", fairySouls), new Pair<>("fishing", fishing), new Pair<>("lividColor", lividColor), new Pair<>("lividColor.text", lividColorText), new Pair<>("quiver", quiver), new Pair<>("reparty", reparty)));

    public static Text getGenericValueText(Text prefix, double value) {
        return GameOptions.getGenericValueText(prefix, Text.literal(String.format("%.2f", value)));
    }

    public static int getScreenWidth() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        return screen == null ? Integer.MAX_VALUE - 1 : screen.width * 2;
    }

    public static int getScreenHeight() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        return screen == null ? Integer.MAX_VALUE - 1 : screen.height * 2;
    }

    public static <T> void emptyConsumer(T value) {
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
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated(since = "1.8.2")
    private void loadLegacyOptions() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(FabricLoader.getInstance().getConfigDir().toFile(), SkyblockMod.MOD_ID + ".txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(":");
                try {
                    switch (args[0]) {
                        case "experimentChronomatron" -> experimentChronomatron.setValue(Boolean.parseBoolean(args[1]));
                        case "experimentSuperpairs" -> experimentSuperpairs.setValue(Boolean.parseBoolean(args[1]));
                        case "experimentUltrasequencer" ->
                                experimentUltrasequencer.setValue(Boolean.parseBoolean(args[1]));
                        case "fairySouls" -> fairySouls.setValue(Boolean.parseBoolean(args[1]));
                        case "fishing" -> fishing.setValue(Boolean.parseBoolean(args[1]));
                        case "lividColor" -> lividColor.setValue(Boolean.parseBoolean(args[1]));
                        case "lividColorText" -> lividColorText.setValue(args[1]);
                        case "map" -> dungeonMap.setValue(Boolean.parseBoolean(args[1]));
                        case "mapScale" -> dungeonMapScale.setValue(Double.parseDouble(args[1]));
                        case "mapOffsetX" -> dungeonMapX.setValue(Integer.parseInt(args[1]));
                        case "mapOffsetY" -> dungeonMapY.setValue(Integer.parseInt(args[1]));
                        case "quiverWarning" -> quiver.setValue(Boolean.parseBoolean(args[1]));
                        case "reparty" -> reparty.setValue(Boolean.parseBoolean(args[1]));
                        case "score270" -> dungeonScore270.setValue(Boolean.parseBoolean(args[1]));
                        case "score270Text" -> dungeonScore270Text.setValue(args[1]);
                        case "score300" -> dungeonScore300.setValue(Boolean.parseBoolean(args[1]));
                        case "score300Text" -> dungeonScore300Text.setValue(args[1]);
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
