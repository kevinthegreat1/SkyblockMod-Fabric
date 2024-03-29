package com.kevinthegreat.skyblockmod.waypoint;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.mixins.CheckboxWidgetAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WaypointsListWidget extends ElementListWidget<WaypointsListWidget.AbstractWaypointEntry> {
    private final AbstractWaypointsScreen<?> screen;
    private final String island;
    private final List<WaypointCategory> waypoints;

    public WaypointsListWidget(MinecraftClient client, AbstractWaypointsScreen<?> screen, int width, int height, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
        this.screen = screen;
        island = SkyblockMod.skyblockMod.info.locationRaw;
        waypoints = (List<WaypointCategory>) screen.waypoints.get(island);
        updateEntries();
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 100;
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 50;
    }

    Optional<WaypointCategoryEntry> getCategory() {
        if (getSelectedOrNull() instanceof WaypointCategoryEntry category) {
            return Optional.of(category);
        } else if (getSelectedOrNull() instanceof WaypointEntry waypointEntry) {
            return Optional.of(waypointEntry.category);
        }
        return Optional.empty();
    }

    void addWaypointCategoryAfterSelected() {
        WaypointCategoryEntry categoryEntry = new WaypointCategoryEntry();
        Optional<WaypointCategoryEntry> selectedCategoryEntryOptional = getCategory();
        int index = waypoints.size();
        int entryIndex = children().size();
        if (selectedCategoryEntryOptional.isPresent()) {
            WaypointCategoryEntry selectedCategoryEntry = selectedCategoryEntryOptional.get();
            index = waypoints.indexOf(selectedCategoryEntry.category) + 1;
            entryIndex = children().indexOf(selectedCategoryEntry) + 1;
            while (entryIndex < children().size() && !(children().get(entryIndex) instanceof WaypointCategoryEntry)) {
                entryIndex++;
            }
        }
        waypoints.add(index, categoryEntry.category);
        children().add(entryIndex, categoryEntry);
    }

    void updateEntries() {
        clearEntries();
        for (WaypointCategory category : waypoints) {
            WaypointCategoryEntry categoryEntry = new WaypointCategoryEntry(category);
            addEntry(categoryEntry);
            for (NamedWaypoint waypoint : category.waypoints()) {
                addEntry(new WaypointEntry(categoryEntry, waypoint));
            }
        }
    }

    void updateButtons() {
        for (Entry<AbstractWaypointEntry> entry : children()) {
            if (entry instanceof WaypointCategoryEntry categoryEntry && categoryEntry.enabled.isChecked() != categoryEntry.category.waypoints().stream().allMatch(screen::isEnabled)) {
                ((CheckboxWidgetAccessor) categoryEntry.enabled).setChecked(!categoryEntry.enabled.isChecked());
            } else if (entry instanceof WaypointEntry waypointEntry && waypointEntry.enabled.isChecked() != screen.isEnabled(waypointEntry.waypoint)) {
                waypointEntry.enabled.onPress();
            }
        }
    }

    protected static abstract class AbstractWaypointEntry extends ElementListWidget.Entry<AbstractWaypointEntry> {
    }

    protected class WaypointCategoryEntry extends AbstractWaypointEntry {
        private final WaypointCategory category;
        private final List<ClickableWidget> children;
        private final CheckboxWidget enabled;
        private final ButtonWidget buttonNewWaypoint;
        private final ButtonWidget buttonDelete;

        public WaypointCategoryEntry() {
            this(new WaypointCategory("New Category", island, new ArrayList<>()));
        }

        public WaypointCategoryEntry(WaypointCategory category) {
            this.category = category;
            enabled = CheckboxWidget.builder(Text.literal(""), client.textRenderer).checked(!category.waypoints().isEmpty() && category.waypoints().stream().allMatch(screen::isEnabled)).callback((checkbox, checked) -> category.waypoints().forEach(waypoint -> screen.enabledChanged(waypoint, checked))).build();
            buttonNewWaypoint = ButtonWidget.builder(Text.translatable("skyblocker.waypoints.new"), buttonNewWaypoint -> {
                WaypointEntry waypointEntry = new WaypointEntry(this);
                int entryIndex;
                if (getSelectedOrNull() instanceof WaypointEntry selectedWaypointEntry && selectedWaypointEntry.category == this) {
                    entryIndex = WaypointsListWidget.this.children().indexOf(selectedWaypointEntry) + 1;
                } else {
                    entryIndex = WaypointsListWidget.this.children().indexOf(this) + 1;
                    while (entryIndex < WaypointsListWidget.this.children().size() && !(WaypointsListWidget.this.children().get(entryIndex) instanceof WaypointCategoryEntry)) {
                        entryIndex++;
                    }
                }
                category.waypoints().add(waypointEntry.waypoint);
                WaypointsListWidget.this.children().add(entryIndex, waypointEntry);
            }).width(75).build();
            buttonDelete = ButtonWidget.builder(Text.translatable("selectServer.deleteButton"), buttonDelete -> {
                int entryIndex = WaypointsListWidget.this.children().indexOf(this) + 1;
                while (entryIndex < WaypointsListWidget.this.children().size() && !(WaypointsListWidget.this.children().get(entryIndex) instanceof WaypointCategoryEntry)) {
                    WaypointsListWidget.this.children().remove(entryIndex);
                }
                WaypointsListWidget.this.children().remove(this);
                waypoints.remove(category);
            }).width(50).build();
            children = List.of(enabled, buttonNewWaypoint, buttonDelete);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return children;
        }

        @Override
        public List<? extends Element> children() {
            return children;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawTextWithShadow(client.textRenderer, category.name(), x + 25, y + 5, 0xFFFFFF);
            enabled.setPosition(x, y);
            buttonNewWaypoint.setPosition(x + entryWidth - 133, y);
            buttonDelete.setPosition(x + entryWidth - 54, y);
            enabled.render(context, mouseX, mouseY, tickDelta);
            buttonNewWaypoint.render(context, mouseX, mouseY, tickDelta);
            buttonDelete.render(context, mouseX, mouseY, tickDelta);
        }
    }

    protected class WaypointEntry extends AbstractWaypointEntry {
        private final WaypointCategoryEntry category;
        private final NamedWaypoint waypoint;
        private final List<ClickableWidget> children;
        private final CheckboxWidget enabled;
        private final ButtonWidget buttonDelete;

        public WaypointEntry(WaypointCategoryEntry category) {
            this(category, new NamedWaypoint(BlockPos.ORIGIN, "New Waypoint", new float[]{0f, 1f, 0f}));
        }

        public WaypointEntry(WaypointCategoryEntry category, NamedWaypoint waypoint) {
            this.category = category;
            this.waypoint = waypoint;
            enabled = CheckboxWidget.builder(Text.literal(""), client.textRenderer).checked(screen.isEnabled(waypoint)).callback((checkbox, checked) -> screen.enabledChanged(waypoint, checked)).build();
            buttonDelete = ButtonWidget.builder(Text.translatable("selectServer.deleteButton"), button -> {
                category.category.waypoints().remove(waypoint);
                WaypointsListWidget.this.children().remove(this);
            }).width(50).build();
            children = List.of(enabled, buttonDelete);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return children;
        }

        @Override
        public List<? extends Element> children() {
            return children;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawTextWithShadow(client.textRenderer, waypoint.getName(), x + 35, y + 5, 0xFFFFFF);
            context.drawTextWithShadow(client.textRenderer, waypoint.pos.toShortString(), width / 2 - 25, y + 5, 0xFFFFFF);
            float[] colorComponents = waypoint.getColorComponents();
            context.drawTextWithShadow(client.textRenderer, String.format("#%02X%02X%02X", (int) (colorComponents[0] * 255), (int) (colorComponents[1] * 255), (int) (colorComponents[2] * 255)), width / 2 + 25, y + 5, 0xFFFFFF);
            enabled.setPosition(x + 10, y);
            buttonDelete.setPosition(x + entryWidth - 54, y);
            enabled.render(context, mouseX, mouseY, tickDelta);
            buttonDelete.render(context, mouseX, mouseY, tickDelta);
        }
    }
}
