package com.kevinthegreat.skyblockmod.dungeons;

import com.kevinthegreat.skyblockmod.SkyblockMod;

public class DungeonScore {
    public boolean on270 = true;
    public String text270 = "270 score";
    private long last270 = 0;
    public boolean on300 = true;
    public String text300 = "300 score";
    private long last300 = 0;

    public boolean onChatMessage(String message) {
        if (on270 && SkyblockMod.skyblockMod.info.catacombs && ((message.contains("Skytils-SC > ") && message.contains("270")) || message.endsWith("Skytils > 270 score") || message.endsWith("270 Score Reached!")) && last270 + 2000 < System.currentTimeMillis()) {
            SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text270);
            last270 = System.currentTimeMillis();
            return true;
        }
        if (on300 && SkyblockMod.skyblockMod.info.catacombs && ((message.contains("Skytils-SC > ") && message.contains("300")) || message.endsWith("Skytils > 300 score") || message.endsWith("300 Score Reached!")) && last300 + 2000 < System.currentTimeMillis()) {
            SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(text300);
            last300 = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
