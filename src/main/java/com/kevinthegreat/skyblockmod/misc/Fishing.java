package com.kevinthegreat.skyblockmod.misc;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.TypedActionResult;

public class Fishing {
    public boolean on = true;
    public boolean fishing = false;
    public long startTime;

    public Fishing() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack = player.getStackInHand(hand);
            if (on) {
                if (itemStack.isOf(Items.FISHING_ROD)) {
                    fishing = true;
                    startTime = System.currentTimeMillis();
                }
            }
            return TypedActionResult.pass(itemStack);
        });
    }
}