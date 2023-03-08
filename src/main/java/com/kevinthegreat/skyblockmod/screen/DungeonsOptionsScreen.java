package com.kevinthegreat.skyblockmod.screen;

import net.minecraft.client.gui.screen.Screen;

public class DungeonsOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public DungeonsOptionsScreen(Screen parent) {
        super(parent, SkyblockModOptionsScreen.DUNGEONS);
    }

    @Override
    protected void init() {
        super.init();
        add(options.dungeonMap);
        add(options.dungeonMapScale);
        add(options.dungeonMapX);
        add(options.dungeonMapY);
        add(options.dungeonScore270);
        add(options.dungeonScore270Text);
        add(options.dungeonScore300);
        add(options.dungeonScore300Text);
        add(options.lividColor);
        add(options.lividColorText);
        add(options.reparty);
        addGridWidget();
    }
}
