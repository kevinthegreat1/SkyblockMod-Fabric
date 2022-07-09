package com.kevinthegreat.skyblockmod.dungeons;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;

//from skyfabric
public class DungeonMap {
    public boolean on = true;
    public float scale = 1;
    public int offsetX = 0;
    public int offsetY = 0;

    public void render(MatrixStack matrixStack, float tickDelta) {
        if (on && SkyblockMod.skyblockMod.util.catacombs) {
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
            matrixStack.translate(offsetX, offsetY, 0);
            matrixStack.scale(scale, scale, 0);
            minecraftClient.gameRenderer.getMapRenderer().draw(matrixStack, vertices, FilledMapItem.getMapId(itemStack), mapState, false, 15728880);
            vertices.draw();
            matrixStack.pop();
        }
    }
}
