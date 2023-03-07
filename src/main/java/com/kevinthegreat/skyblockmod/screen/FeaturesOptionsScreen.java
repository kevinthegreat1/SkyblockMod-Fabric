package com.kevinthegreat.skyblockmod.screen;

import net.minecraft.client.gui.screen.Screen;

public class FeaturesOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public FeaturesOptionsScreen(Screen parent) {
        super(parent, SkyblockModOptionsScreen.OTHER_FEATURES);
    }


    @Override
    protected void init() {
        super.init();
        addGridWidget();
    }
}