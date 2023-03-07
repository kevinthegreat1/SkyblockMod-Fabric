package com.kevinthegreat.skyblockmod.option;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.mixins.accessors.GameOptionsInvoker;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.io.File;
import java.util.List;

public class SkyblockModOptions {
    private static final MaxSuppliableIntSliderCallbacks screenWidthCallback = new MaxSuppliableIntSliderCallbacks(0, SkyblockModOptions::getScreenWidth);
    private static final MaxSuppliableIntSliderCallbacks screenHeightCallback = new MaxSuppliableIntSliderCallbacks(0, SkyblockModOptions::getScreenHeight);
    public final SimpleOption<Boolean> dungeonMap = SimpleOption.ofBoolean("skyblockmod:dungeonMap", true);
    public final SimpleOption<Double> dungeonMapScale = new SimpleOption<>("skyblockmod:dungeonMap.scale", SimpleOption.emptyTooltip(), SkyblockModOptions::getGenericValueText, SimpleOption.DoubleSliderCallbacks.INSTANCE, 1D, value -> {});
    public final SimpleOption<Integer> dungeonMapX = new SimpleOption<>("skyblockmod:dungeonMap.offset.x", SimpleOption.emptyTooltip(), GameOptionsInvoker::getPixelValueText, screenWidthCallback, 0, value -> {});
    public final SimpleOption<Integer> dungeonMapY = new SimpleOption<>("skyblockmod:dungeonMap.offset.y", SimpleOption.emptyTooltip(), GameOptionsInvoker::getPixelValueText, screenHeightCallback, 0, value -> {});
    @SuppressWarnings("SuspiciousNameCombination")
    public final List<List<Pair<String, SimpleOption<?>>>> options = List.of(List.of(new Pair<>("dungeonMap", dungeonMap), new Pair<>("dungeonMap.scale", dungeonMapScale), new Pair<>("dungeonMap.offset.x", dungeonMapX), new Pair<>("dungeonMap.offset.y", dungeonMapY)));

    public static Text getGenericValueText(Text prefix, double value) {
        return GameOptions.getGenericValueText(prefix, Text.literal(Double.toString(value)));
    }

    public static int getScreenWidth() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        return screen == null ? Integer.MAX_VALUE : screen.width;
    }

    public static int getScreenHeight() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        return screen == null ? Integer.MAX_VALUE : screen.height;
    }

    public void load() {
    }

    public void save() {
    }
}
