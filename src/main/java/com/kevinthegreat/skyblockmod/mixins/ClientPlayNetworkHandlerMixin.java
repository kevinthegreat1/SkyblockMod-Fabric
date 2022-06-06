package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onPlaySoundId", at = @At(value = "HEAD"))
    private void onPlaySoundId(PlaySoundIdS2CPacket packet, CallbackInfo ci) {
        if (packet.getSoundId().getPath().equals("entity.generic.splash") || packet.getSoundId().getPath().equals("entity.player.splash")) {
            System.out.println(packet.getSoundId().getPath());
            if (SkyblockMod.skyblockMod.fishing.fishing && System.currentTimeMillis() >= SkyblockMod.skyblockMod.fishing.startTime + 2000) {
                SkyblockMod.skyblockMod.message.addMessage(Text.of("Reel in now!"));
                SkyblockMod.skyblockMod.fishing.fishing = false;
            }
        }
    }
}
