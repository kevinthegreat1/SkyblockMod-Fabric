package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ChatHudListener.class)
public class ChatHudListenerMixin {
    @Inject(method = "onChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V", at = @At(value = "HEAD"))
    private void onChatMessage(MessageType messageType, Text text, UUID sender, CallbackInfo ci) {
        String message = text.getString();
        if(message.equals("[BOSS] Livid: I respect you for making it to here, but I'll be your undoing.")){
            SkyblockMod.skyblockMod.lividColor.start();
        }
        SkyblockMod.skyblockMod.reparty.onChatMessage(message);
    }
}
