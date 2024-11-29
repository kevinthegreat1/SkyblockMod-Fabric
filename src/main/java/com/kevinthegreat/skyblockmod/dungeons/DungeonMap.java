package com.kevinthegreat.skyblockmod.dungeons;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.option.SkyblockModOptions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.MapRenderState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;

public class DungeonMap {
    private final MapIdComponent DEFAULT_MAP_ID_COMPONENT = new MapIdComponent(1024);
    private static final MapRenderState MAP_RENDER_STATE = new MapRenderState();
    private MapIdComponent cachedMapIdComponent = null;

    public void init() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> render(context));
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> reset());
    }

    private void render(DrawContext context) {
        SkyblockModOptions options = SkyblockMod.skyblockMod.options;
        if (!options.dungeonMap.getValue() || !SkyblockMod.skyblockMod.info.catacombs) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        MapIdComponent mapId = getMapIdComponent(client.player.getInventory().main.get(8));
        MapState state = FilledMapItem.getMapState(mapId, client.world);
        if (state == null) return;

        MatrixStack matrices = context.getMatrices();
        float scaling = options.dungeonMapScale.getValue().floatValue();
        VertexConsumerProvider.Immediate vertices = client.getBufferBuilders().getEffectVertexConsumers();

        matrices.push();
        matrices.translate(options.dungeonMapX.getValue(), options.dungeonMapY.getValue(), 0);
        matrices.scale(scaling, scaling, 0f);
        client.getMapRenderer().update(mapId, state, MAP_RENDER_STATE);
        client.getMapRenderer().draw(MAP_RENDER_STATE, matrices, vertices, false, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        vertices.draw();
        matrices.pop();
    }

    public MapIdComponent getMapIdComponent(ItemStack stack) {
        return stack.isOf(Items.FILLED_MAP) && stack.contains(DataComponentTypes.MAP_ID) ? (cachedMapIdComponent = stack.get(DataComponentTypes.MAP_ID)) : cachedMapIdComponent != null ? cachedMapIdComponent : DEFAULT_MAP_ID_COMPONENT;
    }

    private void reset() {
        cachedMapIdComponent = null;
    }
}
