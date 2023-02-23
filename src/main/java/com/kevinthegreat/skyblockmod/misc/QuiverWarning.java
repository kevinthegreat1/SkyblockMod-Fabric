package com.kevinthegreat.skyblockmod.misc;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.util.ChatListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class QuiverWarning implements ChatListener {
    public boolean on = true;
    private int warning = 0;

    @Override
    public boolean onChatMessage(String message) {
        if (on && message.endsWith("left in your Quiver!")) {
            MinecraftClient.getInstance().inGameHud.setDefaultTitleFade();
            if (message.startsWith("You only have 50")) {
                MinecraftClient.getInstance().inGameHud.setTitle(Text.translatable("skyblockmod:quiver.50Left").formatted(Formatting.RED));
                if (SkyblockMod.skyblockMod.info.catacombs) {
                    warning = 1;
                }
            } else if (message.startsWith("You only have 10")) {
                MinecraftClient.getInstance().inGameHud.setTitle(Text.translatable("skyblockmod:quiver.10Left").formatted(Formatting.RED));
                if (SkyblockMod.skyblockMod.info.catacombs) {
                    warning = 2;
                }
            } else if (message.startsWith("You don't have any more")) {
                MinecraftClient.getInstance().inGameHud.setTitle(Text.translatable("skyblockmod:quiver.empty").formatted(Formatting.RED));
                if (SkyblockMod.skyblockMod.info.catacombs) {
                    warning = 3;
                }
            }
        }
        return true;
    }

    public void check(MinecraftClient minecraftClient) {
        if (on && warning != 0 && !SkyblockMod.skyblockMod.info.catacombs) {
            minecraftClient.inGameHud.setDefaultTitleFade();
            switch (warning) {
                case 1 ->
                        minecraftClient.inGameHud.setTitle(Text.translatable("skyblockmod:quiver.50Left").formatted(Formatting.RED));
                case 2 ->
                        minecraftClient.inGameHud.setTitle(Text.translatable("skyblockmod:quiver.10Left").formatted(Formatting.RED));
                case 3 ->
                        minecraftClient.inGameHud.setTitle(Text.translatable("skyblockmod:quiver.empty").formatted(Formatting.RED));
            }
            warning = 0;
        }
    }
}
