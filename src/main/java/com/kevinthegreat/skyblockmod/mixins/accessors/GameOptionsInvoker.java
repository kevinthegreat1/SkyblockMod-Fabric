package com.kevinthegreat.skyblockmod.mixins.accessors;

import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameOptions.class)
public interface GameOptionsInvoker {
    @SuppressWarnings("unused")
    @Invoker("getPixelValueText")
    static Text getPixelValueText(Text prefix, int value) {
        throw new IllegalStateException("Mixin invoker failed to apply.");
    }
}
