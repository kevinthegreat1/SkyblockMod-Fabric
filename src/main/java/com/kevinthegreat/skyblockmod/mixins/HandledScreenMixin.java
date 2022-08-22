package com.kevinthegreat.skyblockmod.mixins;

import com.kevinthegreat.skyblockmod.SkyblockMod;
import com.kevinthegreat.skyblockmod.misc.Experiments;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
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
public class HandledScreenMixin {
    final Experiments experiments = SkyblockMod.skyblockMod.experiments;

    @Inject(method = "drawSlot", at = @At(value = "TAIL"))
    private void fillSlot(MatrixStack matrices, Slot slot, CallbackInfo ci) {
        if (experiments.type != Experiments.Type.NONE && experiments.state == Experiments.State.SHOW && slot.inventory instanceof SimpleInventory) {
            switch (experiments.type) {
                case CHRONOMATRON -> {
                    Item item = experiments.chronomatronSlots.get(experiments.chronomatronCurrentOrdinal);
                    if (slot.getStack().isOf(item) || Experiments.terracottaToGlass.get(slot.getStack().getItem()) == item) {
                        matrices.push();
                        matrices.translate(0, 0, 300);
                        DrawableHelper.fill(matrices, slot.x, slot.y, slot.x + 16, slot.y + 16, -1073676544);
                        matrices.pop();
                    }
                }
                case SUPERPAIRS -> {
                    ItemStack itemStack = experiments.superpairsSlots.get(slot.getIndex());
                    if (itemStack != null && !ItemStack.areEqual(itemStack, slot.getStack())) {
                        matrices.push();
                        matrices.translate(0, 0, 300);
                        if (ItemStack.areEqual(experiments.superpairsCurrentSlot, itemStack) && slot.getStack().getName().getString().equals("Click a second button!")) {
                            DrawableHelper.fill(matrices, slot.x, slot.y, slot.x + 16, slot.y + 16, -1073676544);
                        } else if (experiments.superpairsDuplicatedSlots.contains(slot.getIndex())) {
                            DrawableHelper.fill(matrices, slot.x, slot.y, slot.x + 16, slot.y + 16, -1056964864);
                        } else {
                            DrawableHelper.fill(matrices, slot.x, slot.y, slot.x + 16, slot.y + 16, 1090453504);
                        }
                        matrices.pop();
                    }
                }
                case ULTRASEQUENCER -> {
                    if (slot.getIndex() == experiments.ultrasequencerNextSlot) {
                        matrices.push();
                        matrices.translate(0, 0, 300);
                        DrawableHelper.fill(matrices, slot.x, slot.y, slot.x + 16, slot.y + 16, -1073676544);
                        matrices.pop();
                    }
                }
            }
        }
    }

    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;", ordinal = 0))
    private ItemStack getStack(Slot slot) {
        if (experiments.type != Experiments.Type.NONE && experiments.state == Experiments.State.SHOW && slot.inventory instanceof SimpleInventory) {
            switch (experiments.type) {
                case SUPERPAIRS -> {
                    ItemStack itemStack = experiments.superpairsSlots.get(slot.getIndex());
                    return itemStack == null ? slot.getStack() : itemStack;
                }
                case ULTRASEQUENCER -> {
                    ItemStack itemStack = experiments.ultrasequencerSlots.get(slot.getIndex());
                    return itemStack == null ? slot.getStack() : itemStack;
                }
            }
        }
        return slot.getStack();
    }

    @Redirect(method = "drawMouseoverTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack getStackTooltip(Slot slot) {
        return getStack(slot);
    }

    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickSlot(IIILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V"))
    private void onSlotClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (slot != null) {
            if (experiments.type != Experiments.Type.NONE && experiments.state == Experiments.State.SHOW && slot.inventory instanceof SimpleInventory) {
                switch (experiments.type) {
                    case CHRONOMATRON -> {
                        Item item = experiments.chronomatronSlots.get(experiments.chronomatronCurrentOrdinal);
                        if (slot.getStack().isOf(item) || Experiments.terracottaToGlass.get(slot.getStack().getItem()) == item) {
                            if (++experiments.chronomatronCurrentOrdinal >= experiments.chronomatronSlots.size()) {
                                experiments.state = Experiments.State.END;
                            }
                        }
                    }
                    case SUPERPAIRS -> {
                        experiments.superpairsPrevClickedSlot = slot.getIndex();
                        experiments.superpairsCurrentSlot = ItemStack.EMPTY;
                    }
                    case ULTRASEQUENCER -> {
                        if (slot.getIndex() == experiments.ultrasequencerNextSlot) {
                            int count = experiments.ultrasequencerSlots.get(experiments.ultrasequencerNextSlot).getCount() + 1;
                            experiments.ultrasequencerSlots.entrySet().stream().filter(entry -> entry.getValue().getCount() == count).findAny().ifPresentOrElse((entry) -> experiments.ultrasequencerNextSlot = entry.getKey(), () -> experiments.state = Experiments.State.END);
                        }
                    }
                }
            }
        }
    }
}
