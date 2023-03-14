package com.kevinthegreat.skyblockmod.dungeons;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.option.SkyblockModOptions;
import com.kevinthegreat.skyblockmod.util.ChatListener;

public class DungeonScore implements ChatListener {
    private long last270 = 0;
    private long last300 = 0;

    @Override
    public boolean onChatMessage(String message) {
        SkyblockModOptions options = SkyblockMod.skyblockMod.options;
        if (options.dungeonScore270.getValue() && SkyblockMod.skyblockMod.info.catacombs && ((message.contains("Skytils-SC > ") && message.contains("270")) || message.endsWith("Skytils > 270 score") || message.endsWith("270 Score Reached!")) && last270 + 2000 < System.currentTimeMillis()) {
            SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(options.dungeonScore270Text.getValue());
            last270 = System.currentTimeMillis();
        }
        if (options.dungeonScore300.getValue() && SkyblockMod.skyblockMod.info.catacombs && ((message.contains("Skytils-SC > ") && message.contains("300")) || message.endsWith("Skytils > 300 score") || message.endsWith("300 Score Reached!")) && last300 + 2000 < System.currentTimeMillis()) {
            SkyblockMod.skyblockMod.message.sendMessageAfterCooldown(options.dungeonScore300Text.getValue());
            last300 = System.currentTimeMillis();
        }
        return true;
    }
}
