package com.kevinthegreat.skyblockmod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class QuiverWarning {
    public boolean on = true;
    private long last = 0;

    public boolean onChatMessage(String message) {
        if (on && message.endsWith("You only have 50 Arrows left in your Quiver!") && last + 2000 < System.currentTimeMillis()) {
            MinecraftClient.getInstance().inGameHud.setDefaultTitleFade();
            MinecraftClient.getInstance().inGameHud.setTitle(Text.of("§cYou only have 50 Arrows left in your Quiver!"));
            last = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
