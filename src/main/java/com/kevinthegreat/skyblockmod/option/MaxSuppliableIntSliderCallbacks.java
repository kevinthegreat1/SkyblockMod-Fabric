package com.kevinthegreat.skyblockmod.option;

import com.mojang.serialization.Codec;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;
import java.util.function.IntSupplier;

public record MaxSuppliableIntSliderCallbacks(int minInclusive, IntSupplier maxSupplier) implements SimpleOption.IntSliderCallbacks {
    @Override
    public int maxInclusive() {
        return this.maxSupplier.getAsInt();
    }

    @Override
    public Optional<Integer> validate(Integer integer) {
        return Optional.of(MathHelper.clamp(integer, this.minInclusive(), this.maxInclusive()));
    }

    @Override
    public Codec<Integer> codec() {
        return Codec.intRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}
