package com.kevinthegreat.skyblockmod.screen;

import com.kevinthegreat.skyblockmod.waypoint.WaypointsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class FeaturesOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public FeaturesOptionsScreen(Screen parent) {
        super(parent, SkyblockModOptionsScreen.OTHER_FEATURES);
    }

    @Override
    protected void init() {
        super.init();
        addOptionButton(options.experimentChronomatron);
        addOptionButton(options.experimentSuperpairs);
        addOptionButton(options.experimentUltrasequencer);
        addOptionButton(options.fairySouls);
        addOptionButton(options.fishing);
        addOptionButton(options.mythologicalRitual);
        addOptionButton(options.quiver);
        addOptionButton(options.shortcuts);
        addOptionButton(options.waypoints);
        addOptionButton(options.waypointType);
        addScreenButton(Text.translatable("skyblocker.waypoints.config"), WaypointsScreen::new);
        addGridWidget();
    }
}
