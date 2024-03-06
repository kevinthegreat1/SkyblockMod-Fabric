package com.kevinthegreat.skyblockmod.waypoint;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class AbstractWaypointsScreen extends Screen {
    protected final Screen parent;
    protected final Multimap<String, WaypointCategory> waypoints;

    public AbstractWaypointsScreen(Text title, Screen parent) {
        this(title, parent, MultimapBuilder.hashKeys().arrayListValues().build());
    }

    public AbstractWaypointsScreen(Text title, Screen parent, Multimap<String, WaypointCategory> waypoints) {
        super(title);
        this.parent = parent;
        this.waypoints = waypoints;
    }

    protected abstract boolean isEnabled(WaypointCategory waypointCategory);

    protected abstract boolean isEnabled(NamedWaypoint waypoint);

    protected abstract void enabledChanged(WaypointCategory waypointCategory, boolean enabled);

    protected abstract void enabledChanged(NamedWaypoint waypoint, boolean enabled);
}
