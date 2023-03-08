package com.kevinthegreat.skyblockmod.screen;

import net.minecraft.client.gui.screen.Screen;

public class FeaturesOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public FeaturesOptionsScreen(Screen parent) {
        super(parent, SkyblockModOptionsScreen.OTHER_FEATURES);
    }


    @Override
    protected void init() {
        super.init();
        add(options.experimentChronomatron);
        add(options.experimentSuperpairs);
        add(options.experimentUltrasequencer);
        add(options.fairySouls);
        add(options.fishing);
        add(options.quiver);
        addGridWidget();
    }
}
