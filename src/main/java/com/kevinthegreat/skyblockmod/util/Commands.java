package com.kevinthegreat.skyblockmod.util;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
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
        dispatcher.register(literal("sbm")
                .then(literal("config").executes(context -> {
                            context.getSource().sendFeedback(Text.of("/sbm config [save|load]"));
                            return 1;
                        })
                        .then(literal("reload").executes(context -> {
                            SkyblockMod.skyblockMod.options.load();
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:options.reloaded"));
                            return 1;
                        }))
                        .then(literal("save").executes(context -> {
                            SkyblockMod.skyblockMod.options.save();
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:options.saved"));
                            return 1;
                        })))
                .then(literal("dungeonMap").executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap").append(queryOnOrOff(SkyblockMod.skyblockMod.options.dungeonMap.getValue())));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.options.dungeonMap.setValue(BoolArgumentType.getBool(context, "value"));
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap").append(turnOnOrOff(SkyblockMod.skyblockMod.options.dungeonMap.getValue())));
                            return 1;
                        }))
                        .then(literal("offset").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap.offset").append(Text.translatable("skyblockmod:options.query")).append(SkyblockMod.skyblockMod.options.dungeonMapX.getValue() + ", " + SkyblockMod.skyblockMod.options.dungeonMapY.getValue()));
                                    return 1;
                                })
                                .then(argument("offsetX", IntegerArgumentType.integer()).executes(context -> {
                                            SkyblockMod.skyblockMod.options.dungeonMapX.setValue(IntegerArgumentType.getInteger(context, "offsetX"));
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap.offset").append(Text.translatable("skyblockmod:options.set")).append(SkyblockMod.skyblockMod.options.dungeonMapX.getValue() + ", " + SkyblockMod.skyblockMod.options.dungeonMapY.getValue()));
                                            return 1;
                                        })
                                        .then(argument("offsetY", IntegerArgumentType.integer()).executes(context -> {
                                            SkyblockMod.skyblockMod.options.dungeonMapX.setValue(IntegerArgumentType.getInteger(context, "offsetX"));
                                            SkyblockMod.skyblockMod.options.dungeonMapY.setValue(IntegerArgumentType.getInteger(context, "offsetY"));
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap.offset").append(Text.translatable("skyblockmod:options.set")).append(SkyblockMod.skyblockMod.options.dungeonMapX.getValue() + ", " + SkyblockMod.skyblockMod.options.dungeonMapY.getValue()));
                                            return 1;
                                        }))))
                        .then(literal("scale").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap.scale").append(Text.translatable("skyblockmod:options.query")).append(String.format("%.2f", SkyblockMod.skyblockMod.options.dungeonMapScale.getValue())));
                                    return 1;
                                })
                                .then(argument("scale", DoubleArgumentType.doubleArg()).executes(context -> {
                                    SkyblockMod.skyblockMod.options.dungeonMapScale.setValue(DoubleArgumentType.getDouble(context, "scale"));
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonMap.scale").append(Text.translatable("skyblockmod:options.set")).append(String.format("%.2f", SkyblockMod.skyblockMod.options.dungeonMapScale.getValue())));
                                    return 1;
                                }))))
                .then(literal("dungeonScore").executes(context -> {
                            context.getSource().sendFeedback(Text.of("/sbm score [270|300] [on|off|message]"));
                            return 1;
                        })
                        .then(literal("270").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.270").append(queryOnOrOff(SkyblockMod.skyblockMod.dungeonScore.on270)).append(": " + SkyblockMod.skyblockMod.dungeonScore.text270));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.dungeonScore.on270 = BoolArgumentType.getBool(context, "value");
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.270").append(turnOnOrOff(SkyblockMod.skyblockMod.dungeonScore.on270)).append(": " + SkyblockMod.skyblockMod.dungeonScore.text270));
                                    return 1;
                                }))
                                .then(literal("message").executes(context -> {
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.270.text").append(queryOnOrOff(SkyblockMod.skyblockMod.dungeonScore.on270)).append(": " + SkyblockMod.skyblockMod.dungeonScore.text270));
                                            return 1;
                                        })
                                        .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                            SkyblockMod.skyblockMod.dungeonScore.text270 = StringArgumentType.getString(context, "message");
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.270.text").append(Text.translatable("skyblockmod:options.set")).append(ScreenTexts.onOrOff(SkyblockMod.skyblockMod.dungeonScore.on270)).append(": " + SkyblockMod.skyblockMod.dungeonScore.text270));
                                            return 1;
                                        }))))
                        .then(literal("300").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.300").append(queryOnOrOff(SkyblockMod.skyblockMod.dungeonScore.on300)).append(": " + SkyblockMod.skyblockMod.dungeonScore.text300));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.dungeonScore.on300 = BoolArgumentType.getBool(context, "value");
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.300").append(turnOnOrOff(SkyblockMod.skyblockMod.dungeonScore.on300)).append(": " + SkyblockMod.skyblockMod.dungeonScore.text300));
                                    return 1;
                                }))
                                .then(literal("message").executes(context -> {
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.300.text").append(queryOnOrOff(SkyblockMod.skyblockMod.dungeonScore.on300)).append(": " + SkyblockMod.skyblockMod.dungeonScore.text300));
                                            return 1;
                                        })
                                        .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                            SkyblockMod.skyblockMod.dungeonScore.text300 = StringArgumentType.getString(context, "message");
                                            context.getSource().sendFeedback(Text.translatable("skyblockmod:dungeonScore.300.text").append(Text.translatable("skyblockmod:options.set")).append(ScreenTexts.onOrOff(SkyblockMod.skyblockMod.dungeonScore.on300)).append(": " + SkyblockMod.skyblockMod.dungeonScore.text300));
                                            return 1;
                                        })))))
                .then(literal("experiments").executes(context -> {
                            context.getSource().sendFeedback(Text.of("/sbm experiments [chronomatron|superpairs|ultrasequencer] [on|off]"));
                            return 1;
                        })
                        .then(literal("chronomatron").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.chronomatron").append(queryOnOrOff(SkyblockMod.skyblockMod.experiments.toggleChronomatron)));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.experiments.toggleChronomatron = BoolArgumentType.getBool(context, "value");
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.chronomatron").append(turnOnOrOff(SkyblockMod.skyblockMod.experiments.toggleChronomatron)));
                                    return 1;
                                })))
                        .then(literal("superpairs").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.superpairs").append(queryOnOrOff(SkyblockMod.skyblockMod.experiments.toggleSuperpairs)));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.experiments.toggleSuperpairs = BoolArgumentType.getBool(context, "value");
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.superpairs").append(turnOnOrOff(SkyblockMod.skyblockMod.experiments.toggleSuperpairs)));
                                    return 1;
                                })))
                        .then(literal("ultrasequencer").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.ultrasequencer").append(queryOnOrOff(SkyblockMod.skyblockMod.experiments.toggleUltrasequencer)));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.experiments.toggleUltrasequencer = BoolArgumentType.getBool(context, "value");
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:experiments.ultrasequencer").append(turnOnOrOff(SkyblockMod.skyblockMod.experiments.toggleUltrasequencer)));
                                    return 1;
                                }))))
                .then(literal("fairySouls").executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:fairySouls").append(queryOnOrOff(SkyblockMod.skyblockMod.fairySouls.on)));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.fairySouls.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:fairySouls").append(turnOnOrOff(SkyblockMod.skyblockMod.fairySouls.on)));
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
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:fishingHelper").append(queryOnOrOff(SkyblockMod.skyblockMod.fishing.on)));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.fishing.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:fishingHelper").append(turnOnOrOff(SkyblockMod.skyblockMod.fishing.on)));
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
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:lividColor").append(queryOnOrOff(SkyblockMod.skyblockMod.lividColor.on)).append(": " + SkyblockMod.skyblockMod.lividColor.text));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.lividColor.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:lividColor").append(turnOnOrOff(SkyblockMod.skyblockMod.lividColor.on)).append(": " + SkyblockMod.skyblockMod.lividColor.text));
                            return 1;
                        }))
                        .then(literal("message").executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:lividColor").append(queryOnOrOff(SkyblockMod.skyblockMod.lividColor.on)).append(": " + SkyblockMod.skyblockMod.lividColor.text));
                                    return 1;
                                })
                                .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                    SkyblockMod.skyblockMod.lividColor.text = StringArgumentType.getString(context, "message");
                                    context.getSource().sendFeedback(Text.translatable("skyblockmod:lividColor").append(Text.translatable("skyblockmod:options.set")).append(ScreenTexts.onOrOff(SkyblockMod.skyblockMod.lividColor.on)).append(": " + SkyblockMod.skyblockMod.lividColor.text));
                                    return 1;
                                }))))
                .then(literal("quiverWarning").executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:quiver").append(queryOnOrOff(SkyblockMod.skyblockMod.quiverWarning.on)));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.quiverWarning.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:quiver").append(turnOnOrOff(SkyblockMod.skyblockMod.quiverWarning.on)));
                            return 1;
                        })))
                .then(literal("reparty").executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:reparty").append(queryOnOrOff(SkyblockMod.skyblockMod.reparty.on)));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.reparty.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.translatable("skyblockmod:reparty").append(turnOnOrOff(SkyblockMod.skyblockMod.reparty.on)));
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
