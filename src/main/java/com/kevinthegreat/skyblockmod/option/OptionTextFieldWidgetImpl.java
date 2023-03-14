package com.kevinthegreat.skyblockmod.option;

import com.kevinthegreat.skyblockmod.mixins.accessors.SimpleOptionAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.SimpleOption;

import java.util.function.Consumer;

public class OptionTextFieldWidgetImpl<T> extends TextFieldWidget {
    SimpleOption<T> option;
    TextFieldCallbacks<T> callbacks;
    SimpleOption.TooltipFactory<T> tooltipFactory;
    Consumer<T> changeCallback;

    public OptionTextFieldWidgetImpl(int x, int y, int width, int height, int maxLength, SimpleOption<T> option, TextFieldCallbacks<T> callbacks, SimpleOption.TooltipFactory<T> tooltipFactory, Consumer<T> changeCallback) {
        super(MinecraftClient.getInstance().textRenderer, x, y, width, height, ((SimpleOptionAccessor) (Object) option).getTextGetter().apply(option.getValue()));
        this.option = option;
        this.callbacks = callbacks;
        this.tooltipFactory = tooltipFactory;
        this.changeCallback = changeCallback;
        setMaxLength(maxLength);
        updateText();
    }

    protected void updateText() {
        setText(((SimpleOptionAccessor) (Object) option).getTextGetter().apply(option.getValue()).getString());
        setTooltip(tooltipFactory.apply(option.getValue()));
    }

    @Override
    protected void onChanged(String newText) {
        if (!newText.equals(((SimpleOptionAccessor) (Object) option).getTextGetter().apply(option.getValue()).getString())) {
            option.setValue(callbacks.toValue(newText));
            changeCallback.accept(option.getValue());
            updateText();
        }
        super.onChanged(newText);
    }
}
