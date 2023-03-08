package com.kevinthegreat.skyblockmod.util;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.option.SkyblockModOptions;
import com.kevinthegreat.skyblockmod.screen.SkyblockModOptionsScreen;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Commands {
    public void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        for (String key : SkyblockMod.skyblockMod.message.commands.keySet()) {
            if (key.startsWith("/")) {
                dispatcher.register(literal(key.substring(1)));
            }
        }
        for (String key : SkyblockMod.skyblockMod.message.commandsArgs.keySet()) {
            if (key.startsWith("/")) {
                dispatcher.register(literal(key.substring(1)));
            }
        }
        dispatcher.register(literal("reparty").executes(context -> {
            SkyblockMod.skyblockMod.reparty.start();
            SkyblockMod.skyblockMod.message.sendMessageAfterCooldown("/pl");
            context.getSource().sendFeedback(Text.translatable("skyblockmod:reparty.repartying"));
            return 1;
        }));
        SkyblockModOptions options = SkyblockMod.skyblockMod.options;
        dispatcher.register(literal("sbm")
                .then(literal("config").executes(context -> {
                            // Don't immediately open the next screen as it will be closed by ChatScreen right after this command is executed
                            SkyblockMod.skyblockMod.setNextScreen(new SkyblockModOptionsScreen());
                            return 1;
                        })
                        .then(literal("reload").executes(context -> {
                            options.load();
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:options.reloaded"));
                            return 1;
                        }))
                        .then(literal("save").executes(context -> {
                            options.save();
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:options.saved"));
                            return 1;
                        })))
                .then(literal("dungeonMap").executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap").append(queryOnOrOff(options.dungeonMap.getValue())));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            options.dungeonMap.setValue(BoolArgumentType.getBool(context, "value"));
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap").append(turnOnOrOff(options.dungeonMap.getValue())));
                            return 1;
                        }))
                        .then(literal("offset").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap.offset").append(Text.translatable("skyblockmod:options.query")).append(options.dungeonMapX.getValue() + ", " + options.dungeonMapY.getValue()));
                                    return 1;
                                })
                                .then(argument("offsetX", IntegerArgumentType.integer()).executes(context -> {
                                            options.dungeonMapX.setValue(IntegerArgumentType.getInteger(context, "offsetX"));
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap.offset").append(Text.translatable("skyblockmod:options.set")).append(options.dungeonMapX.getValue() + ", " + options.dungeonMapY.getValue()));
                                            return 1;
                                        })
                                        .then(argument("offsetY", IntegerArgumentType.integer()).executes(context -> {
                                            options.dungeonMapX.setValue(IntegerArgumentType.getInteger(context, "offsetX"));
                                            options.dungeonMapY.setValue(IntegerArgumentType.getInteger(context, "offsetY"));
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap.offset").append(Text.translatable("skyblockmod:options.set")).append(options.dungeonMapX.getValue() + ", " + options.dungeonMapY.getValue()));
                                            return 1;
                                        }))))
                        .then(literal("scale").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap.scale").append(Text.translatable("skyblockmod:options.query")).append(String.format("%.2f", options.dungeonMapScale.getValue())));
                                    return 1;
                                })
                                .then(argument("scale", DoubleArgumentType.doubleArg()).executes(context -> {
                                    options.dungeonMapScale.setValue(DoubleArgumentType.getDouble(context, "scale"));
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap.scale").append(Text.translatable("skyblockmod:options.set")).append(String.format("%.2f", options.dungeonMapScale.getValue())));
                                    return 1;
                                }))))
                .then(literal("dungeonScore").executes(context -> {
                            context.getSource().sendFeedback(Text.of("/sbm score [270|300] [on|off|message]"));
                            return 1;
                        })
                        .then(literal("270").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.270").append(queryOnOrOff(options.dungeonScore270.getValue())).append(": " + options.dungeonScore270Text.getValue()));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    options.dungeonScore270.setValue(BoolArgumentType.getBool(context, "value"));
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.270").append(turnOnOrOff(options.dungeonScore270.getValue())).append(": " + options.dungeonScore270Text.getValue()));
                                    return 1;
                                }))
                                .then(literal("message").executes(context -> {
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.270.text").append(queryOnOrOff(options.dungeonScore270.getValue())).append(": " + options.dungeonScore270Text.getValue()));
                                            return 1;
                                        })
                                        .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                            options.dungeonScore270Text.setValue(StringArgumentType.getString(context, "message"));
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.270.text").append(Text.translatable("skyblockmod:options.set")).append(ScreenTexts.onOrOff(options.dungeonScore270.getValue())).append(": " + options.dungeonScore270Text.getValue()));
                                            return 1;
                                        }))))
                        .then(literal("300").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.300").append(queryOnOrOff(options.dungeonScore300.getValue())).append(": " + options.dungeonScore300Text.getValue()));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    options.dungeonScore300.setValue(BoolArgumentType.getBool(context, "value"));
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.300").append(turnOnOrOff(options.dungeonScore300.getValue())).append(": " + options.dungeonScore300Text.getValue()));
                                    return 1;
                                }))
                                .then(literal("message").executes(context -> {
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.300.text").append(queryOnOrOff(options.dungeonScore300.getValue())).append(": " + options.dungeonScore300Text.getValue()));
                                            return 1;
                                        })
                                        .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                            options.dungeonScore300Text.setValue(StringArgumentType.getString(context, "message"));
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.300.text").append(Text.translatable("skyblockmod:options.set")).append(ScreenTexts.onOrOff(options.dungeonScore300.getValue())).append(": " + options.dungeonScore300Text.getValue()));
                                            return 1;
                                        })))))
                .then(literal("experiments").executes(context -> {
                            context.getSource().sendFeedback(Text.of("/sbm experiments [chronomatron|superpairs|ultrasequencer] [on|off]"));
                            return 1;
                        })
                        .then(literal("chronomatron").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.chronomatron").append(queryOnOrOff(options.experimentChronomatron.getValue())));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    options.experimentChronomatron.setValue(BoolArgumentType.getBool(context, "value"));
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.chronomatron").append(turnOnOrOff(options.experimentChronomatron.getValue())));
                                    return 1;
                                })))
                        .then(literal("superpairs").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.superpairs").append(queryOnOrOff(options.experimentSuperpairs.getValue())));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    options.experimentSuperpairs.setValue(BoolArgumentType.getBool(context, "value"));
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.superpairs").append(turnOnOrOff(options.experimentSuperpairs.getValue())));
                                    return 1;
                                })))
                        .then(literal("ultrasequencer").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.ultrasequencer").append(queryOnOrOff(options.experimentUltrasequencer.getValue())));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    options.experimentUltrasequencer.setValue(BoolArgumentType.getBool(context, "value"));
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.ultrasequencer").append(turnOnOrOff(options.experimentUltrasequencer.getValue())));
                                    return 1;
                                }))))
                .then(literal("fairySouls").executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:fairySouls").append(queryOnOrOff(options.fairySouls.getValue())));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            options.fairySouls.setValue(BoolArgumentType.getBool(context, "value"));
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:fairySouls").append(turnOnOrOff(options.fairySouls.getValue())));
                            return 1;
                        }))
                        .then(literal("markAllInCurrentIslandFound").executes(context -> {
                            SkyblockMod.skyblockMod.fairySouls.markAllFairiesFound();
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:fairySouls.markAllFound"));
                            return 1;
                        }))
                        .then(literal("markAllInCurrentIslandMissing").executes(context -> {
                            SkyblockMod.skyblockMod.fairySouls.markAllFairiesNotFound();
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:fairySouls.markAllMissing"));
                            return 1;
                        })))
                .then(literal("fishingHelper").executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:fishingHelper").append(queryOnOrOff(options.fishing.getValue())));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            options.fishing.setValue(BoolArgumentType.getBool(context, "value"));
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:fishingHelper").append(turnOnOrOff(options.fishing.getValue())));
                            return 1;
                        })))
                .then(literal("help").executes(context -> {
                    FabricClientCommandSource source = context.getSource();
                    for (Map.Entry<String, String> command : SkyblockMod.skyblockMod.message.commandsAll.entrySet()) {
                        source.sendFeedback(Text.of(command.getKey() + " -> " + command.getValue()));
                    }
                    for (String command : dispatcher.getSmartUsage(dispatcher.getRoot().getChild("sbm"), source).values()) {
                        source.sendFeedback(Text.of("/sbm " + command));
                    }
                    return 1;
                }))
                .then(literal("lividColor").executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:lividColor").append(queryOnOrOff(options.lividColor.getValue())).append(": " + options.lividColorText.getValue()));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            options.lividColor.setValue(BoolArgumentType.getBool(context, "value"));
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:lividColor").append(turnOnOrOff(options.lividColor.getValue())).append(": " + options.lividColorText.getValue()));
                            return 1;
                        }))
                        .then(literal("message").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:lividColor").append(queryOnOrOff(options.lividColor.getValue())).append(": " + options.lividColorText.getValue()));
                                    return 1;
                                })
                                .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                    options.lividColorText.setValue(StringArgumentType.getString(context, "message"));
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:lividColor").append(Text.translatable("skyblockmod:options.set")).append(ScreenTexts.onOrOff(options.lividColor.getValue())).append(": " + options.lividColorText.getValue()));
                                    return 1;
                                }))))
                .then(literal("quiverWarning").executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:quiver").append(queryOnOrOff(options.quiver.getValue())));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            options.quiver.setValue(BoolArgumentType.getBool(context, "value"));
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:quiver").append(turnOnOrOff(options.quiver.getValue())));
                            return 1;
                        })))
                .then(literal("reparty").executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:reparty").append(queryOnOrOff(options.reparty.getValue())));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            options.reparty.setValue(BoolArgumentType.getBool(context, "value"));
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:reparty").append(turnOnOrOff(options.reparty.getValue())));
                            return 1;
                        }))));
    }

    private Text queryOnOrOff(boolean on) {
        return Text.translatable("skyblockmod:options.query").append(ScreenTexts.onOrOff(on));
    }

    private Text turnOnOrOff(boolean on) {
        return Text.translatable("skyblockmod:options.turn").append(ScreenTexts.onOrOff(on));
    }
}
