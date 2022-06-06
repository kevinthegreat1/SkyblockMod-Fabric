package com.kevinthegreat.skyblockmod.dungeons;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class LividColor {
    public boolean on = true;
    public String text = "";
    private int ticks = 0;

    public void start() {
        ticks = 80;
    }

    public void tick() {
        if (on && SkyblockMod.skyblockMod.util.catacombs && MinecraftClient.getInstance().world != null) {
            if (ticks == 1) {
                SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", "red"));
                ticks = 0;
            } else if (ticks % 10 == 1) {
                BlockState blockState = MinecraftClient.getInstance().world.getBlockState(new BlockPos(5, 110, 42));
                if (blockState.isOf(Blocks.BLUE_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", "blue"));
                    ticks = 0;
                } else if (blockState.isOf(Blocks.GRAY_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", "gray"));
                    ticks = 0;
                } else if (blockState.isOf(Blocks.GREEN_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", "green"));
                    ticks = 0;
                } else if (blockState.isOf(Blocks.LIME_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", "lime"));
                    ticks = 0;
                } else if (blockState.isOf(Blocks.MAGENTA_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", "magenta"));
                    ticks = 0;
                } else if (blockState.isOf(Blocks.PURPLE_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", "purple"));
                    ticks = 0;
                } else if (blockState.isOf(Blocks.WHITE_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", "white"));
                    ticks = 0;
                } else if (blockState.isOf(Blocks.YELLOW_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", "yellow"));
                    ticks = 0;
                }
            }
            if (ticks > 0) {
                ticks--;
            }
        } else {
            ticks = 0;
        }
    }
}
