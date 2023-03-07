package com.kevinthegreat.skyblockmod.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Function;

public class SkyblockModOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public static final Text DUNGEONS = Text.translatable("skyblockmod:dungeons");
    public static final Text OTHER_FEATURES = Text.translatable("skyblockmod:options.other_features");

    public SkyblockModOptionsScreen(Screen parent) {
        super(parent, Text.translatable("skyblockmod:options.title"));
    }

    @Override
    protected void init() {
        super.init();
        adder.add(createButton(DUNGEONS, DungeonsOptionsScreen::new));
        adder.add(createButton(OTHER_FEATURES, FeaturesOptionsScreen::new));
        addGridWidget();
    }

    private ButtonWidget createButton(Text message, Function<Screen, Screen> screenFunction) {
        return ButtonWidget.builder(message, button -> client.setScreen(screenFunction.apply(this))).build();
    }
}