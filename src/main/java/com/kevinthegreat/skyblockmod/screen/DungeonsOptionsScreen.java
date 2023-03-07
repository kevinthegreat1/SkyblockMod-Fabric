package com.kevinthegreat.skyblockmod.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public class DungeonsOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public DungeonsOptionsScreen(Screen parent) {
        super(parent, SkyblockModOptionsScreen.DUNGEONS);
    }

    @Override
    protected void init() {
        super.init();
        adder.add(options.dungeonMap.createWidget(gameOptions, 0, 0, ButtonWidget.DEFAULT_WIDTH));
        adder.add(options.dungeonMapScale.createWidget(gameOptions, 0, 0, ButtonWidget.DEFAULT_WIDTH));
        adder.add(options.dungeonMapX.createWidget(gameOptions, 0, 0, ButtonWidget.DEFAULT_WIDTH));
        adder.add(options.dungeonMapY.createWidget(gameOptions, 0, 0, ButtonWidget.DEFAULT_WIDTH));
        addGridWidget();
    }
}
