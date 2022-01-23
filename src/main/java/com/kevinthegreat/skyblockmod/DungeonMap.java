package com.kevinthegreat.skyblockmod;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;

//from skyfabric
public class DungeonMap {
    public float mapScale = 1;
    public int mapOffsetx = 0;
    public int mapOffsety = 0;

    public DungeonMap() {
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (SkyblockMod.skyblockMod.util.catacombs) {
                MinecraftClient minecraftClient = MinecraftClient.getInstance();
                if (minecraftClient == null || minecraftClient.player == null || minecraftClient.world == null) {
                    return;
                }
                ItemStack itemStack = minecraftClient.player.getInventory().main.get(8);
                if (!itemStack.isOf(Items.FILLED_MAP)) {
                    return;
                }
                MapState mapState = FilledMapItem.getMapState(FilledMapItem.getMapId(itemStack), minecraftClient.world);
                if (mapState == null) {
                    return;
                }
                VertexConsumerProvider.Immediate vertices = minecraftClient.getBufferBuilders().getEffectVertexConsumers();
                matrixStack.push();
                matrixStack.translate(mapOffsetx, mapOffsety, 0);
                matrixStack.scale(mapScale, mapScale, 0);
                minecraftClient.gameRenderer.getMapRenderer().draw(matrixStack, vertices, FilledMapItem.getMapId(itemStack), mapState, false, 15728880);
                vertices.draw();
                matrixStack.pop();
            }
        });
    }
}
