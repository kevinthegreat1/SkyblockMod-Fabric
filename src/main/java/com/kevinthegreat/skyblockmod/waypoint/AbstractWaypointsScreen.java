package com.kevinthegreat.skyblockmod.waypoint;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.util.Location;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Arrays;

public abstract class AbstractWaypointsScreen<T extends Screen> extends Screen {
    protected final T parent;
    protected final Multimap<String, WaypointCategory> waypoints;
    protected String island = SkyblockMod.skyblockMod.info.locationRaw;
    protected WaypointsListWidget waypointsListWidget;
    protected DropdownWidget<Location> islandWidget;

    public AbstractWaypointsScreen(Text title, T parent) {
        this(title, parent, MultimapBuilder.hashKeys().arrayListValues().build());
    }

    public AbstractWaypointsScreen(Text title, T parent, Multimap<String, WaypointCategory> waypoints) {
        super(title);
        this.parent = parent;
        this.waypoints = waypoints;
    }

    @Override
    protected void init() {
        super.init();
        waypointsListWidget = addDrawableChild(new WaypointsListWidget(client, this, width, height - 96, 32, 24));
        islandWidget = addDrawableChild(new DropdownWidget<>(client, width - 158, 8, 150, Arrays.asList(Location.values()), this::islandChanged, Location.from(island)));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (islandWidget.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        boolean mouseClicked = super.mouseClicked(mouseX, mouseY, button);
        updateButtons();
        return mouseClicked;
    }

    protected void islandChanged(Location location) {
        island = location.id();
        waypointsListWidget.setIsland(island);
    }

    protected abstract boolean isEnabled(NamedWaypoint waypoint);

    protected abstract void enabledChanged(NamedWaypoint waypoint, boolean enabled);

    protected void updateButtons() {
        waypointsListWidget.updateButtons();
    }
}
