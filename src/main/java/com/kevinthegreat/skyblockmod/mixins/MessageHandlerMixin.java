package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageHandler.class)
public abstract class MessageHandlerMixin {
    @Inject(method = "onGameMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V"))
    private void fabric_onGameMessage(Text text, boolean overlay, CallbackInfo ci) {
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
        SkyblockMod.skyblockMod.reparty.onChatMessage(message);
    }
}
