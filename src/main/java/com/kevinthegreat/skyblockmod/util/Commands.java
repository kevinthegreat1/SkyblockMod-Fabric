package com.kevinthegreat.skyblockmod.util;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
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
            context.getSource().sendFeedback(Text.of("Reparting..."));
            return 1;
        }));
        dispatcher.register(literal("sbm")
                .then(literal("config").executes(context -> {
                            context.getSource().sendFeedback(Text.of("/sbm config [save|load]"));
                            return 1;
                        })
                        .then(literal("reload").executes(context -> {
                            SkyblockMod.skyblockMod.config.load();
                            context.getSource().sendFeedback(Text.of("Reloaded config file"));
                            return 1;
                        }))
                        .then(literal("save").executes(context -> {
                            SkyblockMod.skyblockMod.config.save(MinecraftClient.getInstance());
                            context.getSource().sendFeedback(Text.of("Saved config file"));
                            return 1;
                        })))
                .then(literal("dungeonMap").executes(context -> {
                            context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.dungeonMap.on ? "Dungeon map is on" : "Dungeon map is off"));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.dungeonMap.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.dungeonMap.on ? "Dungeon map turned on" : "Dungeon map turned off"));
                            return 1;
                        }))
                        .then(literal("offset").executes(context -> {
                                    context.getSource().sendFeedback(Text.of("Dungeon map offset is " + SkyblockMod.skyblockMod.dungeonMap.offsetX + ", " + SkyblockMod.skyblockMod.dungeonMap.offsetY));
                                    return 1;
                                })
                                .then(argument("offsetX", IntegerArgumentType.integer()).executes(context -> {
                                            SkyblockMod.skyblockMod.dungeonMap.offsetX = IntegerArgumentType.getInteger(context, "offsetX");
                                            context.getSource().sendFeedback(Text.of("Dungeon map offset set to " + SkyblockMod.skyblockMod.dungeonMap.offsetX + ", " + SkyblockMod.skyblockMod.dungeonMap.offsetY));
                                            return 1;
                                        })
                                        .then(argument("offsetY", IntegerArgumentType.integer()).executes(context -> {
                                            SkyblockMod.skyblockMod.dungeonMap.offsetX = IntegerArgumentType.getInteger(context, "offsetX");
                                            SkyblockMod.skyblockMod.dungeonMap.offsetY = IntegerArgumentType.getInteger(context, "offsetY");
                                            context.getSource().sendFeedback(Text.of("Dungeon map offset set to " + SkyblockMod.skyblockMod.dungeonMap.offsetX + ", " + SkyblockMod.skyblockMod.dungeonMap.offsetY));
                                            return 1;
                                        }))))
                        .then(literal("scale").executes(context -> {
                                    context.getSource().sendFeedback(Text.of("Dungeon map scale is " + SkyblockMod.skyblockMod.dungeonMap.scale));
                                    return 1;
                                })
                                .then(argument("scale", FloatArgumentType.floatArg()).executes(context -> {
                                    SkyblockMod.skyblockMod.dungeonMap.scale = FloatArgumentType.getFloat(context, "scale");
                                    context.getSource().sendFeedback(Text.of("Dungeon map scale set to " + SkyblockMod.skyblockMod.dungeonMap.scale));
                                    return 1;
                                }))))
                .then(literal("dungeonScore").executes(context -> {
                            context.getSource().sendFeedback(Text.of("/sbm score [270|300] [on|off|message]"));
                            return 1;
                        })
                        .then(literal("270").executes(context -> {
                                    context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.dungeonScore.on270 ? "Dungeon score 270 is on: " : "Dungeon score 270 is off: ") + SkyblockMod.skyblockMod.dungeonScore.text270));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.dungeonScore.on270 = BoolArgumentType.getBool(context, "value");
                                    context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.dungeonScore.on270 ? "Dungeon score 270 turned on" : "Dungeon score 270 turned off"));
                                    return 1;
                                }))
                                .then(literal("message").executes(context -> {
                                            context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.dungeonScore.on270 ? "Dungeon score 270 is on: " : "Dungeon score 270 is off: ") + SkyblockMod.skyblockMod.dungeonScore.text270));
                                            return 1;
                                        })
                                        .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                            SkyblockMod.skyblockMod.dungeonScore.text270 = StringArgumentType.getString(context, "message");
                                            context.getSource().sendFeedback(Text.of("Dungeon score 270 text set to " + SkyblockMod.skyblockMod.dungeonScore.text270));
                                            return 1;
                                        }))))
                        .then(literal("300").executes(context -> {
                                    context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.dungeonScore.on300 ? "Dungeon score 300 is on: " : "Dungeon score 300 is off: ") + SkyblockMod.skyblockMod.dungeonScore.text300));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.dungeonScore.on300 = BoolArgumentType.getBool(context, "value");
                                    context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.dungeonScore.on300 ? "Dungeon score 300 turned on" : "Dungeon score 300 turned off"));
                                    return 1;
                                }))
                                .then(literal("message").executes(context -> {
                                            context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.dungeonScore.on300 ? "Dungeon score 300 is on: " : "Dungeon score 300 is off: ") + SkyblockMod.skyblockMod.dungeonScore.text300));
                                            return 1;
                                        })
                                        .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                            SkyblockMod.skyblockMod.dungeonScore.text300 = StringArgumentType.getString(context, "message");
                                            context.getSource().sendFeedback(Text.of("Dungeon score 300 text set to " + SkyblockMod.skyblockMod.dungeonScore.text300));
                                            return 1;
                                        })))))
                .then(literal("experiments").executes(context -> {
                            context.getSource().sendFeedback(Text.of("/sbm experiments [chronomatron|ultrasequencer|superpairs] [on|off]"));
                            return 1;
                        })
                        .then(literal("chronomatron").executes(context -> {
                                    context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.experiments.toggleChronomatron ? "Chronomatron solver is on" : "Chronomatron solver is off")));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.experiments.toggleChronomatron = BoolArgumentType.getBool(context, "value");
                                    context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.experiments.toggleChronomatron ? "Chronomatron solver turned on" : "Chronomatron solver turned off"));
                                    return 1;
                                })))
                        .then(literal("superpairs").executes(context -> {
                                    context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.experiments.toggleSuperpairs ? "Superpairs solver is on" : "Superpairs solver is off")));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.experiments.toggleSuperpairs = BoolArgumentType.getBool(context, "value");
                                    context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.experiments.toggleSuperpairs ? "Superpairs solver turned on" : "Superpairs solver turned off"));
                                    return 1;
                                })))
                        .then(literal("ultrasequencer").executes(context -> {
                                    context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.experiments.toggleUltrasequencer ? "Ultrasequencer solver is on" : "Ultrasequencer solver is off")));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.experiments.toggleUltrasequencer = BoolArgumentType.getBool(context, "value");
                                    context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.experiments.toggleUltrasequencer ? "Ultrasequencer solver turned on" : "Ultrasequencer solver turned off"));
                                    return 1;
                                }))))
                .then(literal("fairySouls").executes(context -> {
                            context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.fairySouls.on ? "Fairy souls helper is on" : "Fairy souls helper is off"));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.fairySouls.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.fairySouls.on ? "Fairy souls helper turned on" : "Fairy souls helper turned off"));
                            return 1;
                        }))
                        .then(literal("markAllInCurrentIslandFound").executes(context -> {
                            SkyblockMod.skyblockMod.fairySouls.markAllFairiesFound();
                            context.getSource().sendFeedback(Text.of("Marked all fairy souls in the current island as found"));
                            return 1;
                        }))
                        .then(literal("markAllInCurrentIslandMissing").executes(context -> {
                            SkyblockMod.skyblockMod.fairySouls.markAllFairiesNotFound();
                            context.getSource().sendFeedback(Text.of("Marked all fairy souls in the current island as missing"));
                            return 1;
                        })))
                .then(literal("fishingHelper").executes(context -> {
                            context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.fishing.on ? "Fishing helper is on" : "Fishing helper is off"));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.fishing.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.fishing.on ? "Fishing helper turned on" : "Fishing helper turned off"));
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
                            context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.lividColor.on ? "Livid color is on: " : "Livid color is off: ") + SkyblockMod.skyblockMod.lividColor.text));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.lividColor.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.lividColor.on ? "Livid color turned on" : "Livid color turned off"));
                            return 1;
                        }))
                        .then(literal("message").executes(context -> {
                                    context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.lividColor.on ? "Livid color is on: " : "Livid color is off: ") + SkyblockMod.skyblockMod.lividColor.text));
                                    return 1;
                                })
                                .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                    SkyblockMod.skyblockMod.lividColor.text = StringArgumentType.getString(context, "message");
                                    context.getSource().sendFeedback(Text.of("Livid color text set to: " + SkyblockMod.skyblockMod.lividColor.text));
                                    return 1;
                                }))))
                .then(literal("quiverWarning").executes(context -> {
                            context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.quiverWarning.on ? "Quiver warning is on" : "Quiver warning is off")));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.quiverWarning.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.quiverWarning.on ? "Quiver warning turned on" : "Quiver warning turned off"));
                            return 1;
                        })))
                .then(literal("reparty").executes(context -> {
                            context.getSource().sendFeedback(Text.of((SkyblockMod.skyblockMod.reparty.on ? "Reparty is on" : "Reparty is off")));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.reparty.on = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(Text.of(SkyblockMod.skyblockMod.reparty.on ? "Reparty turned on" : "Reparty turned off"));
                            return 1;
                        }))));
    }
}
