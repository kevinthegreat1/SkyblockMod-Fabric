package com.kevinthegreat.skyblockmod.waypoint;

import com.google.common.primitives.Floats;
import com.google.gson.JsonObject;
import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.kevinthegreat.skyblockmod.util.PosUtils;
import com.kevinthegreat.skyblockmod.util.RenderHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.function.Supplier;

public class NamedWaypoint extends Waypoint {
    public static final Codec<NamedWaypoint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(secretWaypoint -> secretWaypoint.pos),
            TextCodecs.CODEC.fieldOf("name").forGetter(secretWaypoint -> secretWaypoint.name),
            Codec.floatRange(0, 1).listOf().comapFlatMap(
                    colorComponentsList -> colorComponentsList.size() == 3 ? DataResult.success(Floats.toArray(colorComponentsList)) : DataResult.error(() -> "Expected 3 color components, got " + colorComponentsList.size() + " instead"),
                    Floats::asList
            ).fieldOf("colorComponents").forGetter(secretWaypoint -> secretWaypoint.colorComponents),
            Codec.BOOL.fieldOf("shouldRender").forGetter(Waypoint::shouldRender)
    ).apply(instance, NamedWaypoint::new));
    protected final Text name;
    protected final Vec3d centerPos;

    public NamedWaypoint(BlockPos pos, String name, float[] colorComponents) {
        this(pos, name, colorComponents, true);
    }

    public NamedWaypoint(BlockPos pos, String name, float[] colorComponents, boolean shouldRender) {
        this(pos, Text.of(name), colorComponents, shouldRender);
    }

    public NamedWaypoint(BlockPos pos, Text name, float[] colorComponents, boolean shouldRender) {
        this(pos, name, () -> SkyblockMod.skyblockMod.options.waypointType.getValue(), colorComponents, shouldRender);
    }

    public NamedWaypoint(BlockPos pos, String name, Supplier<Type> typeSupplier, float[] colorComponents, boolean shouldRender) {
        this(pos, Text.of(name), typeSupplier, colorComponents, shouldRender);
    }

    public NamedWaypoint(BlockPos pos, Text name, Supplier<Type> typeSupplier, float[] colorComponents) {
        this(pos, name, typeSupplier, colorComponents, true);
    }

    public NamedWaypoint(BlockPos pos, Text name, Supplier<Type> typeSupplier, float[] colorComponents, boolean shouldRender) {
        super(pos, typeSupplier, colorComponents, DEFAULT_HIGHLIGHT_ALPHA, DEFAULT_LINE_WIDTH, true, shouldRender);
        this.name = name;
        this.centerPos = pos.toCenterPos();
    }

    public static NamedWaypoint fromSkytilsJson(JsonObject waypointJson) {
        int color = waypointJson.get("color").getAsInt();
        return new NamedWaypoint(PosUtils.parsePosJson(waypointJson), waypointJson.get("name").getAsString(), () -> SkyblockMod.skyblockMod.options.waypointType.getValue(), new float[]{((color & 0x00FF0000) >> 16) / 255f, ((color & 0x0000FF00) >> 8) / 255f, (color & 0x000000FF) / 255f}, waypointJson.get("enabled").getAsBoolean());
    }

    public Text getName() {
        return name;
    }

    protected boolean shouldRenderName() {
        return true;
    }

    @Override
    public void render(WorldRenderContext context) {
        super.render(context);
        if (shouldRenderName()) {
            RenderHelper.renderText(context, name, centerPos.add(0, 1, 0), true);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof NamedWaypoint waypoint && name.equals(waypoint.name);
    }
}
