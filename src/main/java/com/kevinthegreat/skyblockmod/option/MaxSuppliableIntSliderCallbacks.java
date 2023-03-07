package com.kevinthegreat.skyblockmod.option;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;

public record MaxSuppliableIntSliderCallbacks(int minInclusive, IntSupplier maxSupplier) implements SimpleOption.IntSliderCallbacks {
    @Override
    public int maxInclusive() {
        return this.maxSupplier.getAsInt();
    }

    @Override
    public Function<SimpleOption<Integer>, ClickableWidget> getWidgetCreator(SimpleOption.TooltipFactory<Integer> tooltipFactory, GameOptions gameOptions, int x, int y, int width, Consumer<Integer> changeCallback) {
        return SimpleOption.IntSliderCallbacks.super.getWidgetCreator(tooltipFactory, gameOptions, x, y, width, changeCallback);
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
