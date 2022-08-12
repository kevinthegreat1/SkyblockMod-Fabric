package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/message/MessageHandler;onChatMessage(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/network/message/MessageType$Parameters;)V"))
    private void onChatMessage(ChatMessageS2CPacket packet, CallbackInfo ci) {
        String message = packet.message().getContent().getString();
        if (SkyblockMod.skyblockMod.dungeonScore.onChatMessage(message)) {
            return;
        }
        if (SkyblockMod.skyblockMod.lividColor.on && message.equals("[BOSS] Livid: I respect you for making it to here, but I'll be your undoing.")) {
            SkyblockMod.skyblockMod.lividColor.start();
            return;
        }
        if (SkyblockMod.skyblockMod.quiverWarning.onChatMessage(message)) {
            return;
        }
        if (SkyblockMod.skyblockMod.reparty.onChatMessage(message)) {
            return;
        }
    }

    @Inject(method = "onPlaySoundId", at = @At(value = "HEAD"))
    private void onPlaySoundId(PlaySoundIdS2CPacket packet, CallbackInfo ci) {
        SkyblockMod.skyblockMod.fishing.onSound(client, packet);
    }
}
