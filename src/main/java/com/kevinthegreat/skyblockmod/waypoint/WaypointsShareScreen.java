package com.kevinthegreat.skyblockmod.waypoint;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

public class WaypointsShareScreen extends AbstractWaypointsScreen {
    private WaypointsListWidget waypointsListWidget;
    private final Set<NamedWaypoint> selectedWaypoints = new HashSet<>();

    protected WaypointsShareScreen(Screen parent, Multimap<String, WaypointCategory> waypoints) {
        super(Text.translatable("skyblocker.waypoints.shareWaypoints"), parent, MultimapBuilder.hashKeys().arrayListValues().build(waypoints));
    }

    @Override
    protected void init() {
        super.init();
        waypointsListWidget = addDrawableChild(new WaypointsListWidget(client, this, width, height -96, 32, 24));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFF);
    }

    @Override
    protected boolean isEnabled(WaypointCategory waypointCategory) {
        return selectedWaypoints.containsAll(waypointCategory.waypoints());
    }

    @Override
    protected boolean isEnabled(NamedWaypoint waypoint) {
        return selectedWaypoints.contains(waypoint);
    }

    @Override
    protected void enabledChanged(WaypointCategory waypointCategory, boolean enabled) {
        selectedWaypoints.addAll(waypointCategory.waypoints());
    }

    @Override
    protected void enabledChanged(NamedWaypoint waypoint, boolean enabled) {
        selectedWaypoints.add(waypoint);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
