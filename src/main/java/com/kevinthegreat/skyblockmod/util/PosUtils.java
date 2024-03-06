package com.kevinthegreat.skyblockmod.util;

import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;

public final class PosUtils {
    public static BlockPos parsePosJson(JsonObject posJson) {
        return new BlockPos(posJson.get("x").getAsInt(), posJson.get("y").getAsInt(), posJson.get("z").getAsInt());
    }
}
