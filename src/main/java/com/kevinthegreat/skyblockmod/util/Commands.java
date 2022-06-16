package com.kevinthegreat.skyblockmod.util;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class Commands {
    public void registerCommands() {
        CommandDispatcher<FabricClientCommandSource> DISPATCHER = ClientCommandManager.DISPATCHER;
        for (String key : SkyblockMod.skyblockMod.message.commands.keySet()) {
            if (key.startsWith("/")) {
                DISPATCHER.register(literal(key.substring(1)));
            }
        }
        for (String key : SkyblockMod.skyblockMod.message.commandsArgs.keySet()) {
            if (key.startsWith("/")) {
                DISPATCHER.register(literal(key.substring(1)));
            }
        }
        DISPATCHER.register(literal("reparty").executes(context -> {
            SkyblockMod.skyblockMod.reparty.start();
            SkyblockMod.skyblockMod.message.sendMessageAfterCooldown("/pl");
            SkyblockMod.skyblockMod.message.addMessage(Text.of("Reparting..."));
            return 1;
        }));
        DISPATCHER.register(literal("sbm")
                .then(literal("config").executes(context -> {
                            SkyblockMod.skyblockMod.message.addMessage(Text.of("/sbm config [save|load]"));
                            return 1;
                        })
                        .then(literal("reload").executes(context -> {
                            SkyblockMod.skyblockMod.config.load();
                            SkyblockMod.skyblockMod.message.addMessage(Text.of("Reloaded config file"));
                            return 1;
                        }))
                        .then(literal("save").executes(context -> {
                            SkyblockMod.skyblockMod.config.save();
                            SkyblockMod.skyblockMod.message.addMessage(Text.of("Saved config file"));
                            return 1;
                        })))
                .then(literal("dungeonMap").executes(context -> {
                            SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.dungeonMap.on ? "Dungeon map is on" : "Dungeon map is off"));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.dungeonMap.on = BoolArgumentType.getBool(context, "value");
                            SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.dungeonMap.on ? "Dungeon map turned on" : "Dungeon map turned off"));
                            return 1;
                        }))
                        .then(literal("offset").executes(context -> {
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of("Dungeon map offset is " + SkyblockMod.skyblockMod.dungeonMap.offsetX + ", " + SkyblockMod.skyblockMod.dungeonMap.offsetY));
                                    return 1;
                                })
                                .then(argument("offsetX", IntegerArgumentType.integer()).executes(context -> {
                                            SkyblockMod.skyblockMod.dungeonMap.offsetX = IntegerArgumentType.getInteger(context, "offsetX");
                                            SkyblockMod.skyblockMod.message.addMessage(Text.of("Dungeon map offset set to " + SkyblockMod.skyblockMod.dungeonMap.offsetX + ", " + SkyblockMod.skyblockMod.dungeonMap.offsetY));
                                            return 1;
                                        })
                                        .then(argument("offsetY", IntegerArgumentType.integer()).executes(context -> {
                                            SkyblockMod.skyblockMod.dungeonMap.offsetX = IntegerArgumentType.getInteger(context, "offsetX");
                                            SkyblockMod.skyblockMod.dungeonMap.offsetY = IntegerArgumentType.getInteger(context, "offsetY");
                                            SkyblockMod.skyblockMod.message.addMessage(Text.of("Dungeon map offset set to " + SkyblockMod.skyblockMod.dungeonMap.offsetX + ", " + SkyblockMod.skyblockMod.dungeonMap.offsetY));
                                            return 1;
                                        }))))
                        .then(literal("scale").executes(context -> {
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of("Dungeon map scale is " + SkyblockMod.skyblockMod.dungeonMap.scale));
                                    return 1;
                                })
                                .then(argument("scale", FloatArgumentType.floatArg()).executes(context -> {
                                    SkyblockMod.skyblockMod.dungeonMap.scale = FloatArgumentType.getFloat(context, "scale");
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of("Dungeon map scale set to " + SkyblockMod.skyblockMod.dungeonMap.scale));
                                    return 1;
                                }))))
                .then(literal("dungeonScore").executes(context -> {
                            SkyblockMod.skyblockMod.message.addMessage(Text.of("/sbm score [270|300] [on|off|message]"));
                            return 1;
                        })
                        .then(literal("270").executes(context -> {
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.dungeonScore.on270 ? "Dungeon score 270 is on: " : "Dungeon score 270 is off: ") + SkyblockMod.skyblockMod.dungeonScore.text270));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.dungeonScore.on270 = BoolArgumentType.getBool(context, "value");
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.dungeonScore.on270 ? "Dungeon score 270 turned on" : "Dungeon score 270 turned off"));
                                    return 1;
                                }))
                                .then(literal("message").executes(context -> {
                                            SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.dungeonScore.on270 ? "Dungeon score 270 is on: " : "Dungeon score 270 is off: ") + SkyblockMod.skyblockMod.dungeonScore.text270));
                                            return 1;
                                        })
                                        .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                            SkyblockMod.skyblockMod.dungeonScore.text270 = StringArgumentType.getString(context, "message");
                                            SkyblockMod.skyblockMod.message.addMessage(Text.of("Dungeon score 270 text set to " + SkyblockMod.skyblockMod.dungeonScore.text270));
                                            return 1;
                                        }))))
                        .then(literal("300").executes(context -> {
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.dungeonScore.on300 ? "Dungeon score 300 is on: " : "Dungeon score 300 is off: ") + SkyblockMod.skyblockMod.dungeonScore.text300));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.dungeonScore.on300 = BoolArgumentType.getBool(context, "value");
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.dungeonScore.on300 ? "Dungeon score 300 turned on" : "Dungeon score 300 turned off"));
                                    return 1;
                                }))
                                .then(literal("message").executes(context -> {
                                            SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.dungeonScore.on300 ? "Dungeon score 300 is on: " : "Dungeon score 300 is off: ") + SkyblockMod.skyblockMod.dungeonScore.text300));
                                            return 1;
                                        })
                                        .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                            SkyblockMod.skyblockMod.dungeonScore.text300 = StringArgumentType.getString(context, "message");
                                            SkyblockMod.skyblockMod.message.addMessage(Text.of("Dungeon score 300 text set to " + SkyblockMod.skyblockMod.dungeonScore.text300));
                                            return 1;
                                        })))))
                .then(literal("experiments").executes(context -> {
                            SkyblockMod.skyblockMod.message.addMessage(Text.of("/sbm experiments [chronomatron|ultrasequencer|superpairs] [on|off]"));
                            return 1;
                        })
                        .then(literal("chronomatron").executes(context -> {
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.experiments.toggleChronomatron ? "Chronomatron solver is on" : "Chronomatron solver is off")));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.experiments.toggleChronomatron = BoolArgumentType.getBool(context, "value");
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.experiments.toggleChronomatron ? "Chronomatron solver turned on" : "Chronomatron solver turned off"));
                                    return 1;
                                })))
                        .then(literal("superpairs").executes(context -> {
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.experiments.toggleSuperpairs ? "Superpairs solver is on" : "Superpairs solver is off")));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.experiments.toggleSuperpairs = BoolArgumentType.getBool(context, "value");
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.experiments.toggleSuperpairs ? "Superpairs solver turned on" : "Superpairs solver turned off"));
                                    return 1;
                                })))
                        .then(literal("ultrasequencer").executes(context -> {
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.experiments.toggleUltrasequencer ? "Ultrasequencer solver is on" : "Ultrasequencer solver is off")));
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                                    SkyblockMod.skyblockMod.experiments.toggleUltrasequencer = BoolArgumentType.getBool(context, "value");
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.experiments.toggleUltrasequencer ? "Ultrasequencer solver turned on" : "Ultrasequencer solver turned off"));
                                    return 1;
                                }))))
                .then(literal("fishingHelper").executes(context -> {
                            SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.fishing.on ? "Fishing helper is on" : "Fishing helper is off"));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.fishing.on = BoolArgumentType.getBool(context, "value");
                            SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.fishing.on ? "Fishing helper turned on" : "Fishing helper turned off"));
                            return 1;
                        })))
                .then(literal("lividColor").executes(context -> {
                            SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.lividColor.on ? "Livid color is on: " : "Livid color is off: ") + SkyblockMod.skyblockMod.lividColor.text));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.lividColor.on = BoolArgumentType.getBool(context, "value");
                            SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.lividColor.on ? "Livid color turned on" : "Livid color turned off"));
                            return 1;
                        }))
                        .then(literal("message").executes(context -> {
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.lividColor.on ? "Livid color is on: " : "Livid color is off: ") + SkyblockMod.skyblockMod.lividColor.text));
                                    return 1;
                                })
                                .then(argument("message", StringArgumentType.greedyString()).executes(context -> {
                                    SkyblockMod.skyblockMod.lividColor.text = StringArgumentType.getString(context, "message");
                                    SkyblockMod.skyblockMod.message.addMessage(Text.of("Livid color text set to: " + SkyblockMod.skyblockMod.lividColor.text));
                                    return 1;
                                }))))
                .then(literal("quiverWarning").executes(context -> {
                            SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.quiverWarning.on ? "Quiver warning is on" : "Quiver warning is off")));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.quiverWarning.on = BoolArgumentType.getBool(context, "value");
                            SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.quiverWarning.on ? "Quiver warning turned on" : "Quiver warning turned off"));
                            return 1;
                        })))
                .then(literal("reparty").executes(context -> {
                            SkyblockMod.skyblockMod.message.addMessage(Text.of((SkyblockMod.skyblockMod.reparty.on ? "Reparty is on" : "Reparty is off")));
                            return 1;
                        })
                        .then(argument("value", BoolArgumentType.bool()).executes(context -> {
                            SkyblockMod.skyblockMod.reparty.on = BoolArgumentType.getBool(context, "value");
                            SkyblockMod.skyblockMod.message.addMessage(Text.of(SkyblockMod.skyblockMod.reparty.on ? "Reparty turned on" : "Reparty turned off"));
                            return 1;
                        }))));
    }
}
