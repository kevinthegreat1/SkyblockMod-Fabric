package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onPlaySound", at = @At("RETURN"))
    private void skyblockmod_onPlaySound(PlaySoundS2CPacket packet, CallbackInfo ci) {
        SkyblockMod.skyblockMod.fishing.onSound(packet);
    }

    @Inject(method = "onParticle", at = @At("RETURN"))
    private void skyblockmod_onParticle(ParticleS2CPacket packet, CallbackInfo ci) {
        SkyblockMod.skyblockMod.mythologicalRitual.onParticle(packet);
    }
}
