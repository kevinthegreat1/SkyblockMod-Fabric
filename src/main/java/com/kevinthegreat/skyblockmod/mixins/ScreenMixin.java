package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @ModifyVariable(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "HEAD"), argsOnly = true)
    private String modifyMessage(String message) {
        if (message.startsWith("/")) {
            message = SkyblockMod.skyblockMod.commands.getOrDefault(message, message);
            if (message.equals("/reparty")) {
                SkyblockMod.skyblockMod.reparty.reparty = true;
                return "/pl";
            }
            String[] messageArgs = message.split(" ");
            for (int i = 0; i < messageArgs.length; i++) {
                messageArgs[i] = SkyblockMod.skyblockMod.commandsArgs.getOrDefault(messageArgs[i], messageArgs[i]);
            }
            message = String.join(" ", messageArgs);
        }
        return message;
    }

    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;)V"), cancellable = true)
    private void processMessage(String message, boolean toHud, CallbackInfo callbackInfo) {
        if (message.startsWith("/")) {
            String[] messageArgs = message.split(" ");
            if (messageArgs[0].equals("/sbm") && messageArgs.length >= 2) {
                if (messageArgs[1].equals("reload")) {
                    SkyblockMod.skyblockMod.load();
                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Reloaded Config File."));
                } else if (messageArgs[1].equals("map") && messageArgs.length >= 3) {
                    if (messageArgs[2].equals("scale")) {
                        try {
                            SkyblockMod.skyblockMod.mapScale = Float.parseFloat(messageArgs[3]);
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Map size set to " + messageArgs[3]));
                        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Map size is " + SkyblockMod.skyblockMod.mapScale));
                        }
                    } else if (messageArgs[2].equals("offset")) {
                        try {
                            SkyblockMod.skyblockMod.mapOffsetx = Integer.parseInt(messageArgs[3]);
                            SkyblockMod.skyblockMod.mapOffsety = Integer.parseInt(messageArgs[4]);
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Map offset set to " + messageArgs[3] + ", " + messageArgs[4]));
                        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Map offset is " + SkyblockMod.skyblockMod.mapOffsetx + ", " + SkyblockMod.skyblockMod.mapOffsety));
                        }
                    }
                }
                callbackInfo.cancel();
            }
        }
    }
}
