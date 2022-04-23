package com.kevinthegreat.skyblockmod.misc;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class QuiverWarning {
    public boolean on = true;
    private int warning = 0;

    public boolean onChatMessage(String message) {
        if (on && message.endsWith("left in your Quiver!")) {
            MinecraftClient.getInstance().inGameHud.setDefaultTitleFade();
            if (message.startsWith("You only have 50")) {
                MinecraftClient.getInstance().inGameHud.setTitle(Text.of("§cYou only have 50 Arrows left in your Quiver!"));
                if (SkyblockMod.skyblockMod.util.catacombs) {
                    warning = 1;
                }
            } else if (message.startsWith("You only have 10")) {
                MinecraftClient.getInstance().inGameHud.setTitle(Text.of("§cYou only have 10 Arrows left in your Quiver!"));
                if (SkyblockMod.skyblockMod.util.catacombs) {
                    warning = 2;
                }
            } else if (message.startsWith("You don't have any more")) {
                MinecraftClient.getInstance().inGameHud.setTitle(Text.of("§cYou don't have any more Arrows left in your Quiver!"));
                if (SkyblockMod.skyblockMod.util.catacombs) {
                    warning = 3;
                }
            }
            return true;
        }
        return false;
    }

    public void check() {
        if (on && warning != 0 && !SkyblockMod.skyblockMod.util.catacombs) {
            MinecraftClient.getInstance().inGameHud.setDefaultTitleFade();
            switch (warning) {
                case 1 -> MinecraftClient.getInstance().inGameHud.setTitle(Text.of("§cYou only have 50 Arrows left in your Quiver!"));
                case 2 -> MinecraftClient.getInstance().inGameHud.setTitle(Text.of("§cYou only have 10 Arrows left in your Quiver!"));
                case 3 -> MinecraftClient.getInstance().inGameHud.setTitle(Text.of("§cYou don't have any more Arrows left in your Quiver!"));
            }
            warning = 0;
        }
    }
}
