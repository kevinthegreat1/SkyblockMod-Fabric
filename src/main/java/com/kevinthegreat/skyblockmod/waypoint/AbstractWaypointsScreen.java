package com.kevinthegreat.skyblockmod.waypoint;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class AbstractWaypointsScreen<T extends Screen> extends Screen {
    protected final T parent;
    protected final Multimap<String, WaypointCategory> waypoints;
    protected WaypointsListWidget waypointsListWidget;

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
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean mouseClicked = super.mouseClicked(mouseX, mouseY, button);
        updateButtons();
        return mouseClicked;
    }

    protected abstract boolean isEnabled(NamedWaypoint waypoint);

    protected abstract void enabledChanged(NamedWaypoint waypoint, boolean enabled);

    protected void updateButtons() {
        waypointsListWidget.updateButtons();
    }
}
