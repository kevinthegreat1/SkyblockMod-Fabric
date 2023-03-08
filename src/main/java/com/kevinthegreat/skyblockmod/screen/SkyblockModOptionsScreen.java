package com.kevinthegreat.skyblockmod.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.text.Text;

public class SkyblockModOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public static final Text DUNGEONS = Text.translatable("skyblockmod:dungeons");
    public static final Text OTHER_FEATURES = Text.translatable("skyblockmod:options.other_features");

    public SkyblockModOptionsScreen() {
        this(null);
    }

    public SkyblockModOptionsScreen(Screen parent) {
        super(parent, Text.translatable("skyblockmod:options.title"));
    }

    @Override
    protected void init() {
        super.init();
        addScreenButton(DUNGEONS, DungeonsOptionsScreen::new);
        addScreenButton(OTHER_FEATURES, FeaturesOptionsScreen::new);
        adder.add(EmptyWidget.ofHeight(20), 2);
        addButton(Text.translatable("skyblockmod:options.reload"), button -> options.load());
        addButton(Text.translatable("skyblockmod:options.save"), button -> options.save());
        addGridWidget();
    }
}
