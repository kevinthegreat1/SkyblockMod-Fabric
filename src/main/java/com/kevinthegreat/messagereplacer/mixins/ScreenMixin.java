package com.kevinthegreat.messagereplacer.mixins;

import com.kevinthegreat.messagereplacer.MessageReplacer;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Screen.class)
public class ScreenMixin {
    @ModifyVariable(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "HEAD"))
    private String modifyMessage(String message) {
        if (message.startsWith("/")) {
            message = MessageReplacer.commands.getOrDefault(message, message);
            if (message.equals("/reparty")) {
                MessageReplacer.reparty.startReparty();
                return "/pl";
            }
            String[] messageArgs = message.split(" ");
            for (int i = 0; i < messageArgs.length; i++) {
                messageArgs[i] = MessageReplacer.commandsArgs.getOrDefault(messageArgs[i], messageArgs[i]);
            }
            message = String.join(" ", messageArgs);
        }
        return message;
    }
}
