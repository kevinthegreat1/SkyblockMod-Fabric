package com.kevinthegreat.skyblockmod.util;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class EmptyButtonWidget extends ButtonWidget {
    private EmptyButtonWidget(int width, int height) {
        super(0, 0, width, height, Text.of(""), button -> {}, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }

    public static EmptyButtonWidget ofHeight(int height) {
        return new EmptyButtonWidget(0, height);
    }
}
