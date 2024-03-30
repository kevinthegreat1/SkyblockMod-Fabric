package com.kevinthegreat.skyblockmod.waypoint;

import com.google.common.collect.Multimap;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

public class WaypointsShareScreen extends AbstractWaypointsScreen<WaypointsScreen> {
    private final Set<NamedWaypoint> selectedWaypoints = new HashSet<>();

    protected WaypointsShareScreen(WaypointsScreen parent, Multimap<String, WaypointCategory> waypoints) {
        super(Text.translatable("skyblocker.waypoints.shareWaypoints"), parent, waypoints);
    }

    @Override
    protected void init() {
        super.init();
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginY(2);
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(ButtonWidget.builder(Text.translatable("skyblocker.waypoints.importWaypointsSkytils"), buttonImport -> {
            for (WaypointCategory waypointCategory : Waypoints.fromSkytilsBase64(client.keyboard.getClipboard())) {
                selectedWaypoints.addAll(waypointCategory.waypoints());
                waypoints.put(waypointCategory.island(), waypointCategory);
            }
            waypointsListWidget.updateEntries();
        }).build());
        adder.add(ButtonWidget.builder(Text.translatable("skyblocker.waypoints.importWaypointsSnoopy"), buttonImport -> {}).build());
        adder.add(ButtonWidget.builder(ScreenTexts.BACK, buttonBack -> close()).build());
        adder.add(ButtonWidget.builder(Text.translatable("skyblocker.waypoints.exportWaypointsSkytils"), buttonExport -> client.keyboard.setClipboard(Waypoints.toSkytilsBase64(waypoints.values().stream().map(WaypointCategory.filter(selectedWaypoints::contains)).filter(waypointCategory -> !waypointCategory.waypoints().isEmpty()).toList()))).build());
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.height - 64, this.width, 64);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFF);
    }

    @Override
    protected boolean isEnabled(NamedWaypoint waypoint) {
        return selectedWaypoints.contains(waypoint);
    }

    @Override
    protected void enabledChanged(NamedWaypoint waypoint, boolean enabled) {
        if (enabled) selectedWaypoints.add(waypoint);
        else selectedWaypoints.remove(waypoint);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void close() {
        client.setScreen(parent);
    }
}
