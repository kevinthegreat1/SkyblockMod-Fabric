package com.kevinthegreat.skyblockmod.dungeons;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.util.ChatListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class LividColor implements ChatListener {
    public boolean on = true;
    public String text = "[color]";
    private int ticks = 0;

    @Override
    public boolean onChatMessage(String message) {
        if (on && message.equals("[BOSS] Livid: I respect you for making it to here, but I'll be your undoing.")) {
            ticks = 80;
        }
        return true;
    }

    public void tick(MinecraftClient minecraftClient) {
        if (ticks != 0) {
            if (on && SkyblockMod.skyblockMod.info.catacombs && minecraftClient.world != null) {
                if (ticks % 10 == 1) {
                    if (ticks == 1) {
                        SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", "red"));
                        ticks = 0;
                        return;
                    }
                    String key = minecraftClient.world.getBlockState(new BlockPos(5, 110, 42)).getBlock().getTranslationKey();
                    if (key.startsWith("block.minecraft.") && key.endsWith("wool") && !key.endsWith("red_wool")) {
                        SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text.replace("[color]", key.substring(16, key.length() - 5)));
                        ticks = 0;
                        return;
                    }
                }
                ticks--;
            } else {
                ticks = 0;
            }
        }
    }
}
