package com.kevinthegreat.skyblockmod.screen;

import com.kevinthegreat.skyblockmod.util.EmptyButtonWidget;
import net.minecraft.client.gui.screen.Screen;
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
    protected void addOptions() {
        body.addWidgetEntry(createScreenButton(DUNGEONS, DungeonsOptionsScreen::new), createScreenButton(OTHER_FEATURES, FeaturesOptionsScreen::new));
        body.addWidgetEntry(EmptyButtonWidget.ofHeight(20), null);
        body.addWidgetEntry(createButton(Text.translatable("skyblockmod:options.reload"), button -> options.load()), createButton(Text.translatable("skyblockmod:options.save"), button -> options.save()));
    }
}
