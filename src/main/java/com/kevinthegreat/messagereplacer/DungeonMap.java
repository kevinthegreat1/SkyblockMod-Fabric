package com.kevinthegreat.messagereplacer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;

//from skyfabric
public class DungeonMap {
    public static void register() {
        HudRenderCallback.EVENT.register(((matrixStack, tickDelta) -> {
            if (MessageReplacer.messageReplacer.util.catacombs) {
                MinecraftClient minecraftClient = MinecraftClient.getInstance();
                if (minecraftClient == null || minecraftClient.player == null || minecraftClient.world == null) {
                    return;
                }
                ItemStack itemStack = minecraftClient.player.getInventory().main.get(8);
                NbtCompound nbtCompound = itemStack.getNbt();
                if (nbtCompound == null || !nbtCompound.contains("map")) {
                    return;
                }
                MapState mapState = FilledMapItem.getMapState(FilledMapItem.getMapId(itemStack), minecraftClient.world);
                if (mapState == null) {
                    return;
                }
                VertexConsumerProvider.Immediate vertices = minecraftClient.getBufferBuilders().getEffectVertexConsumers();
                matrixStack.push();
                matrixStack.translate(MessageReplacer.messageReplacer.mapOffsetx, MessageReplacer.messageReplacer.mapOffsety, 0);
                matrixStack.scale(MessageReplacer.messageReplacer.mapScale, MessageReplacer.messageReplacer.mapScale, 0);
                minecraftClient.gameRenderer.getMapRenderer().draw(matrixStack, vertices, FilledMapItem.getMapId(itemStack), mapState, false, 15728880);
                vertices.draw();
                matrixStack.pop();
            }
        }));
    }
}
