package com.kevinthegreat.skyblockmod.misc;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Fishing {
    private long startTime;
    private Vec3d normalYawVector;

    public void start(PlayerEntity player) {
        startTime = System.currentTimeMillis();
        float yawRad = player.getYaw() * 0.017453292F;
        normalYawVector = new Vec3d(-MathHelper.sin(yawRad), 0, MathHelper.cos(yawRad));
    }

    public void reset() {
        startTime = 0;
    }

    public void onSound(PlaySoundS2CPacket packet) {
        MinecraftClient client = MinecraftClient.getInstance();
        String path = packet.getSound().value().getId().getPath();
        if (SkyblockMod.skyblockMod.options.fishing.getValue() && startTime != 0 && System.currentTimeMillis() >= startTime + 2000 && ("entity.generic.splash".equals(path) || "entity.player.splash".equals(path))) {
            ClientPlayerEntity player = client.player;
            if (player != null && player.fishHook != null) {
                Vec3d soundToFishHook = player.fishHook.getPos().subtract(packet.getX(), 0, packet.getZ());
                if (Math.abs(normalYawVector.x * soundToFishHook.z - normalYawVector.z * soundToFishHook.x) < 0.2D && Math.abs(normalYawVector.dotProduct(soundToFishHook)) < 4D && player.getPos().squaredDistanceTo(packet.getX(), packet.getY(), packet.getZ()) > 1D) {
                    client.inGameHud.setTitleTicks(0, 10, 5);
                    client.inGameHud.setTitle(Text.translatable("skyblockmod:fishing.reelNow").formatted(Formatting.GREEN));
                    reset();
                }
            } else {
                reset();
            }
        }
    }
}
