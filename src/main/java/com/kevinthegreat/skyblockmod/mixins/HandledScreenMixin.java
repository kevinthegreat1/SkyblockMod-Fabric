package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.misc.Experiments;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    private final Experiments skyblockmod_experiments = SkyblockMod.skyblockMod.experiments;

    @Inject(method = "drawSlot", at = @At(value = "TAIL"))
    private void skyblockmod_fillSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        if (skyblockmod_experiments.type != Experiments.Type.NONE && skyblockmod_experiments.state == Experiments.State.SHOW && slot.inventory instanceof SimpleInventory) {
            switch (skyblockmod_experiments.type) {
                case CHRONOMATRON -> {
                    Item item = skyblockmod_experiments.chronomatronSlots.get(skyblockmod_experiments.chronomatronCurrentOrdinal);
                    if (slot.getStack().isOf(item) || Experiments.terracottaToGlass.get(slot.getStack().getItem()) == item) {
                        context.getMatrices().push();
                        context.getMatrices().translate(0, 0, 300);
                        context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, -1073676544);
                        context.getMatrices().pop();
                    }
                }
                case SUPERPAIRS -> {
                    ItemStack itemStack = skyblockmod_experiments.superpairsSlots.get(slot.getIndex());
                    if (itemStack != null && !ItemStack.areEqual(itemStack, slot.getStack())) {
                        context.getMatrices().push();
                        context.getMatrices().translate(0, 0, 300);
                        if (ItemStack.areEqual(skyblockmod_experiments.superpairsCurrentSlot, itemStack) && slot.getStack().getName().getString().equals("Click a second button!")) {
                            context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, -1073676544);
                        } else if (skyblockmod_experiments.superpairsDuplicatedSlots.contains(slot.getIndex())) {
                            context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, -1056964864);
                        } else {
                            context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, 1090453504);
                        }
                        context.getMatrices().pop();
                    }
                }
                case ULTRASEQUENCER -> {
                    if (slot.getIndex() == skyblockmod_experiments.ultrasequencerNextSlot) {
                        context.getMatrices().push();
                        context.getMatrices().translate(0, 0, 300);
                        context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, -1073676544);
                        context.getMatrices().pop();
                    }
                }
            }
        }
    }

    @Redirect(method = {"drawSlot", "drawMouseoverTooltip"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;", ordinal = 0))
    private ItemStack skyblockmod_getStack(Slot slot) {
        if (skyblockmod_experiments.type != Experiments.Type.NONE && skyblockmod_experiments.state == Experiments.State.SHOW && slot.inventory instanceof SimpleInventory) {
            switch (skyblockmod_experiments.type) {
                case SUPERPAIRS -> {
                    ItemStack itemStack = skyblockmod_experiments.superpairsSlots.get(slot.getIndex());
                    return itemStack == null ? slot.getStack() : itemStack;
                }
                case ULTRASEQUENCER -> {
                    ItemStack itemStack = skyblockmod_experiments.ultrasequencerSlots.get(slot.getIndex());
                    return itemStack == null ? slot.getStack() : itemStack;
                }
            }
        }
        return slot.getStack();
    }

    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickSlot(IIILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V"))
    private void skyblockmod_onSlotClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (slot != null) {
            if (skyblockmod_experiments.type != Experiments.Type.NONE && skyblockmod_experiments.state == Experiments.State.SHOW && slot.inventory instanceof SimpleInventory) {
                switch (skyblockmod_experiments.type) {
                    case CHRONOMATRON -> {
                        Item item = skyblockmod_experiments.chronomatronSlots.get(skyblockmod_experiments.chronomatronCurrentOrdinal);
                        if (slot.getStack().isOf(item) || Experiments.terracottaToGlass.get(slot.getStack().getItem()) == item) {
                            if (++skyblockmod_experiments.chronomatronCurrentOrdinal >= skyblockmod_experiments.chronomatronSlots.size()) {
                                skyblockmod_experiments.state = Experiments.State.END;
                            }
                        }
                    }
                    case SUPERPAIRS -> {
                        skyblockmod_experiments.superpairsPrevClickedSlot = slot.getIndex();
                        skyblockmod_experiments.superpairsCurrentSlot = ItemStack.EMPTY;
                    }
                    case ULTRASEQUENCER -> {
                        if (slot.getIndex() == skyblockmod_experiments.ultrasequencerNextSlot) {
                            int count = skyblockmod_experiments.ultrasequencerSlots.get(skyblockmod_experiments.ultrasequencerNextSlot).getCount() + 1;
                            skyblockmod_experiments.ultrasequencerSlots.entrySet().stream().filter(entry -> entry.getValue().getCount() == count).findAny().ifPresentOrElse((entry) -> skyblockmod_experiments.ultrasequencerNextSlot = entry.getKey(), () -> skyblockmod_experiments.state = Experiments.State.END);
                        }
                    }
                }
            }
        }
    }
}
