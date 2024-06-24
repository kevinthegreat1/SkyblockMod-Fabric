package com.kevinthegreat.skyblockmod.screen;

import net.minecraft.client.gui.screen.Screen;

public class DungeonsOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public DungeonsOptionsScreen(Screen parent) {
        super(parent, SkyblockModOptionsScreen.DUNGEONS);
    }

    @Override
    protected void addOptions() {
        body.addAll(options.dungeonMap, options.dungeonMapScale, options.dungeonMapX, options.dungeonMapY, options.dungeonScore270, options.dungeonScore270Text, options.dungeonScore300, options.dungeonScore300Text, options.lividColor, options.lividColorText, options.reparty);
    }
}
