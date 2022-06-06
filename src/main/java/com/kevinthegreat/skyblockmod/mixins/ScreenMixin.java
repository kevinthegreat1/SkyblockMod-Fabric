package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Screen.class)
public class ScreenMixin {

    @ModifyArg(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;)V"))
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
