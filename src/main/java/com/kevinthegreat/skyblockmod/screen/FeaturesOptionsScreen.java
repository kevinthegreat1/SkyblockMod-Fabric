package com.kevinthegreat.skyblockmod.screen;

import com.kevinthegreat.skyblockmod.waypoint.WaypointsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class FeaturesOptionsScreen extends AbstractSkyblockModOptionsScreen {
    public FeaturesOptionsScreen(Screen parent) {
        super(parent, SkyblockModOptionsScreen.OTHER_FEATURES);
    }

    @Override
    protected void addOptions() {
        body.addAll(options.experimentChronomatron, options.experimentSuperpairs, options.experimentUltrasequencer, options.fairySouls, options.fishing, options.mythologicalRitual, options.quiver, options.shortcuts, options.waypoints, options.waypointType);
        body.addWidgetEntry(createScreenButton(Text.translatable("skyblocker.waypoints.config"), WaypointsScreen::new), null);
    }
}
