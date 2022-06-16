package com.kevinthegreat.skyblockmod.misc;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Experiments {
    public boolean toggleChronomatron = true;
    public boolean toggleSuperpairs = true;
    public boolean toggleUltrasequencer = true;

    public enum Type {
        NONE, CHRONOMATRON, SUPERPAIRS, ULTRASEQUENCER
    }

    public enum State {
        REMEMBER, WAIT, SHOW
    }

    public Type type = Type.NONE;
    public State state = State.REMEMBER;

    public final Map<Integer, ItemStack> superpairsSlots = new HashMap<>();
    public int superpairsLastClickedSlot;
    public ItemStack superpairsCurrentSlot;
    public final Set<Integer> superpairsDuplicatedSlots = new HashSet<>();
    public final Map<Integer, ItemStack> ultrasequencerSlots = new HashMap<>();
    public int ultrasequencerNextSlotIndex;

    public Experiments() {
        ScreenEvents.AFTER_INIT.register((minecraftClient, screen, scaledWidth, scaledHeight) -> {
            if ((toggleChronomatron || toggleSuperpairs || toggleUltrasequencer) && screen instanceof GenericContainerScreen genericContainerScreen) {
                String title = genericContainerScreen.getTitle().getString();
                int index = title.indexOf('(');
                if (11 <= index && index <= 15) {
                    switch (title.substring(0, index)) {
                        case "Chronomatron " -> {
                            if (toggleChronomatron) {
                                type = Type.CHRONOMATRON;
                                state = State.REMEMBER;
                                chronomatron(genericContainerScreen);
                            }
                        }
                        case "Superpairs " -> {
                            if (toggleSuperpairs) {
                                type = Type.SUPERPAIRS;
                                state = State.SHOW;
                                superpairs(genericContainerScreen);
                            }
                        }
                        case "Ultrasequencer " -> {
                            if (toggleUltrasequencer) {
                                type = Type.ULTRASEQUENCER;
                                state = State.REMEMBER;
                                ultrasequencer(genericContainerScreen);
                            }
                        }
                    }
                }
            }
        });
    }

    public void tick() {
        if (type != Type.NONE) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            if (currentScreen instanceof GenericContainerScreen genericContainerScreen) {
                String title = genericContainerScreen.getTitle().getString();
                switch (type) {
                    case CHRONOMATRON -> {
                        if (toggleChronomatron && title.startsWith("Chronomatron (")) {
                            chronomatron(genericContainerScreen);
                        } else {
                            reset();
                        }
                    }
                    case SUPERPAIRS -> {
                        if (toggleSuperpairs && title.startsWith("Superpairs (")) {
                            superpairs(genericContainerScreen);
                        } else {
                            reset();
                        }
                    }
                    case ULTRASEQUENCER -> {
                        if (toggleUltrasequencer && title.startsWith("Ultrasequencer (")) {
                            ultrasequencer(genericContainerScreen);
                        } else {
                            reset();
                        }
                    }
                }
            } else {
                reset();
            }
        }
    }

    private void reset() {
        type = Type.NONE;
        state = State.REMEMBER;
        superpairsSlots.clear();
        ultrasequencerSlots.clear();
    }

    private void chronomatron(GenericContainerScreen screen) {
    }

    private void superpairs(GenericContainerScreen screen) {
        if (state == State.SHOW && superpairsSlots.get(superpairsLastClickedSlot) == null) {
            ItemStack itemStack = screen.getScreenHandler().getInventory().getStack(superpairsLastClickedSlot);
            if (!(itemStack.isOf(Items.CYAN_STAINED_GLASS) || itemStack.isOf(Items.BLACK_STAINED_GLASS_PANE) || itemStack.isOf(Items.AIR))) {
                superpairsSlots.entrySet().stream().filter((entry -> ItemStack.areEqual(entry.getValue(), itemStack))).findAny().ifPresent(entry -> superpairsDuplicatedSlots.add(entry.getKey()));
                superpairsSlots.put(superpairsLastClickedSlot, itemStack);
                superpairsCurrentSlot = itemStack;
            }
        }
        if (screen.getScreenHandler().getInventory().getStack(4).isOf(Items.CAULDRON)) {
            reset();
        }
    }

    private void ultrasequencer(GenericContainerScreen screen) {
        switch (state) {
            case REMEMBER -> {
                if (screen.getScreenHandler().getInventory().getStack(49).getName().getString().equals("Remember the pattern!")) {
                    for (Slot slot : screen.getScreenHandler().slots) {
                        String name = slot.getStack().getName().getString();
                        if (name.matches("\\d+")) {
                            if (name.equals("1")) {
                                ultrasequencerNextSlotIndex = slot.getIndex();
                            }
                            ultrasequencerSlots.put(slot.getIndex(), slot.getStack());
                        }
                    }
                    state = State.WAIT;
                }
            }
            case WAIT -> {
                if (screen.getScreenHandler().getInventory().getStack(49).getName().getString().startsWith("Timer: ")) {
                    state = State.SHOW;
                }
            }
            case SHOW -> {
                if (!screen.getScreenHandler().getInventory().getStack(49).getName().getString().startsWith("Timer: ")) {
                    ultrasequencerSlots.clear();
                    state = State.REMEMBER;
                }
            }
        }
    }
}
