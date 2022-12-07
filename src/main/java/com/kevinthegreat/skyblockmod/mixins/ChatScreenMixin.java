package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    @ModifyVariable(method = "sendMessage",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addToMessageHistory(Ljava/lang/String;)V"),
                    to = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z")
            ),
            at = @At(value = "LOAD", ordinal = 0), argsOnly = true)
    private String skyblockmod_modifyMessage(String message) {
        if (message.startsWith("/")) {
            message = SkyblockMod.skyblockMod.message.commands.getOrDefault(message, message);
            String[] messageArgs = message.split(" ");
            for (int i = 0; i < messageArgs.length; i++) {
                messageArgs[i] = SkyblockMod.skyblockMod.message.commandsArgs.getOrDefault(messageArgs[i], messageArgs[i]);
            }
            return String.join(" ", messageArgs);
        }
        return message;
    }
}
