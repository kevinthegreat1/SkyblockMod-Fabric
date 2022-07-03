package com.kevinthegreat.skyblockmod.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

public class Fishing {
    public boolean on = true;
    private long startTime;
    private Vec2f normalYawVector;

    public void start(PlayerEntity player) {
        startTime = System.currentTimeMillis();
        float yawRad = (player.getYaw() + 90) * 0.017453292F;
        normalYawVector = new Vec2f(MathHelper.cos(yawRad), MathHelper.sin(yawRad));
    }

    public void reset() {
        startTime = 0;
    }

    public void onSound(PlaySoundIdS2CPacket packet) {
        if (on && startTime != 0 && System.currentTimeMillis() >= startTime + 2000 && (packet.getSoundId().getPath().equals("entity.generic.splash") || packet.getSoundId().getPath().equals("entity.player.splash"))) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                FishingBobberEntity fishHook = player.fishHook;
                if (fishHook != null) {
                    Vec2f hookToSound = new Vec2f((float) (packet.getX() - fishHook.getX()), (float) (packet.getZ() - fishHook.getZ()));
                    if (MathHelper.abs(normalYawVector.x * hookToSound.y - normalYawVector.y * hookToSound.x) < 0.2F && MathHelper.abs(normalYawVector.dot(hookToSound)) < 4 && player.getPos().squaredDistanceTo(packet.getX(), packet.getY(), packet.getZ()) > 1) {
                        MinecraftClient.getInstance().inGameHud.setTitleTicks(2, 10, 4);
                        MinecraftClient.getInstance().inGameHud.setTitle(Text.of("§aReel in now!"));
                        reset();
                    }
                } else {
                    reset();
                }
            } else {
                reset();
            }
        }
    }
}
