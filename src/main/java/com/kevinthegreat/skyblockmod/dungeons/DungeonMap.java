package com.kevinthegreat.skyblockmod.dungeons;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.option.SkyblockModOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;

//from skyfabric
public class DungeonMap {
    public void render(DrawContext context, float tickDelta) {
        SkyblockModOptions options = SkyblockMod.skyblockMod.options;
        if (options.dungeonMap.getValue() && SkyblockMod.skyblockMod.info.catacombs) {
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
            context.getMatrices().push();
            context.getMatrices().translate(options.dungeonMapX.getValue(), options.dungeonMapY.getValue(), 0);
            context.getMatrices().scale(options.dungeonMapScale.getValue().floatValue(), options.dungeonMapScale.getValue().floatValue(), 0);
            minecraftClient.gameRenderer.getMapRenderer().draw(context.getMatrices(), vertices, FilledMapItem.getMapId(itemStack), mapState, false, 15728880);
            vertices.draw();
            context.getMatrices().pop();
        }
    }
}
