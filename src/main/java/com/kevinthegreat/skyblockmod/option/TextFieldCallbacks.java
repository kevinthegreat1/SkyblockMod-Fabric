package com.kevinthegreat.skyblockmod.option;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class TextFieldCallbacks<T> implements SimpleOption.Callbacks<T> {
    int maxLength = 32;

    public TextFieldCallbacks() {
    }

    public TextFieldCallbacks(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public Function<SimpleOption<T>, ClickableWidget> getWidgetCreator(SimpleOption.TooltipFactory<T> tooltipFactory, GameOptions gameOptions, int x, int y, int width, Consumer<T> changeCallback) {
        return option -> new OptionTextFieldWidgetImpl<>(x, y, width, 20, maxLength, option, this, tooltipFactory, changeCallback);
    }

    abstract T toValue(String text);

    @Override
    public Optional<T> validate(T value) {
        return Optional.of(value);
    }
}
