package com.kevinthegreat.skyblockmod.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Fishing {
    public boolean on = true;
    private long startTime;
    private Vec3d normalYawVector;

    public void start(PlayerEntity player) {
        startTime = System.currentTimeMillis();
        float yawRad = (player.getYaw() + 90) * 0.017453292F;
        normalYawVector = new Vec3d(MathHelper.cos(yawRad), 0, MathHelper.sin(yawRad));
    }

    public void reset() {
        startTime = 0;
    }

    public void onSound(MinecraftClient minecraftClient, PlaySoundIdS2CPacket packet) {
        if (on && startTime != 0 && System.currentTimeMillis() >= startTime + 2000 && (packet.getSoundId().getPath().equals("entity.generic.splash") || packet.getSoundId().getPath().equals("entity.player.splash"))) {
            ClientPlayerEntity player = minecraftClient.player;
            if (player != null && player.fishHook != null) {
                Vec3d soundToFishHook = player.fishHook.getPos().subtract(packet.getX(), 0, packet.getZ());
                if (Math.abs(normalYawVector.x * soundToFishHook.z - normalYawVector.z * soundToFishHook.x) < 0.2D && Math.abs(normalYawVector.dotProduct(soundToFishHook)) < 4D && player.getPos().squaredDistanceTo(packet.getX(), packet.getY(), packet.getZ()) > 1D) {
                    minecraftClient.inGameHud.setTitleTicks(0, 10, 5);
                    minecraftClient.inGameHud.setTitle(Text.of("§aReel in now!"));
                    reset();
                }
            } else {
                reset();
            }
        }
    }
}
