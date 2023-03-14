package com.kevinthegreat.skyblockmod.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SkyblockModOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public static final Text DUNGEONS = Text.translatable("skyblockmod:dungeons");
    public static final Text OTHER_FEATURES = Text.translatable("skyblockmod:options.other_features");

    public SkyblockModOptionsScreen(Screen parent) {
        super(parent, Text.translatable("skyblockmod:options.title"));
    }

    @Override
    protected void init() {
        super.init();
        add(DUNGEONS, DungeonsOptionsScreen::new);
        add(OTHER_FEATURES, FeaturesOptionsScreen::new);
        addGridWidget();
    }
}
