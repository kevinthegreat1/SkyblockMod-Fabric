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

import java.util.Arrays;

@Mixin(Screen.class)
public class ScreenMixin {
    @ModifyVariable(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "HEAD"), argsOnly = true)
    private String modifyMessage(String message) {
        if (message.startsWith("/")) {
            message = SkyblockMod.skyblockMod.message.commands.getOrDefault(message, message);
            if (message.equals("/reparty")) {
                SkyblockMod.skyblockMod.reparty.start();
                return "/pl";
            }
            String[] messageArgs = message.split(" ");
            for (int i = 0; i < messageArgs.length; i++) {
                messageArgs[i] = SkyblockMod.skyblockMod.message.commandsArgs.getOrDefault(messageArgs[i], messageArgs[i]);
            }
            message = String.join(" ", messageArgs);
        }
        return message;
    }

    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;)V"), cancellable = true)
    private void processMessage(String message, boolean toHud, CallbackInfo ci) {
        if (message.startsWith("/")) {
            String[] messageArgs = message.split(" ");
            if (messageArgs[0].equals("/sbm") && messageArgs.length >= 2) {
                switch (messageArgs[1]) {
                    case "config" -> {
                        if (messageArgs.length == 2) {
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Add reload or save to reload or save config file"));
                        } else switch (messageArgs[2]) {
                            case "reload" -> {
                                SkyblockMod.skyblockMod.config.load();
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Reloaded config file"));
                            }
                            case "save" -> {
                                SkyblockMod.skyblockMod.config.save();
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Saved config file"));
                            }
                        }
                    }
                    case "livid" -> {
                        if (messageArgs.length == 2) {
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of((SkyblockMod.skyblockMod.lividColor.on ? "Livid color is on: " : "Livid color is off: ") + SkyblockMod.skyblockMod.lividColor.text[0] + "[color]" + SkyblockMod.skyblockMod.lividColor.text[1]));
                        } else switch (messageArgs[2]) {
                            case "on", "true" -> {
                                SkyblockMod.skyblockMod.lividColor.on = true;
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Livid color turned on"));
                            }
                            case "off", "false" -> {
                                SkyblockMod.skyblockMod.lividColor.on = false;
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Livid color turned off"));
                            }
                            default -> {
                                SkyblockMod.skyblockMod.lividColor.text = Arrays.copyOf(message.substring(11).split("\\[color]"), 2);
                                if (SkyblockMod.skyblockMod.lividColor.text[0] == null) {
                                    SkyblockMod.skyblockMod.lividColor.text[0] = "";
                                }
                                if (SkyblockMod.skyblockMod.lividColor.text[1] == null) {
                                    SkyblockMod.skyblockMod.lividColor.text[1] = "";
                                }
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Livid color text set to: " + SkyblockMod.skyblockMod.lividColor.text[0] + "[color]" + SkyblockMod.skyblockMod.lividColor.text[1]));
                            }
                        }
                    }
                    case "map" -> {
                        if (messageArgs.length == 2) {
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Map is " + (SkyblockMod.skyblockMod.dungeonMap.on ? "on" : "off")));
                        } else switch (messageArgs[2]) {
                            case "on", "true" -> {
                                SkyblockMod.skyblockMod.dungeonMap.on = true;
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Dungeon map turned on"));
                            }
                            case "off", "false" -> {
                                SkyblockMod.skyblockMod.dungeonMap.on = false;
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Dungeon map turned off"));
                            }
                            case "scale" -> {
                                try {
                                    SkyblockMod.skyblockMod.dungeonMap.mapScale = Float.parseFloat(messageArgs[3]);
                                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Map size set to: " + messageArgs[3]));
                                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Map size: " + SkyblockMod.skyblockMod.dungeonMap.mapScale));
                                }
                            }
                            case "offset" -> {
                                try {
                                    SkyblockMod.skyblockMod.dungeonMap.mapOffsetx = Integer.parseInt(messageArgs[3]);
                                    SkyblockMod.skyblockMod.dungeonMap.mapOffsety = Integer.parseInt(messageArgs[4]);
                                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Map offset set to: " + messageArgs[3] + ", " + messageArgs[4]));
                                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Map offset: " + SkyblockMod.skyblockMod.dungeonMap.mapOffsetx + ", " + SkyblockMod.skyblockMod.dungeonMap.mapOffsety));
                                }
                            }
                            default -> MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Map is " + (SkyblockMod.skyblockMod.dungeonMap.on ? "on" : "off")));
                        }
                    }
                    case "quiver" -> {
                        if (messageArgs.length == 2) {
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Quiver warning is " + (SkyblockMod.skyblockMod.quiverWarning.on ? "on" : "off")));
                        } else switch (messageArgs[2]) {
                            case "on", "true" -> {
                                SkyblockMod.skyblockMod.quiverWarning.on = true;
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Quiver warning turned on"));
                            }
                            case "off", "false" -> {
                                SkyblockMod.skyblockMod.quiverWarning.on = false;
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Quiver warning turned off"));
                            }
                            default -> MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Reparty is " + (SkyblockMod.skyblockMod.reparty.on ? "on" : "off")));
                        }
                    }
                    case "reparty" -> {
                        if (messageArgs.length == 2) {
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Reparty is " + (SkyblockMod.skyblockMod.reparty.on ? "on" : "off")));
                        } else switch (messageArgs[2]) {
                            case "on", "true" -> {
                                SkyblockMod.skyblockMod.reparty.on = true;
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Reparty turned on"));
                            }
                            case "off", "false" -> {
                                SkyblockMod.skyblockMod.reparty.on = false;
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Reparty turned off"));
                            }
                            default -> MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Reparty is " + (SkyblockMod.skyblockMod.reparty.on ? "on" : "off")));
                        }
                    }
                    case "score" -> {
                        if (messageArgs.length == 2) {
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("/sbm score [270 or 300] [on, off, or message]"));
                        } else switch (messageArgs[2]) {
                            case "270" -> {
                                if (messageArgs.length == 3) {
                                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of((SkyblockMod.skyblockMod.dungeonScore.on270 ? "Dungeon score 270 is on: " : "Dungeon score 270 is off: ") + SkyblockMod.skyblockMod.dungeonScore.text270));
                                } else switch (messageArgs[3]) {
                                    case "on", "true" -> {
                                        SkyblockMod.skyblockMod.dungeonScore.on270 = true;
                                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Dungeon score 270 turned on"));
                                    }
                                    case "off", "false" -> {
                                        SkyblockMod.skyblockMod.dungeonScore.on270 = false;
                                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Dungeon score 270 turned off"));
                                    }
                                    default -> {
                                        SkyblockMod.skyblockMod.dungeonScore.text270 = message.substring(15);
                                        if (SkyblockMod.skyblockMod.dungeonScore.text270.isEmpty()) {
                                            SkyblockMod.skyblockMod.dungeonScore.text270 = "270 score";
                                        }
                                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Dungeon score 270 text set to: " + SkyblockMod.skyblockMod.dungeonScore.text270));
                                    }
                                }
                            }
                            case "300" -> {
                                if (messageArgs.length == 3) {
                                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of((SkyblockMod.skyblockMod.dungeonScore.on300 ? "Dungeon score 300 is on: " : "Dungeon score 300 is off: ") + SkyblockMod.skyblockMod.dungeonScore.text300));
                                } else switch (messageArgs[3]) {
                                    case "on", "true" -> {
                                        SkyblockMod.skyblockMod.dungeonScore.on300 = true;
                                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Dungeon score 300 turned on"));
                                    }
                                    case "off", "false" -> {
                                        SkyblockMod.skyblockMod.dungeonScore.on300 = false;
                                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Dungeon score 300 turned off"));
                                    }
                                    default -> {
                                        SkyblockMod.skyblockMod.dungeonScore.text300 = message.substring(15);
                                        if (SkyblockMod.skyblockMod.dungeonScore.text300.isEmpty()) {
                                            SkyblockMod.skyblockMod.dungeonScore.text300 = "300 score";
                                        }
                                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Dungeon score 300 text set to: " + SkyblockMod.skyblockMod.dungeonScore.text300));
                                    }
                                }
                            }
                        }
                    }
                }
                ci.cancel();
            }
        }
    }
}
