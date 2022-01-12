package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatHudListener.class)
public class ChatHudListenerMixin {
    @ModifyVariable(method = "onChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V", at = @At(value = "HEAD"), argsOnly = true)
    private Text onChatMessage(Text message) {
        SkyblockMod.skyblockMod.reparty.onChatMessage(message.getString());
        return message;
    }
}
