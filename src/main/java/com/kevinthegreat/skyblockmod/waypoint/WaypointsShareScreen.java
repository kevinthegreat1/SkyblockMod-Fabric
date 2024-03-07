package com.kevinthegreat.skyblockmod.waypoint;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WaypointsShareScreen extends AbstractWaypointsScreen {
    private static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    private WaypointsListWidget waypointsListWidget;
    private final Set<NamedWaypoint> selectedWaypoints = new HashSet<>();

    protected WaypointsShareScreen(WaypointsScreen parent, Multimap<String, WaypointCategory> waypoints) {
        super(Text.translatable("skyblocker.waypoints.shareWaypoints"), parent, MultimapBuilder.hashKeys().arrayListValues().build(waypoints));
    }

    @Override
    protected void init() {
        super.init();
        waypointsListWidget = addDrawableChild(new WaypointsListWidget(client, this, width, height - 96, 32, 24));
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginY(2);
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(ButtonWidget.builder(Text.translatable("skyblocker.waypoints.importWaypointsSkytils"), buttonImport -> {
            try {
                for (WaypointCategory waypointCategory : Waypoints.fromSkytilsBase64(CLIPBOARD.getData(DataFlavor.stringFlavor).toString())) {
                    selectedWaypoints.addAll(waypointCategory.waypoints());
                    waypoints.put(waypointCategory.island(), waypointCategory);
                    waypointsListWidget.updateEntries();
                }
            } catch (UnsupportedFlavorException e) {
                Waypoints.LOGGER.error("[Skyblocker Waypoints] Encountered exception while parsing clipboard data", e);
            } catch (IOException e) {
                Waypoints.LOGGER.error("[Skyblocker Waypoints] Encountered exception while reading clipboard data", e);
            }
        }).build());
        adder.add(ButtonWidget.builder(Text.translatable("skyblocker.waypoints.importWaypointsSnoopy"), buttonImport -> {}).build());
        adder.add(ButtonWidget.builder(ScreenTexts.BACK, buttonBack -> close()).build());
        adder.add(ButtonWidget.builder(Text.translatable("skyblocker.waypoints.exportWaypointsSkytils"), buttonExport -> {
            List<WaypointCategory> exportWaypoints = waypoints.values().stream().map(WaypointCategory::new).toList();
            exportWaypoints.forEach(waypointCategory -> waypointCategory.waypoints().removeIf(waypoint -> !selectedWaypoints.contains(waypoint)));
            StringSelection exportString = new StringSelection(Waypoints.toSkytilsBase64(exportWaypoints));
            CLIPBOARD.setContents(exportString, exportString);
        }).build());
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
        ((WaypointsScreen) parent).waypoints.clear();
        ((WaypointsScreen) parent).waypoints.putAll(waypoints);
        client.setScreen(parent);
    }
}
