package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick()V", at = @At("RETURN"))
    private void onEndTick(CallbackInfo callbackInfo) {
        SkyblockMod.skyblockMod.tick();
    }

    @Inject(method = "stop()V", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void onStopping(CallbackInfo callbackInfo) {
        SkyblockMod.skyblockMod.config.save();
    }
}