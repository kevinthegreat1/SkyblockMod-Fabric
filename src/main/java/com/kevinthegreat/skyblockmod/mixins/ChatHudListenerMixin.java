package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.message.MessageSender;
import net.minecraft.network.message.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHudListener.class)
public class ChatHudListenerMixin {
    @Inject(method = "onChatMessage", at = @At(value = "HEAD"))
    private void onChatMessage(MessageType messageType, Text text, MessageSender sender, CallbackInfo ci) {
        String message = text.getString();
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
}
