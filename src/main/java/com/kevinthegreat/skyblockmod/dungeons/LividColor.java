package com.kevinthegreat.skyblockmod.dungeons;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class LividColor {
    public String[] text = {"", ""};
    private int ticks = 0;

    public void start() {
        ticks = 80;
    }

    public void tick() {
        if (SkyblockMod.skyblockMod.util.catacombs && MinecraftClient.getInstance().world != null) {
            if (ticks == 1) {
                SkyblockMod.skyblockMod.message.sendMessage(text[0] + "red" + text[1]);
                ticks = 0;
            } else if (ticks % 10 == 1) {
                BlockState blockState = MinecraftClient.getInstance().world.getBlockState(new BlockPos(205, 110, 242));
                if (blockState.isOf(Blocks.BLUE_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessage(text[0] + "blue" + text[1]);
                    ticks = 0;
                } else if (blockState.isOf(Blocks.GRAY_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessage(text[0] + "gray" + text[1]);
                    ticks = 0;
                } else if (blockState.isOf(Blocks.GREEN_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessage(text[0] + "green" + text[1]);
                    ticks = 0;
                } else if (blockState.isOf(Blocks.LIME_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessage(text[0] + "lime" + text[1]);
                    ticks = 0;
                } else if (blockState.isOf(Blocks.MAGENTA_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessage(text[0] + "magenta" + text[1]);
                    ticks = 0;
                } else if (blockState.isOf(Blocks.PURPLE_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessage(text[0] + "purple" + text[1]);
                    ticks = 0;
                } else if (blockState.isOf(Blocks.WHITE_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessage(text[0] + "white" + text[1]);
                    ticks = 0;
                } else if (blockState.isOf(Blocks.YELLOW_WOOL)) {
                    SkyblockMod.skyblockMod.message.sendMessage(text[0] + "yellow" + text[1]);
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
