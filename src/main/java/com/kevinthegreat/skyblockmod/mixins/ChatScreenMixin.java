package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @ModifyVariable(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "LOAD", ordinal = 3), argsOnly = true)
    private String modifyMessage(String message) {
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
