package com.kevinthegreat.skyblockmod.screen;

import net.minecraft.client.gui.screen.Screen;

public class DungeonsOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public DungeonsOptionsScreen(Screen parent) {
        super(parent, SkyblockModOptionsScreen.DUNGEONS);
    }

    @Override
    protected void init() {
        super.init();
        addOptionButton(options.dungeonMap);
        addOptionButton(options.dungeonMapScale);
        addOptionButton(options.dungeonMapX);
        addOptionButton(options.dungeonMapY);
        addOptionButton(options.dungeonScore270);
        addOptionButton(options.dungeonScore270Text);
        addOptionButton(options.dungeonScore300);
        addOptionButton(options.dungeonScore300Text);
        addOptionButton(options.lividColor);
        addOptionButton(options.lividColorText);
        addOptionButton(options.reparty);
        addGridWidget();
    }
}
